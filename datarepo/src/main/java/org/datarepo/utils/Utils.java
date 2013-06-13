package org.datarepo.utils;

import org.datarepo.fields.FieldAccess;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.Collator;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
    static Class<Utils> utils = Utils.class;

    private static final Logger log = log(utils);
    public static Class<Comparable> comparable = Comparable.class;
    public static Class<Collection> collection = Collection.class;

    public static Logger log(Class<?> clzz) {
        return Logger.getLogger(clzz.getName());
    }

    private static final Logger appLog = Logger.getLogger(sprop(
            pkey(Utils.class, "appLog"), "genericLog"));

    public static final Class<Object> object = Object.class;
    public static final Class<String> string = String.class;
    @SuppressWarnings("rawtypes")
    public static final Class<List> slist = List.class;
    public static final Class<String[]> sarray = String[].class;
    public static final Class<Boolean> bool = Boolean.class;
    public static final Class<Integer> integer = Integer.class;
    public static final Class<Number> number = Number.class;
    public static final Class<CharSequence> chars = CharSequence.class;


    public static final Class<Float> flt = Float.class;
    public static final Class<Long> lng = Long.class;
    public static final Class<Double> dbl = Double.class;
    public static final Class<?> pint = int.class;
    public static final Class<?> pboolean = boolean.class;
    public static final Class<?> pfloat = float.class;
    public static final Class<?> pdouble = double.class;
    public static final Class<?> plong = long.class;
    public static final Class<?> pshort = short.class;
    public static final Class<?> pchar = char.class;
    public static final Class<?> pbyte = byte.class;


    public static final Class<Date> date = Date.class;
    public static final Class<Calendar> calendar = Calendar.class;

    public static final Class<String[]> stringA = String[].class;
    public static final Class<int[]> intA = int[].class;
    public static final Class<byte[]> byteA = byte[].class;
    public static final Class<short[]> shortA = short[].class;
    public static final Class<char[]> charA = char[].class;
    public static final Class<long[]> longA = long[].class;
    public static final Class<float[]> floatA = float[].class;
    public static final Class<double[]> doubleA = double[].class;
    public static final Class<Object[]> objectA = Object[].class;

    public static final Class<File> fileT = File.class;
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

    public static void print(String... items) {
        if (items != null && items.length == 1) {
            System.out.println(items[0]);
        } else {
            print((Object[]) items);
        }
    }

    public static void print(Object... items) {
        System.out.println(sprint(items));
    }

    public static void fprint(Logger log, Object... items) {
        log.info(sprint(items));
    }

    public static void fprint(Logger log, Level level, Object... items) {
        log.log(level, sprint(items));
    }

    public static void fprint(Appendable appendable, Object... items) {
        add(appendable, sprint(items) + "\n");
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
                        complain(lines(
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
            String str1 = str(value1);
            String str2 = str(value2);
            Collator collator = Collator.getInstance();
            return collator.compare(str1, str2);
        } else if (Types.isComparable(value1)) {
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

    public static class PrintEnumerate implements Enumerate<Object> {

        @Override
        public void visit(int index, Object object) {
            print(object);
        }

    }

    public static PrintEnumerate printEnum = new PrintEnumerate();

    public static String sprint(String... items) {
        return sprint((Object[]) items);
    }

    public static String sprint(Object... items) {
        if (items == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(256);
        for (Object item : items) {
            if (item != null && isArray(item)) {
                builder.append(ls((Object[]) item));
            } else {
                builder.append(item);
            }
            builder.append(' ');
        }
        return builder.toString();
    }

    public static void debugPrint(Object... items) {
        db(items);
    }

    public static void db(Object... items) {
        System.err.println(sprint(items));
    }

    public static void printf(String fmt, Object... args) {
        if (fmt.endsWith("\n")) {
            System.out.printf(fmt, args);
        } else {
            System.out.printf(fmt + "\n", args);
        }
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static String sprintf(String fmt, Object... args) {
        return String.format(fmt, args);
    }

    public static void fprintf(Logger log, Level level, String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        String message = sprintf(fmt, args);
        log.log(level, message);
    }

    public static void fprintf(Logger log, String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        String message = sprintf(fmt, args);
        log.info(message);
    }

    public static void fprintf(StringBuilder builder, String fmt,
                               Object... args) {
        builder.append((sprintf(fmt, args)));
    }

    public static void fprintf(PrintStream out, String fmt, Object... args) {
        out.append((sprintf(fmt, args)));
    }

    public static void fprintf(Appendable out, String fmt, Object... args) {
        try {
            out.append((sprintf(fmt, args)));
        } catch (IOException e) {
            warning(log, e, "problem with fprintf");
        }
    }

    public static void fprintln(PrintStream out, CharSequence message) {
        out.println(message);
    }

    public static void fprintln(StringBuilder builder, CharSequence message) {
        builder.append(message).append("\n");
    }

    public static void fprintln(Appendable out, CharSequence cs) {
        try {
            out.append(cs);
            out.append("\n");
        } catch (IOException e) {
            warning(log, e, "problem with fprintf");
        }
    }

    // Logging
    // Logging
    // Logging
    public static void warning(Logger log, Throwable ex, String fmt,
                               Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.log(Level.WARNING, String.format(fmt, args), ex);
    }

    public static void error(Logger log, Throwable ex, String fmt,
                             Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.log(Level.SEVERE, String.format(fmt, args), ex);
    }

    public static void info(Logger log, Throwable ex, String fmt,
                            Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.log(Level.INFO, String.format(fmt, args), ex);

    }

    public static void debug(Logger log, Throwable ex, String fmt,
                             Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.log(Level.FINE, String.format(fmt, args), ex);

    }

    public static void trace(Logger log, Throwable ex, String fmt,
                             Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.log(Level.FINEST, String.format(fmt, args), ex);
    }

    public static void config(Logger log, Throwable ex, String fmt,
                              Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.log(Level.CONFIG, String.format(fmt, args), ex);
    }

    public static void config(Logger log, String fmt,
                              Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.log(Level.CONFIG, String.format(fmt, args));
    }

    public static void warning(Logger log, String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.warning(String.format(fmt, args));
    }

    public static void error(Logger log, String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.warning(String.format(fmt, args));
    }

    public static void info(Logger log, String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.info(String.format(fmt, args));
    }

    public static void debug(Logger log, String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.fine(String.format(fmt, args));
    }

    public static void trace(Logger log, String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        log.finest(String.format(fmt, args));
    }

    public static void error(String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        appLog.severe(String.format(fmt, args));
    }

    public static void warning(String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        appLog.warning(String.format(fmt, args));
    }

    public static void info(String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        appLog.info(String.format(fmt, args));
    }

    public static void debug(String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        appLog.fine(String.format(fmt, args));
    }

    public static void trace(String fmt, Object... args) {
        if (debug) {
            printf(fmt, args);
            return;
        }
        appLog.finest(String.format(fmt, args));
    }

    @SuppressWarnings("serial")
    public static class AssertionException extends RuntimeException {

        public AssertionException() {
            super();
        }

        public AssertionException(String message, Throwable cause) {
            super(message, cause);
        }

        public AssertionException(String message) {
            super(message);
        }

        public AssertionException(Throwable cause) {
            super(cause);
        }
    }


    public static void die(boolean condition, String message) {
        if (condition) {
            throw new AssertionException(message);
        }
    }

    public static void die(String message) {
        throw new AssertionException(message);
    }

    public static void die(String message, Object... args) {
        throw new AssertionException(String.format(message, args));
    }

    public static void die(Throwable t, String message, Object... args) {
        throw new AssertionException(String.format(message, args), t);
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

    public static boolean debugEqual(List<?> left, List<?> right) {
        print(left, right);
        if (left.size() != right.size()) {
            print("size not equal");
            return false;
        }
        int index = 0;
        for (Object it : left) {
            if (!right.get(index).equals(it)) {
                print("item not equal at index", index, "#" + it + "#", "%"
                        + right.get(index) + "%", it.getClass(),
                        right.get(index).getClass());
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

    public static <V> V[] array(Class<V> type, final Collection<V> array) {
        return (V[]) Types.toArray(type, array);
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
        return new LinkedHashMap<K, V>(map);
    }

    public static <V> List<V> copy(final List<V> col) {
        return new ArrayList<V>(col);
    }

    public static <V> Set<V> copy(final Set<V> col) {
        return new HashSet<V>(col);
    }

    public static <V> SortedSet<V> copy(final SortedSet<V> col) {
        return new TreeSet<V>(col);
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
            String key = Reflection.getProperty(string, v, propertyKey);
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

    public static String joinByString(String delim, Object[] args) {
        StringBuilder builder = new StringBuilder(256);
        int index = 0;

        for (Object arg : args) {
            builder.append(str(arg));
            builder.append(delim);
            if (!(index == args.length - 1)) {
                builder.append(delim);
            }
            index++;
        }
        return builder.toString();
    }

    public static String joinBy(char delim, String... args) {
        return joinBy(delim, (Object[]) args);
    }

    public static String joinBy(char delim, Object... args) {
        StringBuilder builder = new StringBuilder(256);

        if (args.length == 1 && isArray(args[0])) {
            Object array = args[0];
            for (int index = 0; index < len(array); index++) {
                Object obj = Reflection.idx(array, index);
                builder.append(obj.toString());
                if (!(index == args.length - 1)) {
                    builder.append(delim);
                }

            }
        } else {
            int index = 0;
            for (Object arg : args) {
                builder.append(arg.toString());
                if (!(index == args.length - 1)) {
                    builder.append(delim);
                }
                index++;
            }
        }
        return builder.toString();
    }

    public static String join(char delim, Integer... args) {
        return joinBy(delim, (Object[]) args);
    }

    public static String join(Integer... args) {
        return joinByString("", (Object[]) args);
    }

    public static String join(String... args) {
        return joinByString("", (Object[]) args);
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
            throw new AssertionException(sprintf(
                    "expected was to be between %d and %d but was %d", d1, d2, value));

        }
    }

    public static <T> void expect(T ex, T v) {
        if (ex == null && v == null) {
            return;
        }

        if (ex == null && v != null) {
            throw new AssertionException(sprintf(
                    "expected was null, but value was %s", v));
        }

        if (!ex.equals(v)) {
            throw new AssertionException(sprintf(
                    "expected was %s, but value was %s", ex, v));
        }

    }

    public static void expect(boolean test) {
        if (!test) {
            throw new AssertionException(sprintf(
                    "expected condition false"));
        }
    }

    public static <T> void expect(Appendable out, T ex, T v) {
        if (ex == null && v == null) {
            return;
        }

        if (ex == null && v != null) {
            fprintf(out, "expected was null, but value was %s", v);
        }

        if (!ex.equals(v)) {
            fprintf(out, "expected was %s, but value was %s", ex, v);
        }

    }


    public static <T> void expect(String msg, T ex, T v) {
        if (ex == null && v == null) {
            return;
        }

        if (ex == null && v != null) {
            throw new AssertionException(sprintf(
                    "%s | expected \n null, but value was \n #%s#", msg, v));
        }

        if (!ex.equals(v)) {
            throw new AssertionException(sprintf(
                    "%s | expected \n#%s#, but value was \n#%s#", msg, ex, v));
        }

    }

    public static <T> void expect(Appendable out, String msg, T ex, T v) {
        if (ex == null && v == null) {
            return;
        }

        if (ex == null && v != null) {
            fprintf(out, "%s | expected null, but value was #%s#", msg, v);
        }

        if (!ex.equals(v)) {
            fprintf(out, "%s | expected \n#%s#, but value was \n#%s#", msg, ex,
                    v);
        }

    }


    public static char[] range(char start, char end) {
        StringBuilder builder = new StringBuilder();
        for (char c = start; c <= end; c++) {
            builder.append(c);
        }
        return builder.toString().toCharArray();
    }


    public static String[] split(String str, char c) {
        char[] array = str.toCharArray();
        String[] strings = split(array, c);
        return strings;
    }

    public static String repr(char c) {
        return "'" + c + "'";
    }

    public static String[] toLines(final char[] buffer) {
        List<String> list = new ArrayList<String>(100);
        StringBuilder builder = new StringBuilder(256);
        String str = null;

        for (int index = 0; index < buffer.length; index++) {
            char c = buffer[index];
            if (c == '\r') {
                if (index < buffer.length) {
                    index++;
                    c = buffer[index];
                    if (c != '\n') {
                        index--;
                    } else {
                        c = buffer[index];
                    }
                }
                str = builder.toString();
                builder.setLength(0);
                list.add(str);
                continue;
            } else if (c == '\n') {
                str = builder.toString();
                builder.setLength(0);
                list.add(str);
                continue;
            } else {
                builder.append(c);
            }
        }

        str = builder.toString();
        list.add(str);

        return list.toArray(new String[list.size()]);

    }

    public static String[] split(final char[] buffer, final char split) {
        List<String> list = new ArrayList<String>(100);
        StringBuilder builder = new StringBuilder(256);
        String str = null;
        for (int index = 0; index < buffer.length; index++) {
            char c = buffer[index];
            if (c == split) {
                str = builder.toString();
                builder.setLength(0);
                list.add(str.trim());
                continue;
            } else {
                builder.append(c);
            }
        }

        if (builder.length() > 0) {
            str = builder.toString();
            list.add(str.trim());
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] splitByArray(final char[] buffer, final char splits[]) {
        List<String> list = new ArrayList<String>(100);
        StringBuilder builder = new StringBuilder(256);
        String str = null;
        for (int index = 0; index < buffer.length; index++) {
            char c = buffer[index];

            if (isIn(c, splits)) {
                str = builder.toString();
                builder.setLength(0);
                list.add(str.trim());
                continue;
            } else {
                builder.append(c);
            }
        }

        if (builder.length() > 0) {
            str = builder.toString();
            list.add(str.trim());
        }
        return list.toArray(new String[list.size()]);
    }

    public static String trim(String str) {
        return str.trim();
    }

    public static String[] toLines(String str) {
        return toLines(str.toCharArray());
    }

    public static String[] toLines(StringBuilder b) {
        char[] buf = new char[b.length()];
        b.getChars(0, buf.length, buf, 0);
        return toLines(buf);
    }

    public static String[] toLines(CharSequence cs) {
        return toLines(cs.toString());
    }

    public static String str(char... chars) {
        return string(chars);
    }

    public static char[] chars(Object obj) {
        if (obj instanceof String) {
            return chars((String) obj);
        } else if (obj instanceof StringBuilder) {
            return chars((StringBuilder) obj);
        } else if (obj instanceof CharSequence) {
            return chars((CharSequence) obj);
        } else {
            return chars(obj.toString());
        }
    }

    public static char[] chars(String str) {
        if (str != null) {
            return str.toCharArray();
        } else {
            return new char[0];
        }
    }

    public static char[] chars(CharSequence str) {
        if (str != null) {
            return str.toString().toCharArray();
        } else {
            return new char[0];
        }
    }


    public static char[] chars(StringBuilder str) {
        if (str != null) {
            final int length = str.length();
            char[] chars = new char[length];
            str.getChars(0, length, chars, length);
            return chars;
        } else {
            return new char[0];
        }
    }


    public static char[] chars(char... chars) {
        return chars;
    }

    public static String string(char... chars) {
        if (chars != null) {
            return String.valueOf(chars);
        } else {
            return "";
        }

    }

    public static String string(int start, int stop, char... chars) {
        if (chars != null) {
            return String.valueOf(chars, start, stop);
        } else {
            return "";
        }

    }

    public static String str(Object obj) {
        return string(obj);
    }

    public static String string(Object obj) {
        return Types.toString(obj);
    }

    public static <T> boolean isIn(T t1, Collection<T> collection) {
        if (t1 == null) {
            return false;
        }
        for (T t2 : collection) {
            if (t1.equals(t2)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean isIn(T t1, Set<T> set) {
        return set.contains(t1);
    }


    public static boolean isIn(char c, char... array) {
        for (int index = 0; index < array.length; index++) {
            if (c == array[index]) {
                return true;
            }
        }
        return false;

    }


    public static char[] add(char[] chars, char... chars2) {
        List<Character> list = charList(chars);
        list.addAll(charList(chars2));
        char[] chars3 = new char[chars.length + chars2.length];
        int index = 0;
        for (char c : list) {
            chars3[index] = c;
            index++;
        }
        return chars3;
    }

    public static <T> List<T> ls(Class<T> t) {
        return new ArrayList<T>();
    }

    public static List<Character> charList(char... chars) {
        List<Character> ls = new ArrayList<Character>(chars.length);
        for (char c : chars) {
            ls.add(c);
        }
        return ls;
    }

    public static boolean isDebug(Logger log) {
        return log.isLoggable(Level.FINE);

    }

    @SafeVarargs
    public static <T> void notNull(T... objects) {
        if (objects == null) {
            throw new IllegalArgumentException(
                    " nulls arguments are not allowed ");

        }
        for (Object obj : objects) {
            if (obj == null) {
                throw new IllegalArgumentException(
                        " nulls arguments are not allowed ");
            }
        }
    }

    public static void notSupported() {
        throw new UnsupportedOperationException();
    }

    public static void complain(String msg, Object... args) {
        throw new UnsupportedOperationException(sprintf(msg, args));
    }


    public static double sum(double... items) {
        double total = 0d;
        for (int index = 0; index < items.length; index++) {
            total += items[index];
        }
        return total;
    }


    public static int len(Collection<?> col) {
        return col.size();
    }


    public static int len(Object[] obj) {
        return obj.length;
    }

    public static int len(Map<?, ?> map) {
        return map.size();
    }

    public static int len(double[] v) {
        return v.length;
    }

    public static int len(long[] v) {
        return v.length;
    }

    public static int len(float[] v) {
        return v.length;
    }

    public static int len(int[] v) {
        return v.length;
    }

    public static int len(short[] v) {
        return v.length;
    }

    public static int len(byte[] v) {
        return v.length;
    }

    public static int len(char[] v) {
        return v.length;
    }

    public static int len(CharSequence cs) {
        return cs.length();
    }

    public static int len(Object obj) {
        if (isArray(obj)) {
            return arrayLength(obj);
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        } else if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        } else {
            throw new AssertionException("Not an array like object");
        }
    }

    public static int arrayLength(Object obj) {
        return Reflection.arrayLength(obj);
    }

    public static boolean isArray(Object obj) {
        if (obj == null) {
            return false;
        }
        return Reflection.isArray(obj);
    }


    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            handleUnexpectedException(e);
        }
    }

    public static void handleUnexpectedException(Exception ex) {
        throw new AssertionException(ex);
    }

    public static void handleUnexpectedException(String msg, Exception ex) {
        throw new AssertionException(msg, ex);
    }


    @SuppressWarnings({"rawtypes"})
    public static <T> T get(Class<T> clz, Object map, Object key) {
        if (map instanceof Map) {
            return get(clz, (Map) map, key);
        } else {
            return null;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T get(Class<T> clz, Map map, Object key) {
        Object value = map.get(key.toString());
        if (value == null) {
            return (T) value;
        }
        if (value.getClass() != clz) {
            T t = Types.coerce(clz, value);
            return t;
        } else {
            return (T) value;
        }
    }

    public static String trimStart(String arg, String delim) {
        if (arg.startsWith(delim)) {
            return arg.substring(delim.length(), arg.length());
        } else {
            return arg;
        }
    }


    public static Appendable buf() {
        return new StringBuilder();
    }

    public static Appendable buf(int capacity) {
        return new StringBuilder(capacity);
    }

    public static String upper(String str) {
        return str.toUpperCase();
    }

    public static String lower(String str) {
        return str.toLowerCase();
    }


    public static void add(Appendable buf, CharSequence... cs) {
        try {
            for (CharSequence c : cs) {
                buf.append(c);
            }
        } catch (IOException e) {
            handleUnexpectedException(e);
        }
    }

    public static void add(Appendable buf, char c) {
        try {
            buf.append(c);
        } catch (IOException e) {
            handleUnexpectedException(e);
        }
    }

    public static <T> void add(List<T> list, T o) {
        list.add(o);
    }


    public static boolean not(boolean test) {
        return !test;
    }

    public static boolean and(boolean... tests) {
        for (boolean test : tests) {
            if (!test) {
                return test;
            }
        }
        return true;
    }

    public static boolean or(boolean... tests) {
        for (boolean test : tests) {
            if (test) {
                return test;
            }
        }
        return false;
    }

    public static class Pair<T> {

        private T first;
        private T second;
        private T[] both = (T[]) new Object[2];

        public Pair() {
        }

        public Pair(T f, T s) {
            this.first = f;
            this.second = s;
            both[0] = f;
            both[1] = s;
        }


        public T getFirst() {
            return first;
        }

        public T getSecond() {
            return second;
        }


        public T[] getBoth() {
            return both;
        }

        public void setFirst(T first) {
            this.first = first;
            both[0] = first;

        }

        public void setSecond(T second) {
            this.second = second;
            both[1] = second;

        }

        public void setBoth(T[] both) {
            this.both = both;
            this.first = both[0];
            this.second = both[1];

        }


    }


    public static Iterator iterator(final Object o) {
        if (o instanceof Collection) {
            return ((Collection) o).iterator();
        } else if (isArray(o)) {
            return new Iterator() {
                int index = 0;
                int length = len(o);

                @Override
                public boolean hasNext() {
                    return index < length;
                }

                @Override
                public Object next() {
                    Object value = Reflection.idx(o, index);
                    index++;
                    return value;
                }

                @Override
                public void remove() {
                }
            };
        }
        return null;
    }

    public static Object unifyList(Object o) {
        return unifyList(o, null);
    }

    public static Object unifyList(Object o, List list) {

        if (list == null && !isArray(o) && !(o instanceof Iterable)) {
            return o;
        }

        if (list == null) {
            list = new ArrayList(400);
        }
        if (isArray(o)) {
            int length = len(o);
            for (int index = 0; index < length; index++) {
                unifyList(Reflection.idx(o, index), list);
            }
        } else if (o instanceof Iterable) {
            Iterable i = ((Iterable) o);
            for (Object item : i) {
                list = (List) unifyList(item, list);
            }
        } else {
            list.add(o);
        }

        return list;


    }

    public static Date date(String string) {
        return Types.toDateUS(string);
    }

    public static Date euroDate(String string) {
        return Types.toEuroDate(string);
    }

    public static String camelCaseUpper(String in) {
        return camelCase(in, true);
    }


    public static String camelCaseLower(String in) {
        return camelCase(in, false);
    }

    public static String camelCase(String in) {
        return camelCase(in, false);
    }

    public static Function<String, String> underBarCase = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return Utils.underBarCase(in);
        }
    };
    public static Function<String, String> camelCase = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return Utils.camelCase(in);
        }
    };


    public static Function<String, String> camelCaseUpper = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return Utils.camelCaseUpper(in);
        }
    };


    public static Function<String, String> camelCaseLower = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return Utils.camelCaseLower(in);
        }
    };

    public static Function<String, String> upperCase = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return in.toUpperCase();
        }
    };

    public static Function<String, String> lowerCase = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return in.toLowerCase();
        }
    };

    public static String camelCase(String inStr, boolean upper) {
        char[] in = chars(inStr);
        char[] out = camelCase(in, upper);
        return str(out);
    }

    public static char[] camelCase(char[] in, boolean upper) {

        if (in == null || in.length == 0 || in.length == 1) {
            return in;
        }

        char[] out = null;
        int count = 0;
        for (int index = 0; index < in.length; index++) {
            char ch = in[index];
            if (ch == '_' || ch == ' ' || ch == '\t') {
                count++;
            }
        }

        out = new char[in.length - count];


        boolean upperNext = false;

        for (int index = 0, secondIndex = 0; index < in.length; index++) {
            char ch = in[index];
            if (ch == '_' || ch == ' ' || ch == '\t') {
                upperNext = true;
            } else {
                out[secondIndex] = upperNext ? Character.toUpperCase(ch) : Character.toLowerCase(ch);
                upperNext = false;
                secondIndex++;
            }
        }

        if (upper) {
            out[0] = Character.toUpperCase(out[0]);
        } else {
            out[0] = Character.toLowerCase(out[0]);
        }

        return out;
    }


    public static String underBarCase(String inStr) {
        char[] in = chars(inStr);
        char[] out = underBarCase(in);
        return str(out);
    }

    public static char[] underBarCase(char[] in) {

        if (in == null || in.length == 0 || in.length == 1) {
            return in;
        }

        char[] out = null;
        int count = 0;

        boolean wasLower = false;

        for (int index = 0; index < in.length; index++) {
            char ch = in[index];
            boolean isUpper;

            isUpper = Character.isUpperCase(ch);

            if (wasLower && isUpper) {
                count++;
            }

            wasLower = Character.isLowerCase(ch);

        }

        out = new char[in.length + count];

        wasLower = false;

        for (int index = 0, secondIndex = 0; index < in.length; index++, secondIndex++) {
            char ch = in[index];
            boolean isUpper;

            isUpper = Character.isUpperCase(ch);

            if (wasLower && isUpper) {
                out[secondIndex] = '_';
                secondIndex++;
            }

            if (ch == ' ' || ch == '-' || ch == '\t') {
                out[secondIndex] = '_';
            } else {
                out[secondIndex] = Character.toUpperCase(ch);
            }
            wasLower = Character.isLowerCase(ch);

        }

        return out;
    }


    public static void main(String[] args) {
    }

}
