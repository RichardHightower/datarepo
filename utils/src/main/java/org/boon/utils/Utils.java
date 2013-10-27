package org.boon.utils;

import org.boon.core.Typ;
import org.boon.core.reflection.fields.FieldAccess;
import org.boon.core.reflection.Reflection;

import java.io.IOException;
import java.io.PrintStream;
import java.text.Collator;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.boon.utils.ComplainAndDie.die;
import static org.boon.utils.ComplainAndDie.handleUnexpectedException;

public class Utils {
    static Class<Utils> utils = Utils.class;

    private static final Logger log = log(utils);

    public static Logger log(Class<?> clzz) {
        return Logger.getLogger(clzz.getName());
    }

    private static final Logger appLog = Logger.getLogger(sprop(
            pkey(Utils.class, "appLog"), "genericLog"));


    public static final boolean debug;
    public static final PrintStream OUT = System.out;
    public static final PrintStream ERR = System.err;

    public final static String quote = "\"";

    public static String quote(String str) {
        return quote + str + quote;
    }

    public final static String singleQuote = "'";

    public static String singleQuote(String str) {
        return singleQuote + str + singleQuote;
    }

    public final static String comma = ",";

    public final static String comma(String str) {
        return str + comma;
    }

    static {
        debug = sbprop(pkey(Utils.class, "debug"));
    }


    public static String pkey(Class<?> cls, String key) {
        return cls.getName() + "." + key;
    }

    public static String sprop(String key) {
        return System.getProperty(key);
    }

