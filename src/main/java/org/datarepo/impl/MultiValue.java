package org.datarepo.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal class
 *
 * @param <T>
 */
class MultiValue<T> {
    T value;
    List<T> values;

    MultiValue() {

    }

    MultiValue(T value) {
        this.value = value;
    }

    void add(T item) {
        if (values == null) {
            values = new ArrayList();
        }
        if (value != null) {
            values.add(value);
            value = null;
        }
        values.add(item);
    }

    void remove(T item) {
        if (value != null) {
            value = null;
        } else {
            if (values != null) {
                values.remove(item);
            }
        }
    }

    T getValue() {
        if (value == null && values != null) {
            return values.get(0);
        } else {
            return value;
        }
    }

    List<T> getValues() {
        if (values != null) {
            return new ArrayList(values);
        } else {
            List<T> list = new ArrayList<T>();
            list.add(value);
            return list;
        }
    }

    int size() {
        if (value != null) {
            return 1;
        } else {
            return values == null ? 0 : values.size();
        }
    }

    void addTo(List<T> results) {
        if (value != null) {
            results.add(value);
        } else {
            results.addAll(values);
        }

    }


}
