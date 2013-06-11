package org.datarepo.impl;

import org.datarepo.Filter;
import org.datarepo.LookupIndex;
import org.datarepo.SearchableCollection;
import org.datarepo.fields.FieldAccess;
import org.datarepo.query.*;
import org.datarepo.spi.FilterComposer;
import org.datarepo.spi.SearchIndex;
import org.datarepo.utils.Utils;

import java.util.*;

import static org.datarepo.query.Criteria.instanceOf;
import static org.datarepo.query.Criteria.not;
import static org.datarepo.utils.Utils.array;


public class FilterDefault implements Filter, FilterComposer {

    Set<Operator> indexedOperators = Utils.set(Operator.BETWEEN, Operator.EQUAL, Operator.STARTS_WITH,
            Operator.GREATER_THAN, Operator.GREATER_THAN_EQUAL,
            Operator.LESS_THAN, Operator.LESS_THAN_EQUAL);

    private Map<String, FieldAccess> fields;
    private SearchableCollection searchableCollection;
    private Map<String, SearchIndex> searchIndexMap;
    private Map<String, LookupIndex> lookupIndexMap;


    @Override
    public List filter(Expression... expressions) {
        try {
            Expression.fields(this.fields);
            return mainQueryPlan(expressions);
        } finally {
            Expression.clearFields();
        }
    }


    private List mainQueryPlan(Expression[] expressions) {
        List results = null;

        Group group = expressions.length == 1 && expressions[0] instanceof Group
                ? (Group) expressions[0] : Criteria.and(expressions);


        results = doFilterGroup(group);

        return results;
    }


    private List orPlanWithIndex(Criterion criterion) {
        List results = null;


        Operator operator = criterion.getOperator();
        if (operator == Operator.EQUAL && lookupIndexMap.get(criterion.getName()) != null) {
            results = lookupIndexMap.get(criterion.getName()).getAll(criterion.getValue());
        } else if (this.isIndexed(criterion.getName()) && Utils.isIn(operator, indexedOperators)) {
            results = doFilterWithIndex(criterion, fields);
        } else {
            results = Criteria.filter(this.searchableCollection.all(), criterion);
        }


        return results;
    }

    @Override
    public void invalidate() {

    }

    private List doFilterGroup(Group group) {
        if (group.getGrouping() == Grouping.OR) {
            return or(group.getExpressions(), fields);
        } else {
            return new ArrayList(and(group.getExpressions(), fields));

        }
    }

    private List or(Expression[] expressions,
                    Map<String, FieldAccess> fields) {


        HashSet set = new HashSet();
        for (Expression expression : expressions) {
            if (expression instanceof Criterion) {
                List list = orPlanWithIndex((Criterion) expression);
                if (list != null) {
                    set.addAll(list);
                }
            } else if (expression instanceof Group) {
                List list = doFilterGroup((Group) expression);
                set.addAll(list);
            }
        }

        return new ArrayList(set);
    }


    private Set and(Expression[] expressions, Map<String, FieldAccess> fields) {

        Set<Expression> expressionSet = Utils.set(expressions);


        ResultInternal results = applyIndexedFiltersForAnd(expressions, fields, expressionSet);

        //This is a second level optimization.
//        if (results.results.size() > 2_000) {
//            results.results = applyGroupsWithIndexesForAnd(results.results, expressionSet);
//        }


        /* If we did not find an index, then we have to load the whole collection. */
        if (!results.foundIndex) {
            results.results = this.searchableCollection.all();
        }

        results.results = applyLinearSearch(results.results, expressionSet);

        results.results = applyGroups(results.results, expressionSet);

        return new HashSet(results.results);

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

        if (expressionSet.size() == 0) {
            return items;
        }

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

        if (expressionSet.size() == 0) {
            return items;
        }

        Expression[] expressions = array(Expression.class, Criteria.filter(expressionSet, not(instanceOf(Group.class))));

        items = Criteria.filter(items, Criteria.and(expressions));


        for (Expression expression : expressions) {
            expressionSet.remove(expression);
        }

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
            results = Collections.EMPTY_LIST;
        }
        return results;
    }


    private class ResultInternal {
        List results;
        boolean foundIndex;

    }

    private ResultInternal applyIndexedFiltersForAnd(Expression[] expressions, Map<String, FieldAccess> fields, Set<Expression> expressionSet) {
        List<HashSet> listOfSets = new ArrayList();

        ResultInternal results = new ResultInternal();

        for (Expression expression : expressions) {

            /*
             * See if the criteria has an index
             */
            if (expression instanceof Criterion) {
                Criterion criteria = (Criterion) expression;
                Operator operator = criteria.getOperator();
                String name = criteria.getName();
                Object value = criteria.getValue();
                if (operator == Operator.EQUAL && lookupIndexMap.get(name) != null) {


                    results.results = lookupIndexMap.get(name).getAll(value);

                                        /* Keep track that we found the index. */
                    results.foundIndex = true;

                    expressionSet.remove(criteria);

                    if (results.results == null) {
                        results.results = Collections.EMPTY_LIST;
                        return results;
                    }
                    /* if it is less than 20, just linear search the rest. */
                    else if (results.results.size() < 20) {
                        return results;
                    } else if (results.results.size() == 0) {
                        return results;
                    } else if (results.results.size() > 0) {
                        listOfSets.add(new HashSet(results.results));
                    }


                } else if (isIndexed(name) && Utils.isIn(operator, indexedOperators)) {


                    results.results = doFilterWithIndex((Criterion) expression, fields);

                                        /* Keep track that we found the index. */
                    results.foundIndex = true;

                    expressionSet.remove(criteria);

                    if (results.results == null) {
                        results.results = Collections.EMPTY_LIST;
                        return results;
                    }
                    /* if it is less than 20, just linear search the rest. */
                    else if (results.results.size() < 20) {
                        return results;
                    } else if (results.results.size() == 0) {
                        return results;
                    } else if (results.results.size() > 0) {
                        listOfSets.add(new HashSet(results.results));
                    }

                }

            }
        }
        results.results = reduceToResults(listOfSets);
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

        if (!criterion.isInitialized()) {
            Object o = searchIndex.findFirst();
            criterion.init(o);
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
                return searchIndex.findBetween(criterion.getValue(), criterion.getValues()[1]);

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