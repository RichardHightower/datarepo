package org.datarepo.impl;

import org.datarepo.Filter;
import org.datarepo.LookupIndex;
import org.datarepo.SearchIndex;
import org.datarepo.SearchableCollection;
import org.datarepo.query.*;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.spi.FilterComposer;
import org.datarepo.utils.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class FilterDefault implements Filter, FilterComposer {

    Set<Operator> indexedOperators = Utils.set(Operator.BETWEEN, Operator.EQUAL, Operator.STARTS_WITH,
            Operator.GREATER_THAN, Operator.GREATER_THAN_EQUAL,
            Operator.LESS_THAN, Operator.LESS_THAN_EQUAL);

    Map<Expression, List> queryCache = new ConcurrentHashMap<>();
    int flushCount = 0;
    boolean cache = true;
    private Map<String, FieldAccess> fields;
    private SearchableCollection searchableCollection;
    private Map<String, SearchIndex> searchIndexMap;
    private Map<String, LookupIndex> lookupIndexMap;


    @Override
    public List filter(Expression... expressions) {
        try {
            Expression.fields(this.fields);

            checkCache();

            if (expressions.length == 1 && expressions[0] instanceof Criterion) {

                return simpleQueryPlanOneCriterion((Criterion) expressions[0]);
            }

            return mainQueryPlan(expressions);
        } finally {

            Expression.clearFields();

        }
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


    private List mainQueryPlan(Expression[] expressions) {
        List results = null;

        Group group = Criteria.and(expressions);

        if (cache) {
            results = queryCache.get(group);

            if (results != null) {
                return results;
            }
        }

        results = doFilterGroup(group);

        if (cache) {
            flushCount++;
            queryCache.put(group, results == null ? Collections.EMPTY_LIST : results);
        }
        return results;
    }

    private List simpleQueryPlanOneCriterion(Criterion criterion) {
        List results = null;

        if (cache) {
            results = queryCache.get(criterion);

            if (results != null) {
                return results;
            }
        }


        Operator operator = criterion.getOperator();
        if (operator == Operator.EQUAL && lookupIndexMap.containsKey(criterion.getName())) {
            results = lookupIndexMap.get(criterion.getName()).getAll(criterion.getValue());
        } else if (this.isIndexed(criterion.getName()) && Utils.isIn(operator, indexedOperators)) {
            results = doFilterWithIndex(criterion, fields);
        } else {
            results = Criteria.filter(searchableCollection.all(), criterion);
        }

        if (cache) {
            flushCount++;
            queryCache.put(criterion, results == null ? Collections.EMPTY_LIST : results);
        }
        return results;
    }

    private List orPlanWithIndex(Criterion criterion) {
        List results = null;

        if (cache) {
            results = queryCache.get(criterion);

            if (results != null) {
                return results;
            }
        }


        Operator operator = criterion.getOperator();
        if (operator == Operator.EQUAL && lookupIndexMap.get(criterion.getName()) != null) {
            results = lookupIndexMap.get(criterion.getName()).getAll(criterion.getValue());
        } else if (this.isIndexed(criterion.getName()) && Utils.isIn(operator, indexedOperators)) {
            results = doFilterWithIndex(criterion, fields);
        } else {
            return null;
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

    private List doFilterGroup(Group group) {
        if (group.getGrouping() == Grouping.OR) {
            return or(group.getExpressions(), fields);
        } else {
            return and(group.getExpressions(), fields);

        }
    }

    private List or(Expression[] expressions,
                    Map<String, FieldAccess> fields) {

        Set<Expression> expressionSet = Utils.set(expressions);


        HashSet set = new HashSet();
        for (Expression expression : expressions) {
            if (expression instanceof Criterion) {
                List list = orPlanWithIndex((Criterion) expression);
                if (list != null) {
                    set.addAll(list);
                    expressionSet.remove(expression);
                }
            }
            if (expression instanceof Group) {
                List list = doFilterGroup((Group) expression);
                set.addAll(list);
                expressionSet.remove(expression);
            }
        }

        Criterion[] criteria = (Criterion[]) expressionSet.toArray(new Criterion[set.size()]);
        return Criteria.filter(new ArrayList(set), Criteria.or(criteria));
    }


    private List and(Expression[] expressions, Map<String, FieldAccess> fields) {

        Set<Expression> expressionSet = Utils.set(expressions);


        List results = applyIndexedFiltersForAnd(expressions, fields, expressionSet);

        if (results.size() > 2_000) {
            results = applyGroupsWithIndexesForAnd(results, expressionSet);
        }


        results = applyLinearSearch(results, expressionSet);

        results = applyGroups(results, expressionSet);

        return results;

    }

    private List applyGroupsWithIndexesForAnd(List items, Set<Expression> expressionSet) {
        List<HashSet> listOfSets = new ArrayList();
        listOfSets.add(new HashSet(items));

        List<Expression> expressionsWeEvaluated = new ArrayList<>();

        outer:
        for (Expression expression : expressionSet) {

            if (expression instanceof Group) {
                Group group = (Group) expression;
                for (Expression innerExpression : group.getExpressions()) {
                    //Don't allow non-index Criterion to avoid too many scans
                    if (innerExpression instanceof Criterion) {
                        Criterion c = (Criterion) innerExpression;
                        if (!this.isIndexed(c.getName())) {
                            continue outer;
                        }
                    }
                    //Don't allow any ors to avoid long scans, at this point
                    //This is simple for now, it does not recusively look for indexes, future one should.
                    else if (innerExpression instanceof Group) {
                        continue;
                    }
                }


                /*
                At this point, this group should be indexed only
                 */
                List list = doFilterGroup((Group) expression);
                if (list.size() > 0) {
                    listOfSets.add(new HashSet(list));
                    expressionsWeEvaluated.add(expression);
                }
            }
        }
        List results = reduceToResults(listOfSets);
        expressionSet.removeAll(expressionsWeEvaluated);

        return results;
    }


    private List applyGroups(List items, Set<Expression> expressionSet) {
        List<HashSet> listOfSets = new ArrayList();
        listOfSets.add(new HashSet(items));

        for (Expression expression : expressionSet) {

            if (expression instanceof Group) {
                List list = doFilterGroup((Group) expression);
                if (list.size() > 0) {
                    listOfSets.add(new HashSet(list));
                }
            }
        }
        List results = reduceToResults(listOfSets);
        return results;
    }


    private List applyLinearSearch(List items, Set<Expression> expressionSet) {
        HashSet set = new HashSet(expressionSet);
        set.removeIf((e) -> {
            return e instanceof Group;
        });

        Criterion[] criteria = (Criterion[]) set.toArray(new Criterion[set.size()]);
        items = Criteria.filter(items, Criteria.and(criteria));

        return items;
    }

    private List reduceToResults(List<HashSet> listOfSets) {
        List results = null;
        HashSet mainSet = null;

        if (listOfSets.size() != 0) {
            mainSet = listOfSets.get(0);


            for (HashSet otherSet : listOfSets) {
                mainSet.retainAll(otherSet);
            }


            results = new ArrayList(mainSet);
        } else {
            results = new ArrayList(this.searchableCollection.all());
        }
        return results;
    }

    private List applyIndexedFiltersForAnd(Expression[] expressions, Map<String, FieldAccess> fields, Set<Expression> expressionSet) {
        List<HashSet> listOfSets = new ArrayList();

        for (Expression expression : expressions) {
            if (expression instanceof Criterion) {
                Criterion criteria = (Criterion) expression;
                Operator operator = criteria.getOperator();
                String name = criteria.getName();
                Object value = criteria.getValue();
                if (operator == Operator.EQUAL && lookupIndexMap.get(name) != null) {
                    List list = lookupIndexMap.get(name).getAll(value);
                    if (list.size() > 0) {
                        listOfSets.add(new HashSet(list));
                    }
                    expressionSet.remove(criteria);
                    if (list.size() < 20) {
                        break;
                    }
                } else if (isIndexed(name) && Utils.isIn(operator, indexedOperators)) {
                    List list = doFilterWithIndex((Criterion) expression, fields);
                    if (list.size() > 0) {
                        listOfSets.add(new HashSet(list));
                    }
                    expressionSet.remove(criteria);
                    if (list.size() < 20) {
                        break;
                    }

                }

            }
        }
        List results = reduceToResults(listOfSets);
        return results;
    }

    private boolean isIndexed(String name) {
        return searchIndexMap.containsKey(name);
    }

    private List doFilterWithIndex(Criterion criterion, Map<String, FieldAccess> fields) {
        String name = criterion.getName();
        Object value = criterion.getValue();
        Operator operator = criterion.getOperator();
        SearchIndex searchIndex = searchIndexMap.get(name);
        LookupIndex lookupIndex = lookupIndexMap.get(name);

        if (lookupIndex != null && operator == Operator.EQUAL) {
            return lookupIndex.getAll(value);
        }

        if (searchIndex == null) {
            Utils.complain("Trying to do a indexed search without an index!");
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


    @Override
    public void setSearchableCollection(SearchableCollection searchableCollection) {
        this.searchableCollection = searchableCollection;
    }

    @Override
    public void setFields(Map<String, FieldAccess> fields) {
        this.fields = fields;
    }

    @Override
    public void setUseCache(boolean cache) {
        this.cache = cache;
    }

    @Override
    public void setSearchIndexMap(Map<String, SearchIndex> searchIndexMap) {
        this.searchIndexMap = searchIndexMap;
    }

    @Override
    public void setLookupIndexMap(Map<String, LookupIndex> lookupIndexMap) {
        this.lookupIndexMap = lookupIndexMap;
    }

    @Override
    public void init() {

    }
}