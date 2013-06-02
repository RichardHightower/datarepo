package org.datarepo.utils;

import org.datarepo.query.Criterion;
import org.datarepo.reflection.FieldAccess;

import java.util.*;

import static org.datarepo.query.Criteria.andTest;
import static org.datarepo.query.Criteria.orTest;

public class LinearSearchWithFields {


    public static <T> List<T> findWithFiltersAnd(Collection<T> items, Map<String, FieldAccess> fields, Criterion... filters) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        List<T> results = new ArrayList<>();
        for (T item : items) {
            if (andTest(item, filters)) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findWithFiltersOr(Collection<T> items, Map<String, FieldAccess> fields, Criterion... filters) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        List<T> results = new ArrayList<>();
        for (T item : items) {
            if (orTest(item, filters)) {
                results.add(item);
            }

        }
        return results;
    }

}
