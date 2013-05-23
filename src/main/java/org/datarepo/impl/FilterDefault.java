package org.datarepo.impl;

import org.datarepo.Filter;
import org.datarepo.LookupIndex;
import org.datarepo.SearchIndex;
import org.datarepo.criteria.*;

import java.util.*;

public class FilterDefault implements Filter {
    @Override
    public List filter(Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                       Expression... expressions) {


        if (expressions.length == 1 && expressions[0] instanceof  Criterion) {
                return doFilter(lookupIndexMap, searchIndexMap, (Criterion) expressions[0]);
        }

        return or(lookupIndexMap, searchIndexMap, expressions);

    }

    private List doFilter(Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                          Group group) {
        if (group.getGrouping() == Grouping.OR) {
            return or(lookupIndexMap, searchIndexMap, group.getExpressions());
        }  else {
            return and(lookupIndexMap, searchIndexMap, group.getExpressions());

        }
    }

    private List or(Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                    Expression... expressions) {

        HashSet set = new HashSet();
        for (Expression expression : expressions) {
            if (expression instanceof Criterion) {
                List list = doFilter(lookupIndexMap, searchIndexMap, (Criterion) expression);
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

        List <HashSet> listOfSets = new ArrayList(new HashSet());
        for (Expression expression : expressions) {
            if (expression instanceof Criterion) {
                List list = doFilter(lookupIndexMap, searchIndexMap, (Criterion) expression);
                if (list.size() > 0) {
                    listOfSets.add(new HashSet(list));
                }
            }
            if (expression instanceof Group) {
                List list = doFilter(lookupIndexMap, searchIndexMap, (Group) expression);
                if (list.size() > 0) {
                    listOfSets.add(new HashSet(list));
                }
          }
        }


        HashSet mainSet = listOfSets.get(1);


        for (HashSet otherSet : listOfSets) {
                mainSet.retainAll(otherSet);
        }


        return  new ArrayList(mainSet);

    }

    private List doFilter(Map<String, LookupIndex> lookupIndexMap, Map<String, SearchIndex> searchIndexMap,
                          Criterion criterion) {
        String name = criterion.getName();
        Object value = criterion.getValue();
        Operator operator = criterion.getOperator();
        SearchIndex searchIndex = searchIndexMap.get(name);
        LookupIndex lookupIndex = lookupIndexMap.get(name);

        if (lookupIndex != null && operator == Operator.EQUAL) {
            return Collections.singletonList(lookupIndex.get(value));
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

        return null;
    }
}