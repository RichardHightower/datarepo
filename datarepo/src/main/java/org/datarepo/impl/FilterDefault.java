package org.datarepo.impl;

import org.datarepo.Filter;
import org.datarepo.LookupIndex;
import org.datarepo.ResultSet;
import org.datarepo.SearchableCollection;
import org.datarepo.fields.FieldAccess;
import org.datarepo.query.*;
import org.datarepo.spi.FilterComposer;
import org.datarepo.spi.ResultSetInternal;
import org.datarepo.spi.SearchIndex;
import org.datarepo.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.datarepo.query.QueryFactory.instanceOf;
import static org.datarepo.query.QueryFactory.not;
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
    public ResultSet filter(Query... expressions) {
        try {
            Query.fields(this.fields);
            return mainQueryPlan(expressions);
        } finally {
            Query.clearFields();
        }
    }


    private ResultSet mainQueryPlan(Query[] expressions) {

        ResultSetInternal results = new ResultSetImpl(this.fields);

        Group group = expressions.length == 1 && expressions[0] instanceof Group
                ? (Group) expressions[0] : QueryFactory.and(expressions);


        doFilterGroup(group, results);

        return results;
    }


    private void orPlanWithIndex(Criterion criterion, ResultSetInternal results) {


        Operator operator = criterion.getOperator();
        if (operator == Operator.EQUAL && lookupIndexMap.get(criterion.getName()) != null) {
            doFilterWithIndex(criterion, fields, results);
        } else if (this.isIndexed(criterion.getName()) && Utils.isIn(operator, indexedOperators)) {
            doFilterWithIndex(criterion, fields, results);
        } else {
            List list = QueryFactory.filter(this.searchableCollection.all(), criterion);
            results.addResults(list);
        }

    }

    @Override
    public void invalidate() {

    }

    private void doFilterGroup(Group group, ResultSetInternal results) {
        if (group.getGrouping() == Grouping.OR) {
            or(group.getExpressions(), fields, results);
        } else {
            ResultSetInternal resultsForAnd = new ResultSetImpl(fields);
            and(group.getExpressions(), fields, resultsForAnd);
            results.addResults(resultsForAnd.asList());
        }
    }

    private void or(Query[] expressions,
                    Map<String, FieldAccess> fields, ResultSetInternal results) {


        for (Query expression : expressions) {
            if (expression instanceof Criterion) {
                orPlanWithIndex((Criterion) expression, results);
            } else if (expression instanceof Group) {
                doFilterGroup((Group) expression, results);
            }
        }
    }


    private void and(Query[] expressions, Map<String, FieldAccess> fields, ResultSetInternal resultSet) {

        Set<Query> expressionSet = Utils.set(expressions);


        boolean foundIndex = applyIndexedFiltersForAnd(expressions, fields, expressionSet, resultSet);
        applyLinearSearch(expressionSet, resultSet, foundIndex);
        applyGroups(expressionSet, resultSet);


    }


    private boolean applyIndexedFiltersForAnd(Query[] expressions, Map<String, FieldAccess> fields, Set<Query> expressionSet, ResultSetInternal resultSet) {
        Criterion criteria = null;
        boolean foundIndex = false;

        if (expressions.length == 1 && expressions[0] instanceof Criterion) {
            criteria = (Criterion) expressions[0];
            foundIndex = doFilterWithIndex(criteria, fields, resultSet);
            if (foundIndex) {
                expressionSet.remove(criteria);
            }
            return foundIndex;
        }


        for (Query expression : expressions) {
            /*
             * See if the criteria has an index
             */
            if (expression instanceof Criterion) {
                criteria = (Criterion) expression;

                foundIndex = doFilterWithIndex(criteria, fields, resultSet);
                if (foundIndex) {
                    expressionSet.remove(criteria);
                }
                /* if it is less than 20, just linear search the rest. */
                if (resultSet.lastSize() < 20) {
                    resultSet.andResults(); //consolidate now
                    return foundIndex;
                } else if (resultSet.lastSize() > 0) {
                    //No op
                }

            }
        }
        if (foundIndex) {
            resultSet.andResults();
        }
        return foundIndex;
    }


