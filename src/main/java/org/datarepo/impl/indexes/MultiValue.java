package org.datarepo.impl.indexes;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal class
 *
 * @param <T>
 */
class MultiValue<T> {
    List<T> values = new ArrayList<>(10);

    public static <T> MultiValue<T> add(MultiValue<T> org, T newItem) {
        if (org == null) {
            return new MultiValue<T>(newItem);
        } else {
            org.add(newItem);
        }
        return org;
    }

    public static <T> MultiValue<T> remove(MultiValue<T> org, T removeItem) {
        if (org == null) {
            return null;
        }

        org.remove(removeItem);

        return org.size() == 0 ? null : org;
    }

    private MultiValue() {

    }

    private MultiValue(T item) {
        values.add(item);

    }

    private void add(T item) {

        values.add(item);
    }

    private void remove(T item) {
        values.remove(item);
    }

    T getValue() {

        return (values.size() > 0) ? values.get(0) : null;
    }

    final List<T> getValues() {
        return values;
    }


    int size() {
        return values.size();
    }

    void addTo(List<T> results) {
        results.addAll(values);
    }


}
