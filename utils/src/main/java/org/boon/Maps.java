package org.boon;


import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Maps {

    /** Universal methods. */
    @Universal
    public static int len(Map<?, ?> map) {
        return map.size();
    }

    @Universal
    public static <K, V> boolean in(K key, Map<K, V> map) {
        return map.containsKey(key);
    }

    @Universal
    public static <K, V> void add(Map<K, V> map, Entry<K, V> entry) {
        map.put(entry.key(), entry.value());
    }

    @Universal
    public static <K, V> V idx(Map<K, V> map, K k) {
        return map.get(k);
    }

    @Universal
    public static <K, V> void idx(Map<K, V> map, K k, V v) {
        map.put(k, v);
    }

    @Universal
    public static <K, V> SortedMap<K, V> copy(SortedMap<K, V> map) {
        if (map instanceof  TreeMap) {
            return new TreeMap<>(map);
        }else if (map instanceof  ConcurrentSkipListMap) {
            return new ConcurrentSkipListMap<>(map);
        } else {
            return new TreeMap<>(map);
        }
    }

    @Universal
    public static <K, V> Map<K, V> copy(Map<K, V> map) {
        if (map instanceof  LinkedHashMap) {
            return new LinkedHashMap<>(map);
        }else if (map instanceof  ConcurrentHashMap) {
            return new ConcurrentHashMap<>(map);
        } else {
            return new LinkedHashMap<>(map);
        }
    }


    @Universal
    public static <K, V> V after(NavigableMap<K, V> map, final K index) {
        return map.get(map.higherKey(index));
    }


    @Universal
    public static <K, V> V before(NavigableMap<K, V> map, final K index) {
        return map.get(map.lowerKey(index));
    }


    @Universal
    public static <K, V> SortedMap<K, V> slc(NavigableMap<K, V> map, K startIndex, K endIndex) {
        return map.subMap(startIndex, endIndex);
    }


    @Universal
    public static <K, V> SortedMap<K, V> slcEnd(NavigableMap<K, V> map, K fromKey) {
        return map.tailMap(fromKey);
    }

    @Universal
    public static <K, V> SortedMap<K, V> slc(NavigableMap<K, V> map, K toKey) {
        return map.headMap(toKey);
    }

    /** End universal methods. */

    public static <K, V> boolean valueIn(V value, Map<K, V> map) {
        return map.containsValue(value);
    }


    public static <K, V> Entry<K, V> entry(final K k, final V v) {
        return new EntryImpl<>(k, v);
    }

    public static <K, V> Entry<K, V> entry(Entry<K, V> entry) {
        return new EntryImpl<>(entry);
    }

    public static interface Entry<K, V> extends Comparable<Entry>,
            Serializable, Cloneable {
        K key();

        V value();

        boolean equals(Entry o) ;
    }

    public static class EntryImpl<K, V> implements Entry<K, V> {

        private K k;
        private V v;

        public EntryImpl() {

        }

        public EntryImpl(EntryImpl<K, V> impl) {
            Objects.requireNonNull(impl);
            Objects.requireNonNull(impl.k);
            Objects.requireNonNull(impl.v);

            this.k = impl.k;
            this.v = impl.v;
        }

        public EntryImpl(Entry<K, V> entry) {
            Objects.requireNonNull(entry);
            Objects.requireNonNull(entry.key());
            Objects.requireNonNull(entry.value());

            this.k = entry.key();
            this.v = entry.value();
        }

        public EntryImpl(K k, V v) {
            Objects.requireNonNull(k);
            Objects.requireNonNull(v);

            this.k = k;
            this.v = v;
        }

        @Override
        public K key() {
            return k;
        }

        @Override
        public V value() {
            return v;
        }



        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EntryImpl entry = (EntryImpl) o;
            return this.equals(entry);
        }

        @Override
        public boolean equals(Entry entry) {

            if (k != null ? !k.equals(entry.key()) : entry.key() != null) return false;
            return !(v != null ? !v.equals(entry.value()) : entry.value() != null);

        }

        @Override
        public int hashCode() {
            int result = k != null ? k.hashCode() : 0;
            result = 31 * result + (v != null ? v.hashCode() : 0);
            return result;
        }

        @Override
        public int compareTo(Entry entry) {
            Objects.requireNonNull(entry);
            return this.key().toString().compareTo(entry.key().toString());
        }

        @Override
        public String toString() {
            return "{" +
                    "\"k\":" + k +
                    ", \"v\":" + v +
                    '}';
        }
    }




    public static <K, V> Map<K, V> map(Class<K> keyClass, Class<V> valueClass) {
        return new LinkedHashMap<>(10);
    }

    public static <K, V> Map<K, V> map(K k0, V v0) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        return map;
    }

    public static <K, V> Map<K, V> map(K k0, V v0, K k1, V v1) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }


    public static <K, V> Map<K, V> map(K k0, V v0, K k1, V v1, K k2, V v2) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> map(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                       V v3) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> Map<K, V> map(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                       V v3, K k4, V v4) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> Map<K, V> map(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                       V v3, K k4, V v4, K k5, V v5) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V> Map<K, V> map(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                       V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    public static <K, V> Map<K, V> map(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                       V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return map;
    }

    public static <K, V> Map<K, V> map(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                       V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return map;
    }

    public static <K, V> Map<K, V> map(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                       V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
                                       K k9, V v9) {
        Map<K, V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return map;
    }

    public static <K, V> Map<K, V> map(Collection<K> keys, Collection<V> values) {
        Map<K, V> map = new LinkedHashMap<>(10 + keys.size());
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> map(Iterable<K> keys, Iterable<V> values) {
        Map<K, V> map = new LinkedHashMap<>();
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> map(K[] keys, V[] values) {

        Map<K, V> map = new LinkedHashMap<>(10 + keys.length);
        int index = 0;
        for (K k : keys) {
            if (index < keys.length) {
                V v = values[index];
                map.put(k, v);
            } else {
                map.put(k, null);
            }
            index++;
        }
        return map;
    }


    @SafeVarargs
    public static <K, V> Map<K, V> map(Entry<K, V>... entries) {
        Map<K, V> map = new LinkedHashMap<>(entries.length);
        for (Entry<K, V> entry : entries) {
            map.put(entry.key(), entry.value());
        }
        return map;
    }


    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0, K k1, V v1) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0, K k1, V v1, K k2, V v2) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
                                                      K k9, V v9) {
        NavigableMap<K, V> map = new TreeMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Collection<K> keys, Collection<V> values) {
        NavigableMap<K, V> map = new TreeMap<>();
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }


    public static <K, V> NavigableMap<K, V> sortedMap(Iterable<K> keys, Iterable<V> values) {
        NavigableMap<K, V> map = new TreeMap<>();
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }


    public static <K, V> NavigableMap<K, V> sortedMap(K[] keys, V[] values) {

        NavigableMap<K, V> map = new TreeMap<>();
        int index = 0;
        for (K k : keys) {
            if (index < keys.length) {
                V v = values[index];
                map.put(k, v);
            } else {
                map.put(k, null);
            }
            index++;
        }
        return map;
    }


    public static <K, V> NavigableMap<K, V> sortedMap(List<Entry<K, V>> entries) {
        NavigableMap<K, V> map = new TreeMap<>();
        for (Entry<K, V> entry : entries) {
            map.put(entry.key(), entry.value());
        }
        return map;
    }


    //

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
                                                      K k9, V v9) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, Collection<K> keys, Collection<V> values) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, K[] keys, V[] values) {

        NavigableMap<K, V> map = new TreeMap<>(comparator);
        int index = 0;
        for (K k : keys) {
            if (index < keys.length) {
                V v = values[index];
                map.put(k, v);
            } else {
                map.put(k, null);
            }
            index++;
        }
        return map;
    }


    public static <K, V> NavigableMap<K, V> sortedMap(Comparator<K> comparator, List<Entry<K, V>> entries) {
        NavigableMap<K, V> map = new TreeMap<>(comparator);
        for (Entry<K, V> entry : entries) {
            map.put(entry.key(), entry.value());
        }
        return map;
    }





    public static <K, V> Map<K, V> safeMap(K k0, V v0) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        return map;
    }

    public static <K, V> Map<K, V> safeMap(K k0, V v0, K k1, V v1) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }


    public static <K, V> Map<K, V> safeMap(K k0, V v0, K k1, V v1, K k2, V v2) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> safeMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                           V v3) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> Map<K, V> safeMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                           V v3, K k4, V v4) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> Map<K, V> safeMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                           V v3, K k4, V v4, K k5, V v5) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V> Map<K, V> safeMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                           V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    public static <K, V> Map<K, V> safeMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                           V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return map;
    }

    public static <K, V> Map<K, V> safeMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                           V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return map;
    }

    public static <K, V> Map<K, V> safeMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                           V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
                                           K k9, V v9) {
        Map<K, V> map = new ConcurrentHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return map;
    }

    public static <K, V> Map<K, V> safeMap(Collection<K> keys, Collection<V> values) {
        Map<K, V> map = new ConcurrentHashMap<>(10 + keys.size());
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> safeMap(Iterable<K> keys, Iterable<V> values) {
        Map<K, V> map = new ConcurrentHashMap<>();
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> safeMap(K[] keys, V[] values) {

        Map<K, V> map = new ConcurrentHashMap<>(10 + keys.length);
        int index = 0;
        for (K k : keys) {
            if (index < keys.length) {
                V v = values[index];
                map.put(k, v);
            } else {
                map.put(k, null);
            }
            index++;
        }
        return map;
    }


    @SafeVarargs
    public static <K, V> Map<K, V> safeMap(Entry<K, V>... entries) {
        Map<K, V> map = new ConcurrentHashMap<>(entries.length);
        for (Entry<K, V> entry : entries) {
            map.put(entry.key(), entry.value());
        }
        return map;
    }



    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0, K k1, V v1) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }


    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0, K k1, V v1, K k2, V v2) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
                                                          K k9, V v9) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Collection<K> keys, Collection<V> values) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Iterable<K> keys, Iterable<V> values) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(K[] keys, V[] values) {

        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        int index = 0;
        for (K k : keys) {
            if (index < keys.length) {
                V v = values[index];
                map.put(k, v);
            } else {
                map.put(k, null);
            }
            index++;
        }
        return map;
    }


    @SafeVarargs
    public static <K, V> NavigableMap<K, V> safeSortedMap(Entry<K, V>... entries) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>();
        for (Entry<K, V> entry : entries) {
            map.put(entry.key(), entry.value());
        }
        return map;
    }




    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                                          V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
                                                          K k9, V v9) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, Collection<K> keys, Collection<V> values) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, K[] keys, V[] values) {

        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        int index = 0;
        for (K k : keys) {
            if (index < keys.length) {
                V v = values[index];
                map.put(k, v);
            } else {
                map.put(k, null);
            }
            index++;
        }
        return map;
    }


    public static <K, V> NavigableMap<K, V> safeSortedMap(Comparator<K> comparator, List<Entry<K, V>> entries) {
        NavigableMap<K, V> map = new ConcurrentSkipListMap<>(comparator);
        for (Entry<K, V> entry : entries) {
            map.put(entry.key(), entry.value());
        }
        return map;
    }



}