//    private List applyGroupsWithIndexesForAnd(List items, Set<Query> expressionSet) {
//
//        List<HashSet> listOfSets = new ArrayList();
//        listOfSets.add(new HashSet(items));
//
//        List<Query> expressionsWeEvaluated = new ArrayList<>();
//
//        outer:
//        for (Query expression : expressionSet) {
//
//            if (expression instanceof Group) {
//                Group group = (Group) expression;
//                for (Query innerExpression : group.getExpressions()) {
//                    //Don't allow non-index Criterion to avoid too many scans
//                    if (innerExpression instanceof Criterion) {
//                        Criterion c = (Criterion) innerExpression;
//                        if (!this.isIndexed(c.getName())) {
//                            continue outer;
//                        }
//                    }
//                    //Don't allow any ors to avoid long scans, at this point
//                    //This is simple for now, it does not recusively look for indexes, future one should.
//                    else if (innerExpression instanceof Group) {
//                        continue;
//                    }
//                }
//
//
//                /*
//                At this point, this group should be indexed only
//                 */
//                List list = doFilterGroup((Group) expression);
//                if (list.size() > 0) {
//                    listOfSets.add(new HashSet(list));
//                    expressionsWeEvaluated.add(expression);
//                }
//            }
//        }
//        List results = reduceToResults(listOfSets);
//        expressionSet.removeAll(expressionsWeEvaluated);
//
//        return results;
//    }


    private void applyGroups(Set<Query> expressionSet, ResultSetInternal resultSet) {

        if (expressionSet.size() == 0) {
            return;
        }


        for (Query expression : expressionSet) {

            if (expression instanceof Group) {
                doFilterGroup((Group) expression, resultSet);
            }
        }
    }


    private void applyLinearSearch(Set<Query> expressionSet, ResultSetInternal resultSet, boolean foundIndex) {

        if (expressionSet.size() == 0) {
            return;
        }

        Query[] expressions = array(Query.class, QueryFactory.filter(expressionSet, not(instanceOf(Group.class))));

        if (foundIndex) {
            resultSet.filterAndPrune(QueryFactory.and(expressions));
        } else {
            resultSet.addResults(
                    QueryFactory.filter(searchableCollection.all(),
                            QueryFactory.and(expressions))
            );
        }
        for (Query expression : expressions) {
            expressionSet.remove(expression);
        }

    }


    private boolean isIndexed(String name) {
        return searchIndexMap.containsKey(name);
    }

    private boolean doFilterWithIndex(Criterion criterion, Map<String, FieldAccess> fields, ResultSetInternal resultSet) {
        String name = criterion.getName();
        Object value = criterion.getValue();
        Operator operator = criterion.getOperator();
        SearchIndex searchIndex = searchIndexMap.get(name);
        LookupIndex lookupIndex = lookupIndexMap.get(name);
        List resultList = null;
        boolean foundIndex = false;

        if (lookupIndex != null && operator == Operator.EQUAL) {
            foundIndex = true;
            resultList = lookupIndex.getAll(value);
            if (resultList != null) {
                resultSet.addResults(resultList);
                return foundIndex;
            } else {
                resultSet.addResults(Collections.EMPTY_LIST);
                return foundIndex;
            }
        }

        if (searchIndex == null) {
            return false;
        }

        foundIndex = true;

        if (!criterion.isInitialized()) {
            criterion.init(this.fields);
        }

        switch (operator) {
            case EQUAL:
                resultList = processResultsFromIndex(searchIndex, searchIndex.findEquals(value));
                break;
            case STARTS_WITH:
                resultList = searchIndex.findStartsWith(value);
                break;

            case GREATER_THAN:
                resultList = searchIndex.findGreaterThan(value);
                break;

            case GREATER_THAN_EQUAL:
                resultList = searchIndex.findGreaterThanEqual(value);
                break;

            case LESS_THAN:
                resultList = searchIndex.findLessThan(value);
                break;

            case LESS_THAN_EQUAL:
                resultList = searchIndex.findLessThanEqual(value);
                break;

            case BETWEEN:
                resultList = searchIndex.findBetween(criterion.getValue(), criterion.getValues()[1]);
                break;
        }

        if (resultList != null) {
            resultSet.addResults(resultList);
            return foundIndex;
        } else {
            return foundIndex;
        }
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