    public static String sprop(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    public static boolean sbprop(String key) {
        return Boolean.getBoolean(key);
    }


    public static Comparable comparable(Object comparable) {
        return (Comparable) comparable;
    }


    public static void sortAsc(List list) {
        sortAsc(list, "this");
    }

    public static void sortDesc(List list) {
        sortDesc(list, "this");
    }

    public static void sortAsc(List list, String sortBy) {
        if (list == null || list.size() == 0) {
            return;
        }
        Map<String, FieldAccess> fields = Reflection.getPropertyFieldAccessMap(list.iterator().next().getClass());

        sortAsc(list, sortBy, fields);
    }

    public static void sortDesc(List list, String sortBy) {
        if (list == null || list.size() == 0) {
            return;
        }
        Map<String, FieldAccess> fields = Reflection.getPropertyFieldAccessMap(list.iterator().next().getClass());

        sortDesc(list, sortBy, fields);
    }

    public static void sortAsc(List list, String sortBy, Map<String, FieldAccess> fields) {
        sort(list, sortBy, fields, true);
    }

    public static void sortDesc(List list, String sortBy, Map<String, FieldAccess> fields) {
        sort(list, sortBy, fields, false);
    }

    public static void sort(List list, String sortBy, Map<String, FieldAccess> fields, boolean ascending) {
        if (list == null || list.size() == 0) {
            return;
        }
        Object o = list.get(0);
        if (sortBy.equals("this") && o instanceof Comparable) {
            Collections.sort(list);
            if (!ascending) {
                Collections.reverse(list);
            }
            return;
        }

        final FieldAccess field = fields.get(sortBy);

        if (field != null) {

            Collections.sort(list, Utils.universalComparator(field, ascending));

        }
    }


    public static Comparator universalComparator(final FieldAccess field, final boolean ascending) {
        return new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Object value1 = null;
                Object value2 = null;

                if (ascending) {
                    value1 = field.getValue(o1);
                    value2 = field.getValue(o2);
                } else {
                    value1 = field.getValue(o2);
                    value2 = field.getValue(o1);
                }
                return Utils.compare(value1, value2);
            }
        };
    }


    public static Comparator universalComparator(final String sortBy, final Map<String, FieldAccess> fields,
                                                 final boolean ascending, final List<Comparator> comparators) {
        return new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {

                Object value1 = null;
                Object value2 = null;

                if (sortBy.equals("this") && o1 instanceof Comparable) {
                    value1 = o1;
                    value2 = o2;
                    if (ascending) {
                        value1 = o1;
                        value2 = o2;
                    } else {
                        value1 = o2;
                        value2 = o1;
                    }
                } else {
                    FieldAccess field = fields.get(sortBy);
                    if (field == null) {
                        ComplainAndDie.complain(lines(
                                "The fields was null for sortBy " + sortBy,
                                sprintf("fields = %s", fields),
                                sprintf("Outer object type = %s", o1.getClass().getName()),
                                sprintf("Outer object is %s", o1)
                        ));
                    }
                    if (ascending) {
                        value1 = field.getValue(o1);
                        value2 = field.getValue(o2);
                    } else {
                        value1 = field.getValue(o2);
                        value2 = field.getValue(o1);
                    }
                }


                int compare = Utils.compare(value1, value2);
                if (compare == 0) {
                    for (Comparator comparator : comparators) {
                        compare = comparator.compare(o1, o2);
                        if (compare != 0) {
                            break;
                        }
                    }
                }
                return compare;
            }
        };
    }


    public static int compare(Object value1, Object value2) {

        if (value1 == null && value2 == null) {
            return 0;
        } else if (value1 == null && value2 != null) {
            return -1;
        } else if (value1 != null && value2 == null) {
            return 1;
        }


        if (value1 instanceof CharSequence) {
            String str1 = Conversions.toString(value1);
            String str2 = Conversions.toString(value2);
            Collator collator = Collator.getInstance();
            return collator.compare(str1, str2);
        } else if (Conversions.isComparable(value1)) {
            Comparable c1 = comparable(value1);
            Comparable c2 = comparable(value2);
            return c1.compareTo(c2);
        } else {
            String name = Reflection.getSortableField(value1);
            String sv1 = (String) Reflection.getPropByPath(value1, name);
            String sv2 = (String) Reflection.getPropByPath(value2, name);
            return Utils.compare(sv1, sv2);

        }

    }

    public static void assertTrue(boolean b) {

        if (b == false) {
            die("Condition was false");
        }

    }


    public static interface Enumerate<T> {
        void visit(int index, T t);
    }





    public static String sprintf(String fmt, Object... args) {
        return String.format(fmt, args);
    }

    // CREATIONAL
    // CREATIONAL

    // CREATIONAL
    // CREATIONAL
    public static interface Entry<K, V> {
        K key();

        V value();
    }

    public static class EntryImpl<K, V> implements Entry<K, V> {
        public EntryImpl(K k, V v) {
            this.k = k;
            this.v = v;
        }

        K k;
        V v;

        @Override
        public K key() {
            return k;
        }

        @Override
        public V value() {
            return v;
        }
    }

    public static <K, V> Entry<K, V> kv(final K k, final V v) {
        return new EntryImpl<K, V>(k, v);
    }

    public static <K, V> Entry<K, V> entry(final K k, final V v) {
        return new EntryImpl<K, V>(k, v);
    }

    public static <V> List<V> ls(@SuppressWarnings("unchecked") final V... array) {
        return list(array);
    }

    public static <V> List<V> ls(Class<V> cls, @SuppressWarnings("unchecked") final Object... array) {
        return list(cls, array);
    }


    public static List<Double> dlist(final double... array) {
        List<Double> list = new ArrayList<Double>();
        for (double d : array) {
            list.add(d);
        }
        return list;
    }

    public static List<Long> flist(final long... array) {
        List<Long> list = new ArrayList<Long>();
        for (long d : array) {
            list.add(d);
        }
        return list;
    }

    public static List<Float> llist(final float... array) {
        List<Float> list = new ArrayList<Float>();
        for (float x : array) {
            list.add(x);
        }
        return list;
    }

    public static List<Integer> ilist(final int... array) {
        List<Integer> list = new ArrayList<Integer>();
        for (int x : array) {
            list.add(x);
        }
        return list;
    }

    public static List<Byte> blist(final byte... array) {
        List<Byte> list = new ArrayList<Byte>();
        for (byte x : array) {
            list.add(x);
        }
        return list;
    }

    public static List<Short> slist(final short... array) {
        List<Short> list = new ArrayList<Short>();
        for (short x : array) {
            list.add(x);
        }
        return list;
    }

    @SafeVarargs
    public static <V> List<V> lsRange(int start, int finish, final V... array) {
        List<V> list = new ArrayList<V>(finish - start);
        for (; start < finish; start++) {
            list.add(array[start]);
        }
        return list;
    }

    public static List<Integer> noZeroList(int[] array) {
        List<Integer> arrayList = new ArrayList<Integer>(array.length);
        for (int i : array) {
            arrayList.add(i);
        }
        return arrayList;
    }
