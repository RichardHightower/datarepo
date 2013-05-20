package org.datarepo;

import org.datarepo.reflection.Reflection;
import org.datarepo.reflection.Types;

import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Date;

public class Utils {
    static Class<Utils> utils = Utils.class;

    private static final Logger log = log(utils);

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
        return quote+str+quote;
    }

    public final static String singleQuote = "'";

    public static String singleQuote(String str) {
        return singleQuote+str+singleQuote;
    }

    public final static String comma = ",";

    public final static String comma(String str) {
        return str+comma;
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
        if (items!=null && items.length==1) {
            System.out.println(items[0]);
        } else {
            print ((Object[] )items);
        }
    }
    public static void print(Object... items) {
        System.out.println(sprint(items));
    }
    public static void fprint(Logger log, Object... items) {
        log.info(sprint(items));
    }
    public static void fprint(Appendable appendable, Object... items) {
        add(appendable, sprint(items) + "\n");
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
        return sprint((Object [])items);
    }
    public static String sprint(Object... items) {
        if (items == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(256);
        for (Object item : items) {
            if ( item!=null && isArray(item)) {
                builder.append(ls((Object[])item));
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
            if(!isEqual(it, right.get(index))){
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
        if (array==null) {
            return null;
        }
        ArrayList<V> list = new ArrayList<V>(array.length);
        for (V o : array) {
            list.add(o);
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
        return new HashMap<K, V>(map);
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
        HashMap<K, V> map = new HashMap<K, V>(10);
        map.put(k0, v0);
        return map;
    }

    public static <K, V> Map<K, V> mp(Class<K> key, Class<V> v) {
        HashMap<K, V> map = new HashMap<K, V>(10);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1) {
        HashMap<K, V> map = new HashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2) {
        HashMap<K, V> map = new HashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3) {
        HashMap<K, V> map = new HashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3, K k4, V v4) {
        HashMap<K, V> map = new HashMap<K, V>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
                                      V v3, K k4, V v4, K k5, V v5) {
        HashMap<K, V> map = new HashMap<K, V>(10);
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
        HashMap<K, V> map = new HashMap<K, V>(10);
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
        HashMap<K, V> map = new HashMap<K, V>(10);
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
        HashMap<K, V> map = new HashMap<K, V>(10);
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
        HashMap<K, V> map = new HashMap<K, V>(10);
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
        HashMap<K, V> map = new HashMap<K, V>(10 + entries.length);
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
        HashMap<K, V> map = new HashMap<K, V>(10 + keys.size());
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
        HashMap<String, V> map = new HashMap<String, V>(values.size());
        Iterator<V> iterator = values.iterator();
        for (V v : values) {
            String key = Reflection.getProperty(string, v, propertyKey);
            map.put(key, v);
        }
        return map;
    }

    public static <K, V> Map<K, V> mp(K[] keys, V[] values) {
        HashMap<K, V> map = new HashMap<K, V>(10 + keys.length);
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
        HashMap<K, V> map = new HashMap<K, V>(entries.length);
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
            if (converted!=null) {
                toList.add(converted);
            }
        }

        return toList;
    }


    public static String lns(String... lines) {
        return lines(lines);
    }

    public static String lines(String... lines) {
        return join('\n', (Object[]) lines);
    }

    public static String joinByString(String delim, Object[] args) {
        StringBuilder builder = new StringBuilder(256);
        int index=0;

        for (Object arg : args) {
            builder.append(str(arg));
            builder.append(delim);
            if (!(index==args.length-1)){
                builder.append(delim);
            }
            index++;
        }
        return builder.toString();
    }

    public static String join(char delim, String... args) {
        return join(delim, (Object[])args);
    }
    public static String joinBy(char delim, Object... args) {
        StringBuilder builder = new StringBuilder(256);

        if (args.length==1 && isArray(args[0])) {
            Object array = args[0];
            for (int index = 0; index < len(array); index++) {
                Object obj = Reflection.idx(array, index);
                builder.append(obj.toString());
                if (!(index==args.length-1)){
                    builder.append(delim);
                }

            }
        } else  {
            int index=0;
            for (Object arg : args) {
                builder.append(arg.toString());
                if (!(index==args.length-1)){
                    builder.append(delim);
                }
                index++;
            }
        }
        return builder.toString();
    }

    public static String join(char delim, Integer... args) {
        return join(delim, (Object[])args);
    }
    public static String join(Integer... args) {
        return join((Object[])args);
    }
    public static String join(String... args) {
        return join((Object[])args);
    }
    public static String join(Object... args) {

        if (args.length==2) {
            Object arg1 = args[0];
            Object arg2 = args[1];

            if (arg2.getClass()==Character.class) {
                if (isArray(arg1)) {
                    return join((Character)arg2, (Object[])arg1);
                } else if (arg1 instanceof Collection) {
                    return join((Character)arg2, (Collection<?>)arg1);
                }
            } else if (arg2.getClass()==String.class) {
                if (isArray(arg1)) {
                    return join((String)arg2, (Object[])arg1);
                } else if (arg1 instanceof Collection) {
                    return join((String)arg2, (Collection<?>)arg1);
                }
            }
        }
        StringBuilder builder = new StringBuilder(256);
        for (Object arg : args) {
            builder.append(arg.toString());
        }
        return builder.toString();
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

        int index=0;
        for (Object arg : args) {
            builder.append(arg.toString());
            builder.append(delim);
            if (!(index==args.size()-1)){
                builder.append(delim);
            }
            index++;

        }
        return builder.toString();
    }
    public static String join(String delim, Collection<?> args) {
        StringBuilder builder = new StringBuilder(256);
        int index=0;
        for (Object arg : args) {
            builder.append(arg.toString());
            builder.append(delim);
            if (!(index==args.size()-1)){
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
        if (value>d1 && value < d2) {

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
        String[] strings = split(array,c);
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

    public static String trim (String str) {
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
            return chars((String)obj);
        } else if (obj instanceof StringBuilder) {
            return chars((StringBuilder)obj);
        } else if (obj instanceof CharSequence){
            return chars((CharSequence)obj);
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
            char [] chars = new char[length];
            str.getChars(0, length, chars, length);
            return chars;
        } else {
            return new char[0];
        }
    }


    public static char[] chars(char... chars) {
        return str(chars).toCharArray();
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
        return org.datarepo.reflection.Types.toString(obj);
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

    public static <T> boolean isIn(T t1, Set <T> set) {
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

    public static List<Character> list(char... chars) {
        return ls(chars);
    }

    public static char[] add(char[] chars, char... chars2) {
        List<Character> list = ls(chars);
        list.addAll(ls(chars2));
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
    public static List<Character> ls(char... chars) {
        List<Character> ls = new ArrayList<Character>();
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

    public static void complain(String msg) {
        throw new UnsupportedOperationException(msg);
    }


    public static double sum(double...items) {
        double total = 0d;
        for (int index=0 ; index < items.length; index++){
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
    public static int len (Map<?, ?> map) {
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
            return ((CharSequence)obj).length();
        }else if (obj instanceof Collection) {
            return ((Collection<?>)obj).size();
        }else if (obj instanceof Map) {
            return ((Map<?,?>)obj).size();
        } else {
            throw new AssertionException("Not an array like object");
        }
    }

    public static int arrayLength(Object obj) {
        return org.datarepo.reflection.Reflection.arrayLength(obj);
    }

    public static boolean isArray(Object obj) {
        return org.datarepo.reflection.Reflection.isArray(obj);
    }


    public static void rest(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            handle(e);
        }
    }

    private static void handle(Exception ex) {
        throw new AssertionException(ex);
    }


    @SuppressWarnings({"rawtypes" })
    public static <T> T get(Class<T> clz, Object map, Object key) {
        if (map instanceof Map) {
            return get(clz, (Map)map, key);
        } else {
            return null;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T get(Class<T> clz, Map map, Object key) {
        Object value =  map.get(key.toString());
        if (value==null) {
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
        }else {
            return arg;
        }
    }


    public static Appendable buf () {
        return new StringBuilder();
    }
    public static Appendable buf (int capacity) {
        return new StringBuilder(capacity);
    }

    public static String upper (String str) {
        return str.toUpperCase();
    }

    public static String lower (String str) {
        return str.toLowerCase();
    }


    public static void add (Appendable buf, CharSequence... cs) {
        try {
            for (CharSequence c : cs) {
                buf.append(c);
            }
        } catch (IOException e) {
            handle(e);
        }
    }
    public static void add (Appendable buf, char c) {
        try {
            buf.append(c);
        } catch (IOException e) {
            handle(e);
        }
    }
    public static <T> void add (List<T> list, T o) {
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




}
