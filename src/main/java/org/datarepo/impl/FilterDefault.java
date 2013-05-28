package org.datarepo.impl;

import org.datarepo.Filter;
import org.datarepo.LookupIndex;
import org.datarepo.SearchIndex;
import org.datarepo.SearchableCollection;
import org.datarepo.query.*;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.utils.LinearSearch;
import org.datarepo.utils.Utils;

import java.util.*;

public class FilterDefault implements Filter {

    Set<Operator> indexedOperators = Utils.set(Operator.BETWEEN, Operator.EQUAL, Operator.STARTS_WITH,
            Operator.GREATER_THAN, Operator.GREATER_THAN_EQUAL,
            Operator.LESS_THAN, Operator.LESS_THAN_EQUAL);

    @Override
    public List filter(SearchableCollection searchableCollection, Map<String, FieldAccess> fields, Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                       Expression... expressions) {

//        Set<SearchIndex> indexSet = new TreeSet<SearchIndex>((SearchIndex index, SearchIndex index2) -> {
//            return index.size() > index.size() ? 1 : -1;
//        }
//        );
//
//        for (SearchIndex si : indexSet) {
//            Utils.print(si.size());
//        }


        if (expressions.length == 1 && expressions[0] instanceof Criterion) {

            Criterion criterion = (Criterion) expressions[0];


            if (Utils.isIn(criterion.getOperator(), indexedOperators)) {
                return doFilterWithIndex(lookupIndexMap, searchIndexMap, criterion);
            } else {
                //TODO fix this... it may work, but who is going to remember.
                return doFilter(new ArrayList(lookupIndexMap.values().iterator().next().all()), criterion);
            }
        }

        return or(lookupIndexMap, searchIndexMap, expressions);

    }

    private List doFilter(Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                          Group group) {
        if (group.getGrouping() == Grouping.OR) {
            return or(lookupIndexMap, searchIndexMap, group.getExpressions());
        } else {
            return and(lookupIndexMap, searchIndexMap, group.getExpressions());

        }
    }

    private List or(Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                    Expression... expressions) {

        HashSet set = new HashSet();
        for (Expression expression : expressions) {
            if (expression instanceof Criterion) {
                List list = doFilterWithIndex(lookupIndexMap, searchIndexMap, (Criterion) expression);
                set.addAll(list);
            }
            if (expression instanceof Group) {
                List list = doFilter(lookupIndexMap, searchIndexMap, (Group) expression);
                set.addAll(list);
            }
        }
        return new ArrayList(set);


    }


    private List and(Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                     Expression... expressions) {

        List<HashSet> listOfSets = new ArrayList(new HashSet());
        Set<Expression> expressionSet = Utils.set(expressions);


        for (Expression expression : expressions) {
            if (expression instanceof Criterion) {
                Criterion criteria = (Criterion) expression;
                if (Utils.isIn(criteria.getOperator(), indexedOperators)) {
                    List list = doFilterWithIndex(lookupIndexMap, searchIndexMap, (Criterion) expression);
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

        Set <Expression> visitedExpressions = new HashSet<>();
        for (Expression expression : expressionSet) {
            if (expression instanceof Criterion) {
                Criterion criteria = (Criterion) expression;
                results = doFilter(results, criteria);
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
                List list = doFilter(lookupIndexMap, searchIndexMap, (Group) expression);
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

    private List doFilterWithIndex(Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                                   Criterion criterion) {
        String name = criterion.getName();
        Object value = criterion.getValue();
        Operator operator = criterion.getOperator();
        SearchIndex searchIndex = searchIndexMap.get(name);
        LookupIndex lookupIndex = lookupIndexMap.get(name);

        if (lookupIndex != null && operator == Operator.EQUAL) {
            return lookupIndex.getAll(value);
        }

        switch (operator) {
            case EQUAL:
                return searchIndex.findEquals(value);

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

    private List doFilter(List list, Criterion criterion) {
        String name = criterion.getName();
        Object value = criterion.getValue();
        Operator operator = criterion.getOperator();
        //Need to optimize for primitives, but not now TODO
        switch (operator) {
            case EQUAL:
                return LinearSearch.findEquals(list, name, value);

            //TODO NEED STARTS WITH
            //case STARTS_WITH:
            //    return LinearSearch.findEquals(list, name, value);

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