package org.datarepo.impl;

import org.datarepo.Filter;
import org.datarepo.LookupIndex;
import org.datarepo.SearchIndex;
import org.datarepo.SearchableCollection;
import org.datarepo.query.*;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Types;
import org.datarepo.utils.LinearSearch;
import org.datarepo.utils.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FilterDefault implements Filter {

    Set<Operator> indexedOperators = Utils.set(Operator.BETWEEN, Operator.EQUAL, Operator.STARTS_WITH,
            Operator.GREATER_THAN, Operator.GREATER_THAN_EQUAL,
            Operator.LESS_THAN, Operator.LESS_THAN_EQUAL);

    Map<Expression, List> queryCache = new ConcurrentHashMap<>();
    int flushCount = 0;
    final boolean cache = true;

    @Override
    public List filter(SearchableCollection searchableCollection, Map<String, FieldAccess> fields, Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                       Expression... expressions) {

        checkCache();

        if (expressions.length == 1 && expressions[0] instanceof Criterion) {

            return simpleQueryPlanOneCriterion(searchableCollection, fields, lookupIndexMap, searchIndexMap,
                    (Criterion) expressions[0]);
        }

        return mainQueryPlan(searchableCollection, fields, lookupIndexMap, searchIndexMap, expressions);

    }

    private void checkCache() {
        if (cache) {
            if (flushCount > 10_000 && queryCache.size() > 10_000) {
                queryCache.clear();
            } else {
                flushCount = 0;
            }
        }
    }


    private List mainQueryPlan(SearchableCollection searchableCollection, Map<String, FieldAccess> fields, Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap, Expression[] expressions) {
        List results = null;

        Group group = (Group) Criteria.or(expressions);

        if (cache) {
            results = queryCache.get(group);

            if (results != null) {
                return results;
            }
        }

        results = doFilterGroup(searchableCollection, lookupIndexMap, searchIndexMap, group, fields);

        if (cache) {
            flushCount++;
            queryCache.put(group, results == null ? Collections.EMPTY_LIST : results);
        }
        return results;
    }

    private List simpleQueryPlanOneCriterion(SearchableCollection searchableCollection,
                                             Map<String, FieldAccess> fields, Map<String, LookupIndex> lookupIndexMap,
                                             Map<String, SearchIndex> searchIndexMap,
                                             Criterion criterion) {
        List results = null;

        if (cache) {
            results = queryCache.get(criterion);

            if (results != null) {
                return results;
            }
        }


        Operator operator = criterion.getOperator();
//        //I was using isIn here but it was causing most of the slow down.
//        if (operator == Operator.BETWEEN || operator == Operator.EQUAL || operator == Operator.STARTS_WITH || operator ==
//                Operator.GREATER_THAN || operator == Operator.GREATER_THAN_EQUAL || operator ==
//                Operator.LESS_THAN || operator == Operator.LESS_THAN_EQUAL) {
        //It is slighlty faster with isIn versus roll out.
        if (Utils.isIn(operator, indexedOperators)) {
            results = doFilterWithIndex(searchableCollection, lookupIndexMap, searchIndexMap, criterion, fields);
        } else {
            results = doFilterUsingLinear(searchableCollection.all(), criterion, fields);
        }

        if (cache) {
            flushCount++;
            queryCache.put(criterion, results == null ? Collections.EMPTY_LIST : results);
        }
        return results;
    }

    @Override
    public void invalidate() {
        queryCache.clear();

    }

    private List doFilterGroup(SearchableCollection searchableCollection, Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                               Group group, Map<String, FieldAccess> fields) {
        if (group.getGrouping() == Grouping.OR) {
            return or(searchableCollection, lookupIndexMap, searchIndexMap, group.getExpressions(), fields);
        } else {
            return and(searchableCollection, lookupIndexMap, searchIndexMap, group.getExpressions(), fields);

        }
    }

    private List or(SearchableCollection searchableCollection, Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                    Expression[] expressions,
                    Map<String, FieldAccess> fields) {

        HashSet set = new HashSet();
        for (Expression expression : expressions) {
            if (expression instanceof Criterion) {
                List list = doFilterWithIndex(searchableCollection, lookupIndexMap, searchIndexMap, (Criterion) expression, fields);
                set.addAll(list);
            }
            if (expression instanceof Group) {
                List list = doFilterGroup(searchableCollection, lookupIndexMap, searchIndexMap, (Group) expression, fields);
                set.addAll(list);
            }
        }
        return new ArrayList(set);


    }


    private List and(SearchableCollection searchableCollection, Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                     Expression[] expressions, Map<String, FieldAccess> fields) {

        List<HashSet> listOfSets = new ArrayList(new HashSet());
        Set<Expression> expressionSet = Utils.set(expressions);


        for (Expression expression : expressions) {
            if (expression instanceof Criterion) {
                Criterion criteria = (Criterion) expression;
                if (Utils.isIn(criteria.getOperator(), indexedOperators)) {
                    List list = doFilterWithIndex(searchableCollection, lookupIndexMap, searchIndexMap, (Criterion) expression, fields);
                    if (list.size() > 0) {
                        listOfSets.add(new HashSet(list));
                    } else {
                        return Collections.EMPTY_LIST;
                    }
                    expressionSet.remove(criteria);
                    if (list.size() < 10) {
                        break;
                    }

                }

            }
        }

        HashSet mainSet = listOfSets.get(0);


        for (HashSet otherSet : listOfSets) {
            mainSet.retainAll(otherSet);
        }


        List results = new ArrayList(mainSet);

        Set<Expression> visitedExpressions = new HashSet<>();
        for (Expression expression : expressionSet) {
            if (expression instanceof Criterion) {
                Criterion criteria = (Criterion) expression;
                results = doFilterUsingLinear(results, criteria, fields);
                if (results.size() == 0) {
                    return Collections.EMPTY_LIST;
                }
                visitedExpressions.add(criteria);
            }
        }

        expressionSet.removeAll(visitedExpressions);


        listOfSets = new ArrayList(new HashSet());
        listOfSets.add(new HashSet(results));

        for (Expression expression : expressionSet) {

            if (expression instanceof Group) {
                List list = doFilterGroup(searchableCollection, lookupIndexMap, searchIndexMap, (Group) expression, fields);
                if (list.size() > 0) {
                    listOfSets.add(new HashSet(list));
                }
            }
        }

        for (HashSet otherSet : listOfSets) {
            mainSet.retainAll(otherSet);
        }


        results = new ArrayList(mainSet);

        return results;

    }

    private List doFilterWithIndex(SearchableCollection searchableCollection, Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                                   Criterion criterion, Map<String, FieldAccess> fields) {
        String name = criterion.getName();
        Object value = criterion.getValue();
        Operator operator = criterion.getOperator();
        SearchIndex searchIndex = searchIndexMap.get(name);
        LookupIndex lookupIndex = lookupIndexMap.get(name);

        if (lookupIndex != null && operator == Operator.EQUAL) {
            return lookupIndex.getAll(value);
        }

        if (searchIndex == null) {
            return this.doFilterUsingLinear(searchableCollection.all(), criterion, fields);
        }

        switch (operator) {
            case EQUAL:
                return processResultsFromIndex(searchIndex, searchIndex.findEquals(value));

            case STARTS_WITH:
                return searchIndex.findStartsWith(value);

            case GREATER_THAN:
                return searchIndex.findGreaterThan(value);

            case GREATER_THAN_EQUAL:
                return searchIndex.findGreaterThanEqual(value);

            case LESS_THAN:
                return searchIndex.findLessThan(value);

            case LESS_THAN_EQUAL:
                return searchIndex.findLessThanEqual(value);

            case BETWEEN:
                return searchIndex.findBetween(value, criterion.getValues()[1]);

        }

        return Collections.EMPTY_LIST;
    }

    private List processResultsFromIndex(SearchIndex searchIndex, List results) {
        if (searchIndex.isPrimaryKeyOnly()) {
            //TODO iterate through list and lookup items from keys, and put those in the actual results
            return null;
        } else {
            return results;
        }
    }

    private List doFilterUsingLinear(List list, Criterion criterion, Map<String, FieldAccess> fields) {
        String name = criterion.getName();
        Object value = criterion.getValue();
        Operator operator = criterion.getOperator();
        FieldAccess field = fields.get(name);

        if (!field.getType().isPrimitive()) {
            return linearFilter(list, criterion, name, operator, value, fields);
        } else {
            return linearFilterWithPrimitive(list, criterion, name, operator, value, fields, field);
        }
    }

    private List linearFilterWithPrimitive(List list, Criterion criterion, String name, Operator operator, Object value, Map<String, FieldAccess> fields, FieldAccess field) {
        Class<?> type = field.getType();
        if (type == int.class) {
            return linearFilterInt(list, criterion, name, operator, value, fields, field);
        } else if (type == float.class) {
            return linearFilterFloat(list, criterion, name, operator, value, fields, field);
        } else if (type == short.class) {
            return linearFilterShort(list, criterion, name, operator, value, fields, field);
        } else if (type == double.class) {
            return linearFilterDouble(list, criterion, name, operator, value, fields, field);
        } else if (type == boolean.class) {
            return linearFilterBoolean(list, criterion, name, operator, value, fields, field);
        } else if (type == byte.class) {
            return linearFilterByte(list, criterion, name, operator, value, fields, field);
        } else if (type == char.class) {
            return linearFilterChar(list, criterion, name, operator, value, fields, field);
        }

        return linearFilter(list, criterion, name, operator, value, fields);


    }

    private List linearFilterInt(List list, Criterion criterion, String name, Operator operator, Object ovalue, Map<String, FieldAccess> fields, FieldAccess field) {
        int value = Types.toInt(ovalue);
        switch (operator) {
            case EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case STARTS_WITH:
                return LinearSearch.findStartsWith(list, name, value);

            case ENDS_WITH:
                return LinearSearch.findEndsWith(list, name, value);

            case CONTAINS:
                return LinearSearch.findContains(list, name, value);

            case GREATER_THAN:
                return LinearSearch.findGreaterThan(list, name, value);

            case GREATER_THAN_EQUAL:
                return LinearSearch.findGreaterThanEqual(list, name, value);

            case LESS_THAN:
                return LinearSearch.findLessThan(list, name, value);

            case BETWEEN:
                int value2 = Types.toInt(criterion.getValues()[1]);

                return LinearSearch.findBetween(list, name, value, value2);

            case LESS_THAN_EQUAL:
                return LinearSearch.findLessThanEqual(list, name, value);

            case NOT_EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case NOT_IN:
                return LinearSearch.findNotIn(list, name, criterion.getValues());

            case IN:
                return LinearSearch.findIn(list, name, criterion.getValues());

        }

        return Collections.EMPTY_LIST;

    }

    private List linearFilterDouble(List list, Criterion criterion, String name, Operator operator, Object ovalue, Map<String, FieldAccess> fields, FieldAccess field) {
        double value = Types.toDouble(ovalue);
        switch (operator) {
            case EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case STARTS_WITH:
                return LinearSearch.findStartsWith(list, name, value);

            case ENDS_WITH:
                return LinearSearch.findEndsWith(list, name, value);

            case CONTAINS:
                return LinearSearch.findContains(list, name, value);

            case GREATER_THAN:
                return LinearSearch.findGreaterThan(list, name, value);

            case GREATER_THAN_EQUAL:
                return LinearSearch.findGreaterThanEqual(list, name, value);

            case LESS_THAN:
                return LinearSearch.findLessThan(list, name, value);

            case BETWEEN:
                double value2 = Types.toDouble(criterion.getValues()[1]);

                return LinearSearch.findBetween(list, name, value, value2);

            case LESS_THAN_EQUAL:
                return LinearSearch.findLessThanEqual(list, name, value);

            case NOT_EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case NOT_IN:
                return LinearSearch.findNotIn(list, name, criterion.getValues());

            case IN:
                return LinearSearch.findIn(list, name, criterion.getValues());

        }

        return Collections.EMPTY_LIST;

    }

    private List linearFilterBoolean(List list, Criterion criterion, String name, Operator operator, Object ovalue, Map<String, FieldAccess> fields, FieldAccess field) {
        boolean value = Types.toBoolean(ovalue);
        switch (operator) {
            case EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case STARTS_WITH:
                return LinearSearch.findStartsWith(list, name, value);

            case ENDS_WITH:
                return LinearSearch.findEndsWith(list, name, value);

            case CONTAINS:
                return LinearSearch.findContains(list, name, value);

            case GREATER_THAN:
                return LinearSearch.findGreaterThan(list, name, value);

            case GREATER_THAN_EQUAL:
                return LinearSearch.findGreaterThanEqual(list, name, value);

            case LESS_THAN:
                return LinearSearch.findLessThan(list, name, value);

            case BETWEEN:
                boolean value2 = Types.toBoolean(criterion.getValues()[1]);

                return LinearSearch.findBetween(list, name, value, value2);

            case LESS_THAN_EQUAL:
                return LinearSearch.findLessThanEqual(list, name, value);

            case NOT_EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case NOT_IN:
                return LinearSearch.findNotIn(list, name, criterion.getValues());

            case IN:
                return LinearSearch.findIn(list, name, criterion.getValues());

        }

        return Collections.EMPTY_LIST;

    }


    private List linearFilterChar(List list, Criterion criterion, String name, Operator operator, Object ovalue, Map<String, FieldAccess> fields, FieldAccess field) {
        char value = Types.toChar(ovalue);
        switch (operator) {
            case EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case STARTS_WITH:
                return LinearSearch.findStartsWith(list, name, value);

            case ENDS_WITH:
                return LinearSearch.findEndsWith(list, name, value);

            case CONTAINS:
                return LinearSearch.findContains(list, name, value);

            case GREATER_THAN:
                return LinearSearch.findGreaterThan(list, name, value);

            case GREATER_THAN_EQUAL:
                return LinearSearch.findGreaterThanEqual(list, name, value);

            case LESS_THAN:
                return LinearSearch.findLessThan(list, name, value);

            case BETWEEN:
                char value2 = Types.toChar(criterion.getValues()[1]);

                return LinearSearch.findBetween(list, name, value, value2);

            case LESS_THAN_EQUAL:
                return LinearSearch.findLessThanEqual(list, name, value);

            case NOT_EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case NOT_IN:
                return LinearSearch.findNotIn(list, name, criterion.getValues());

            case IN:
                return LinearSearch.findIn(list, name, criterion.getValues());

        }

        return Collections.EMPTY_LIST;

    }

    private List linearFilterShort(List list, Criterion criterion, String name, Operator operator, Object ovalue, Map<String, FieldAccess> fields, FieldAccess field) {
        short value = Types.toShort(ovalue);
        switch (operator) {
            case EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case STARTS_WITH:
                return LinearSearch.findStartsWith(list, name, value);

            case ENDS_WITH:
                return LinearSearch.findEndsWith(list, name, value);

            case CONTAINS:
                return LinearSearch.findContains(list, name, value);

            case GREATER_THAN:
                return LinearSearch.findGreaterThan(list, name, value);

            case GREATER_THAN_EQUAL:
                return LinearSearch.findGreaterThanEqual(list, name, value);

            case LESS_THAN:
                return LinearSearch.findLessThan(list, name, value);

            case BETWEEN:
                short value2 = Types.toShort(criterion.getValues()[1]);

                return LinearSearch.findBetween(list, name, value, value2);

            case LESS_THAN_EQUAL:
                return LinearSearch.findLessThanEqual(list, name, value);

            case NOT_EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case NOT_IN:
                return LinearSearch.findNotIn(list, name, criterion.getValues());

            case IN:
                return LinearSearch.findIn(list, name, criterion.getValues());

        }

        return Collections.EMPTY_LIST;

    }

    private List linearFilterByte(List list, Criterion criterion, String name, Operator operator, Object ovalue, Map<String, FieldAccess> fields, FieldAccess field) {
        byte value = Types.toByte(ovalue);
        switch (operator) {
            case EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case STARTS_WITH:
                return LinearSearch.findStartsWith(list, name, value);

            case ENDS_WITH:
                return LinearSearch.findEndsWith(list, name, value);

            case CONTAINS:
                return LinearSearch.findContains(list, name, value);

            case GREATER_THAN:
                return LinearSearch.findGreaterThan(list, name, value);

            case GREATER_THAN_EQUAL:
                return LinearSearch.findGreaterThanEqual(list, name, value);

            case LESS_THAN:
                return LinearSearch.findLessThan(list, name, value);

            case BETWEEN:
                byte value2 = Types.toByte(criterion.getValues()[1]);

                return LinearSearch.findBetween(list, name, value, value2);

            case LESS_THAN_EQUAL:
                return LinearSearch.findLessThanEqual(list, name, value);

            case NOT_EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case NOT_IN:
                return LinearSearch.findNotIn(list, name, criterion.getValues());

            case IN:
                return LinearSearch.findIn(list, name, criterion.getValues());

        }

        return Collections.EMPTY_LIST;

    }

    private List linearFilterFloat(List list, Criterion criterion, String name, Operator operator, Object ovalue, Map<String, FieldAccess> fields, FieldAccess field) {
        float value = Types.toFloat(ovalue);
        switch (operator) {
            case EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case STARTS_WITH:
                return LinearSearch.findStartsWith(list, name, value);

            case ENDS_WITH:
                return LinearSearch.findEndsWith(list, name, value);

            case CONTAINS:
                return LinearSearch.findContains(list, name, value);

            case GREATER_THAN:
                return LinearSearch.findGreaterThan(list, name, value);

            case GREATER_THAN_EQUAL:
                return LinearSearch.findGreaterThanEqual(list, name, value);

            case LESS_THAN:
                return LinearSearch.findLessThan(list, name, value);

            case BETWEEN:
                float value2 = Types.toFloat(criterion.getValues()[1]);

                return LinearSearch.findBetween(list, name, value, value2);

            case LESS_THAN_EQUAL:
                return LinearSearch.findLessThanEqual(list, name, value);

            case NOT_EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case NOT_IN:
                return LinearSearch.findNotIn(list, name, criterion.getValues());

            case IN:
                return LinearSearch.findIn(list, name, criterion.getValues());

        }

        return Collections.EMPTY_LIST;

    }

    private List linearFilter(List list, Criterion criterion, String name, Operator operator, Object value, Map<String, FieldAccess> fields) {

        switch (operator) {
            case EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case STARTS_WITH:
                return LinearSearch.findStartsWith(list, name, value);

            case ENDS_WITH:
                return LinearSearch.findEndsWith(list, name, value);

            case CONTAINS:
                return LinearSearch.findContains(list, name, value);

            case GREATER_THAN:
                return LinearSearch.findGreaterThan(list, name, (Comparable) value);

            case GREATER_THAN_EQUAL:
                return LinearSearch.findGreaterThanEqual(list, name, (Comparable) value);

            case LESS_THAN:
                return LinearSearch.findLessThan(list, name, (Comparable) value);

            case BETWEEN:
                return LinearSearch.findBetween(list, name, (Comparable) value, (Comparable) criterion.getValues()[1]);

            case LESS_THAN_EQUAL:
                return LinearSearch.findLessThanEqual(list, name, (Comparable) value);

            case NOT_EQUAL:
                return LinearSearch.findEquals(list, name, value);

            case NOT_IN:
                return LinearSearch.findNotIn(list, name, criterion.getValues());

            case IN:
                return LinearSearch.findIn(list, name, criterion.getValues());

        }

        return Collections.EMPTY_LIST;

    }
}