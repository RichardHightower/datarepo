package org.datarepo.impl.indexes;

import org.datarepo.utils.Utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.datarepo.utils.Utils.*;

/**
 * Internal class
 *
 * @param <T>
 */
class MultiValue<T> {
    T[] array = (T[]) new Object[10];
    List<T> values;
    int index = 0;

    MultiValue() {

    }

    MultiValue(T item) {
        array[index] = item;
        index++;
    }

    void add(T item) {
        if (index < 10) {
            array[index] = item;
            index++;
        } else {
            createList();
            values.add(item);
        }
    }

    private final void createList() {
        if (values == null) {
            values = new ArrayList(index);
            for (int i = 0; i < index; i++) {
                values.add(array[i]);
            }
            array = null;
            index = Integer.MAX_VALUE;
        }
    }

    void remove(T item) {
        createList();
        values.remove(item);
    }

    T getValue() {

        try {
            if (index < 10) {
                return array[0];
            } else {
                return values.get(0);
            }

        } catch (Exception ex) {
            Utils.handleUnexpectedException(lines(
                    "PROBLEM GETTING A VALUE FROM MULTI-VALUE",
                    sprintf("index %s, array %s, values %s", index, array, values),
                    array != null ? sprintf("array[0] %s, array[9] %s", array[0], array[9]) : "array null",

                    "PROBLEM GETTING A VALUE FROM MULTI-VALUE"
            ), ex);
            return null;
        }

    }

    final List<T> getValues() {

        if (index < 10) {
            return new AbstractList<T>() {
                @Override
                public T get(int i) {
                    return array[i];
                }

                @Override
                public int size() {
                    return index;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>() {
                        int size = index;
                        int i = 0;

                        @Override
                        public boolean hasNext() {
                            return i < index;
                        }

                        @Override
                        public T next() {
                            T v = array[i];
                            i++;
                            return v;
                        }
                    };
                }
            };
        } else {
            return values;
        }

    }


    int size() {
        if (index < 10) {
            return index;
        } else {
            return values.size();
        }
    }

    void addTo(List<T> results) {
        if (index < 10 || values == null) {
            for (int i = 0; i < index; i++) {
                results.add(array[i]);
            }
        } else {

            if (values == null) {
                print("index", index);
                print("array", array);
                print("values", values);
                print("results", array);

            }
            results.addAll(values);
        }

    }


}