//identityHashCode

    public static boolean isSame(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null) {
            return false;
        }
        if (right == null) {
            return false;
        }
        return System.identityHashCode(left) == System.identityHashCode(right);

    }

    public static boolean isEqual(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null) {
            return false;
        }
        if (right == null) {
            return false;
        }
        return left.equals(right);

    }

    public static boolean isEqual(List<?> left, List<?> right) {
        if (left.size() != right.size()) {
            return false;
        }
        int index = 0;
        for (Object it : left) {
            if (!isEqual(it, right.get(index))) {
                return false;
            }
            index++;
        }
        return true;
    }


    @SafeVarargs
    public static <V> V[] array(final V... array) {
        return array;
    }


    public static Object[] oarray(final Object... array) {
        return array;
    }

    public static Object[] oar(final Object... array) {
        return array;
    }

    @SafeVarargs
    public static <V> V[] ary(final V... array) {
        return array;
    }

    public static char[] ary(String str) {
        return str.toCharArray();
    }

    public static char[] array(String str) {
        return str.toCharArray();
    }


    @SafeVarargs
    public static <V> List<V> list(final V... array) {
        if (array == null) {
            return null;
        }
        ArrayList<V> list = new ArrayList<V>(array.length);
        for (V o : array) {
            list.add(o);
        }
        return list;
    }

    public static <V> List<V> list(Class<V> cls, final Object... array) {
        if (array == null) {
            return null;
        }
        ArrayList<V> list = new ArrayList<V>(array.length);
        for (Object o : array) {
            list.add((V) o);
        }
        return list;
    }

    public static <V> List<V> ls(final Collection<V> col) {
        return list(col);
    }

    public static <V> List<V> list(final Collection<V> col) {
        return new ArrayList<V>(col);
    }

    public static <V> List<V> ls(Collection<V> lst,
                                 final Collection<?>... others) {
        return list(lst, others);
    }

    @SuppressWarnings("unchecked")
    public static <V> List<V> list(Collection<V> lst,
                                   final Collection<?>... others) {
        int total = lst.size();
        for (Collection<?> c : others) {
            total += c.size();
        }
        ArrayList<Object> list = new ArrayList<Object>(total);
        list.addAll(lst);
        for (Collection<?> c : others) {
            list.addAll(c);
        }
        return (List<V>) list;
    }

    public static <K, V> Map<K, V> copy(final Map<K, V> map) {
        return Reflection.copy(map);
    }

    public static <V> List<V> copy(final List<V> col) {
        return Reflection.copy(col);
    }

    public static <V> Set<V> copy(final Set<V> col) {
        return Reflection.copy(col);
    }

    public static <V> SortedSet<V> copy(final SortedSet<V> col) {
        return Reflection.copy(col);
    }

    public static String copy(final String str) {
        return str.toString();
    }

    public static StringBuilder copy(final StringBuilder str) {
        return new StringBuilder(str);
    }

    public static CharSequence copy(final CharSequence str) {
        return str.toString();
    }

    public static <V> List<V> mul(int x, Collection<V> lst) {
        ArrayList<V> list = new ArrayList<V>(x * lst.size());
        for (int index = 0; index < x; index++) {
            for (V element : lst) {
                list.add(element);
            }
        }
        return list;
    }

    public static String mul(int x, CharSequence seq) {
        StringBuilder builder = new StringBuilder(x * seq.length());
        for (int index = 0; index < x; index++) {
            builder.append(seq);
        }
        return builder.toString();
    }

    @SafeVarargs
    public static <V> Set<V> set(final V... array) {
        return new HashSet<V>(list(array));
    }

    public static List<Integer> range(int stop) {
        return range(0, stop);
    }

    public static List<Integer> range(int start, int stop) {
        ArrayList<Integer> range = new ArrayList<Integer>(stop - start);
        for (int index = start; index < stop; index++) {
            range.add(index);
        }
        return range;
    }

    public static List<Integer> range(int start, int stop, int inc) {
        ArrayList<Integer> range = new ArrayList<Integer>(stop - start);
        for (int index = start; index < stop; index += inc) {
            range.add(index);
        }
        return range;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
        map.put(k0, v0);
        return map;
    }

    public static <K, V> Map<K, V> mp(Class<K> key, Class<V> v) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3, K k4, V v4) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3, K k4, V v4, K k5, V v5) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
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

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
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

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
                                      K k9, V v9) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10);
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

    @SafeVarargs
    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
                                      K k9, V v9, Entry<K, V>... entries) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10 + entries.length);
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
        for (Entry<K, V> entry : entries) {
            map.put(entry.key(), entry.value());
        }
        return map;
    }

    public static <K, V> Map<K, V> mp(Collection<K> keys, Collection<V> values) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10 + keys.size());
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

    public static <V> Map<String, V> mp(String propertyKey, Collection<V> values) {
        LinkedHashMap<String, V> map = new LinkedHashMap<String, V>(values.size());
        Iterator<V> iterator = values.iterator();
        for (V v : values) {
            String key = Reflection.getProperty(Typ.string, v, propertyKey);
            map.put(key, v);
        }
        return map;
    }

    public static <K, V> Map<K, V> mp(K[] keys, V[] values) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(10 + keys.length);
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
    public static <K, V> Map<K, V> mp(Entry<K, V>... entries) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(entries.length);
        for (Entry<K, V> entry : entries) {
            map.put(entry.key(), entry.value());
        }
        return map;
    }


    public interface Filter<T> {
        boolean filter(T x);
    }


    public interface Converter<TO, FROM> {
        TO convert(FROM from);
    }


    public static <TO, FROM> List<TO> map(Converter<TO, FROM> converter,
                                          List<FROM> fromList) {

        ArrayList<TO> toList = new ArrayList<TO>(fromList.size());

        for (FROM from : fromList) {
            toList.add(converter.convert(from));
        }

        return toList;
    }

    public static <TO, FROM> List<TO> mapFilterNulls(Converter<TO, FROM> converter,
                                                     List<FROM> fromList) {

        ArrayList<TO> toList = new ArrayList<TO>(fromList.size());

        for (FROM from : fromList) {
            TO converted = converter.convert(from);
            if (converted != null) {
                toList.add(converted);
            }
        }

        return toList;
    }


    public static String lns(String... lines) {
        return lines(lines);
    }

    public static String lines(String... lines) {
        return joinBy('\n', lines);
    }


    public static String joinBy(char delim, String... args) {
        return Reflection.joinBy(delim, (Object[]) args);
    }
    public static String join(char delim, Integer... args) {
        return Reflection.joinBy(delim, (Object[]) args);
    }

    public static String join(Collection<?> args) {
        StringBuilder builder = new StringBuilder(256);

        for (Object arg : args) {
            builder.append(arg.toString());
        }
        return builder.toString();
    }

    public static String join(char delim, Collection<?> args) {
        StringBuilder builder = new StringBuilder(256);

        int index = 0;
        for (Object arg : args) {
            builder.append(arg.toString());
            builder.append(delim);
            if (!(index == args.size() - 1)) {
                builder.append(delim);
            }
            index++;

        }
        return builder.toString();
    }

    public static String join(String delim, Collection<?> args) {
        StringBuilder builder = new StringBuilder(256);
        int index = 0;
        for (Object arg : args) {
            builder.append(arg.toString());
            builder.append(delim);
            if (!(index == args.size() - 1)) {
                builder.append(delim);
            }
            index++;
        }
        return builder.toString();
    }

    public static String slc(CharSequence str, int start, int end) {
        return slice(str, start, end);
    }

    public static String slice(CharSequence str, int start, int end) {
        final int length = str.length();

        // Adjust
        if (start < 0) {
            start = length + start;
        }
        if (end < 0) {
            end = length + end;
        }

        // Bound check
        if (start < 0) {
            start = 0;
        }
        if (start > length) {
            start = length;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (end < 0) {
            end = 0;
        }

        // Bound check
        if (start > end) {
            return "";
        }

        return str.subSequence(start, end).toString();
    }

    public static String slc(CharSequence str, int start) {
        return slice(str, start);
    }

    public static String slice(CharSequence str, int start) {
        return slice(str, start, str.length());
    }

    public static char index(CharSequence str, int index) {
        return idx(str, index);
    }

    public static void idx(Object object, int index, Object value) {
        Reflection.idx(object, index, value);
    }


    public static char idx(CharSequence str, int index) {
        final int length = str.length();

        // Adjust
        if (index < 0) {
            index = length + index;
        }

        // Bound check
        if (index < 0) {
            index = 0;
        }
        if (index > length) {
            index = length;
        }

        return str.charAt(index);

    }

    public static <T> List<T> slc(List<T> lst, int start, int end) {
        return slice(lst, start, end);
    }

    public static <T> List<T> slice(List<T> lst, int start, int end) {
        final int length = lst.size();
        // Adjust
        if (start < 0) {
            start = length + start;
        }
        if (end < 0) {
            end = length + end;
        }

        // Bound check
        if (start < 0) {
            start = 0;
        }
        if (start > length) {
            start = length;
        }
        if (end > length) {
            end = length;
        }
        if (end < 0) {
            end = 0;
        }

        // Bound check
        if (start > end) {
            return lst.subList(0, 0);
        }
        return lst.subList(start, end);
    }

    public static <T> void rpl(List<T> lst, int start, int end,
                               List<T> replacement) {
        replace(lst, start, end, replacement);
    }

    public static <T> void rpl(List<T> lst, int start, List<T> replacement) {
        rpl(lst, start, replacement);
    }

    public static <T> void replace(List<T> lst, int start, List<T> replacement) {
        replace(lst, start, lst.size(), replacement);
    }

    public static <T> void replace(List<T> lst, int start, int end,
                                   List<T> replacement) {
        final int length = lst.size();
        // Adjust
        if (start < 0) {
            start = length + start;
        }
        if (end < 0) {
            end = length + end;
        }

        // Bound check
        if (start < 0) {
            start = 0;
        }
        if (start > length) {
            start = length;
        }
        if (end > length) {
            end = length;
        }
        if (end < 0) {
            end = 0;
        }

        if (start == end) {
            List<T> copy = copy(replacement);
            Collections.reverse(copy);
            for (T t : copy) {
                lst.add(start, t);
            }
            return;
        }

        int slicesize = end - start;
        int current = start;
        for (int index = 0; index < replacement.size(); index++, current++) {
            T t = replacement.get(index);
            if (index < slicesize) {
                lst.set(current, t);
            } else {
                lst.add(current, t);
            }
        }
        int stop = current;
        while (current < end) {
            lst.remove(stop);
            current++;
        }

    }

    public static <T> List<T> slc(List<T> lst, int start) {
        return slice(lst, start);
    }

    public static <T> List<T> slice(List<T> lst, int start) {
        return slice(lst, start, lst.size());
    }

    public static <T> T idx(List<T> lst, int index) {
        return index(lst, index);
    }

    public static <T> T index(List<T> lst, int index) {
        final int length = lst.size();

        if (length == 0) {
            return null;
        }

        // Adjust
        if (index < 0) {
            index = length + index;
        }

        // Bound check
        if (index < 0) {
            index = 0;
        }
        if (index >= length) {
            index = length;
        }

        return lst.get(index);

    }

    public static void expect(double d1, double d2, double value) {
        if (value > d1 && value < d2) {

        } else {
            die(
                    "expected was to be between %d and %d but was %d", d1, d2, value);

        }
    }

    public static <T> void expect(T ex, T v) {
        if (ex == null && v == null) {
            return;
        }

        if (ex == null && v != null) {
            die(
                    "expected was null, but set was %s", v);
        }

        if (!ex.equals(v)) {
            die(
                    "expected was %s, but set was %s", ex, v);
        }

    }

    public static void expect(boolean test) {
        if (!test) {
            die(
                    "expected condition false");
        }
    }


    public static <T> void expect(String msg, T ex, T v) {
        if (ex == null && v == null) {
            return;
        }

        if (ex == null && v != null) {
            die(
                    "%s | expected \n null, but set was \n #%s#", msg, v);
        }

        if (!ex.equals(v)) {
            die(
                    "%s | expected \n#%s#, but set was \n#%s#", msg, ex, v);
        }

    }


    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            handleUnexpectedException(e);
        }
    }



    public static String trimStart(String arg, String delim) {
        if (arg.startsWith(delim)) {
            return arg.substring(delim.length(), arg.length());
        } else {
            return arg;
        }
    }


    public static void main(String[] args) {
    }

}
