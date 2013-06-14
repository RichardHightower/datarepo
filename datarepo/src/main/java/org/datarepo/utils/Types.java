package org.datarepo.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.*;

public class Types {
    static Class<Types> types = Types.class;

    private static final Logger log = Logger.getLogger(Types.class.getName());

    public static int toInt(byte[] bytes, int offset) {

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes, offset,
                bytes.length);
        DataInputStream instream = new DataInputStream(bis);
        try {
            return instream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static int toInt(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream instream = new DataInputStream(bis);
        try {
            return instream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int toInt(Object obj) {
        if (obj.getClass() == int.class) {
            return (Integer) obj;
        }
        try {
            if (obj instanceof Number) {
                return ((Number) obj).intValue();
            } else if (obj instanceof CharSequence) {
                try {
                    return Integer.parseInt(((CharSequence) obj).toString());
                } catch (Exception ex) {
                    char[] chars = chars(obj);
                    Appendable buf = buf(chars.length);
                    boolean found = false;
                    for (char c : chars) {
                        if (Character.isDigit(c) && !found) {
                            found = true;
                            add(buf, c);
                        } else if (Character.isDigit(c) && found) {
                            add(buf, c);
                        } else if (!Character.isDigit(c) && found) {
                        }
                    }
                    try {
                        if (len(buf) > 0) {
                            return Integer.parseInt(str(buf));
                        }
                    } catch (Exception ex2) {
                        warning(log,
                                ex,
                                "unable to convert to int and there was an exception %s",
                                ex.getMessage());
                    }
                }
            } else {
            }
        } catch (Exception ex) {
            warning(log, ex,
                    "unable to convert to int and there was an exception %s",
                    ex.getMessage());
        }
        die("Unable to convert %s to a int", obj.getClass());
        return -666; // die throws an exception

    }

    public static byte toByte(Object obj) {
        if (obj.getClass() == byte.class) {
            return (Byte) obj;
        }
        try {
            if (obj instanceof Number) {
                return ((Number) obj).byteValue();
            } else if (obj instanceof CharSequence) {
                try {
                    return Byte.parseByte(((CharSequence) obj).toString());
                } catch (Exception ex) {
                    char[] chars = chars(obj);
                    Appendable buf = buf(chars.length);
                    boolean found = false;
                    for (char c : chars) {
                        if (Character.isDigit(c) && !found) {
                            found = true;
                            add(buf, c);
                        } else if (Character.isDigit(c) && found) {
                            add(buf, c);
                        } else if (!Character.isDigit(c) && found) {
                        }
                    }
                    try {
                        if (len(buf) > 0) {
                            return Byte.parseByte(str(buf));
                        }
                    } catch (Exception ex2) {
                        warning(log,
                                ex,
                                "unable to convert to byte and there was an exception %s",
                                ex.getMessage());
                    }
                }
            } else {
            }
        } catch (Exception ex) {
            warning(log, ex,
                    "unable to convert to byte and there was an exception %s",
                    ex.getMessage());
        }
        die("Unable to convert %s to a byte", obj.getClass());
        return -66; // die throws an exception

    }

    public static short toShort(Object obj) {

        if (obj.getClass() == short.class) {
            return (Short) obj;
        }

        try {
            if (obj instanceof Number) {
                return ((Number) obj).shortValue();
            } else if (obj instanceof CharSequence) {
                try {
                    return Short.parseShort(((CharSequence) obj).toString());
                } catch (Exception ex) {
                    char[] chars = chars(obj);
                    Appendable buf = buf(chars.length);
                    boolean found = false;
                    for (char c : chars) {
                        if (Character.isDigit(c) && !found) {
                            found = true;
                            add(buf, c);
                        } else if (Character.isDigit(c) && found) {
                            add(buf, c);
                        } else if (!Character.isDigit(c) && found) {
                        }
                    }
                    try {
                        if (len(buf) > 0) {
                            return Short.parseShort(str(buf));
                        }
                    } catch (Exception ex2) {
                        warning(log,
                                ex,
                                "unable to convert to short and there was an exception %s",
                                ex.getMessage());
                    }
                }
            } else {
            }
        } catch (Exception ex) {
            warning(log, ex,
                    "unable to convert to byte and there was an exception %s",
                    ex.getMessage());
        }
        die("Unable to convert %s to a short", obj.getClass());
        return -66; // die throws an exception

    }

    public static char toChar(Object obj) {
        if (obj.getClass() == char.class) {
            return (Character) obj;
        }

        try {
            if (obj instanceof Character) {
                return ((Character) obj).charValue();
            } else if (obj instanceof CharSequence) {
                obj.toString().charAt(0);
            } else {
            }
        } catch (Exception ex) {
            warning(log, ex,
                    "unable to convert to byte and there was an exception %s",
                    ex.getMessage());
        }
        die("Unable to convert %s to a byte", obj.getClass());
        return 'Z'; // die throws an exception

    }

    public static long toLong(Object obj) {

        if (obj.getClass() == long.class) {
            return (Long) obj;
        }

        try {
            if (obj instanceof Number) {
                return ((Number) obj).longValue();
            } else if (obj instanceof CharSequence) {
                try {
                    return Long.parseLong(((CharSequence) obj).toString());
                } catch (Exception ex) {
                    char[] chars = chars(obj);
                    Appendable buf = buf(chars.length);
                    boolean found = false;
                    for (char c : chars) {
                        if (Character.isDigit(c) && !found) {
                            found = true;
                            add(buf, c);
                        } else if (Character.isDigit(c) && found) {
                            add(buf, c);
                        } else if (!Character.isDigit(c) && found) {
                        }
                    }
                    try {
                        if (len(buf) > 0) {
                            return Long.parseLong(str(buf));
                        }
                    } catch (Exception ex2) {
                        warning(log,
                                ex,
                                "unable to convert to long and there was an exception %s",
                                ex.getMessage());

                    }
                }
            } else {
            }
        } catch (Exception ex) {
            warning(log, ex,
                    "unable to convert to long and there was an exception %s",
                    ex.getMessage());

        }

        die("Unable to convert %s %s to a long", obj, obj.getClass());
        return -666; // die throws an exception

    }

    public static boolean toBoolean(Object obj) {

        if (obj.getClass() == boolean.class) {
            return (Boolean) obj;
        }


        Set<String> trueSet = set("t", "true", "True", "y", "yes", "1", "aye",
                "ofcourse", "T", "TRUE", "ok");
        if (obj instanceof String || obj instanceof CharSequence
                || obj.getClass() == char[].class) {
            String str = str(obj);
            if (str.length() == 0) {
                return false;
            } else {
                return isIn(str, trueSet);
            }
        } else if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        } else if (isArray(obj) || obj instanceof Collection) {
            return len(obj) > 0;
        } else {
            return toBoolean(str(obj));
        }
    }

    public static double toDouble(Object obj) {
        if (obj.getClass() == double.class) {
            return (Double) obj;
        }

        try {
            if (obj instanceof Double) {
                return (Double) obj;
            } else if (obj instanceof Number) {
                return ((Number) obj).doubleValue();
            } else if (obj instanceof CharSequence) {
                try {
                    return Double.parseDouble(((CharSequence) obj).toString());
                } catch (Exception ex) {
//                    String svalue = str(obj);
//                    Matcher re = Regex.re(
//                            "[-+]?[0-9]+\\.?[0-9]+([eE][-+]?[0-9]+)?", svalue);
//                    if (re.find()) {
//                        svalue = re.group(0);
//                        return Double.parseDouble(svalue);
//                    }
                    complain("unable to convert to double");
                    return Double.NaN;
                }
            } else {
            }
        } catch (Exception ex) {
            warning(log,
                    ex,
                    "unable to convert to double and there was an exception %s",
                    ex.getMessage());
        }

        die("Unable to convert %s to a double", obj.getClass());
        return -666d; // die throws an exception

    }

    public static float toFloat(Object obj) {
        if (obj.getClass() == float.class) {
            return (Float) obj;
        }

        try {
            if (obj instanceof Float) {
                return (Float) obj;
            } else if (obj instanceof Number) {
                return ((Number) obj).floatValue();
            } else if (obj instanceof CharSequence) {
                try {
                    return Float.parseFloat(((CharSequence) obj).toString());
                } catch (Exception ex) {
//                    String svalue = str(obj);
//                    Matcher re = Regex.re(
//                            "[-+]?[0-9]+\\.?[0-9]+([eE][-+]?[0-9]+)?", svalue);
//                    if (re.find()) {
//                        svalue = re.group(0);
//                        return Float.parseFloat(svalue);
//                    }
                    complain("unable to convert to float after regex");
                    return Float.NaN;
                }
            } else {
            }
        } catch (Exception ex) {
            warning(log,
                    "unable to convert to float and there was an exception %s",
                    ex.getMessage());
        }

        die("Unable to convert %s to a float", obj.getClass());
        return -666f; // die throws an exception

    }

    @SuppressWarnings("unchecked")
    public static <T> T coerce(Class<T> clz, Object value) {
        if (clz == integer || clz == pint) {
            Integer i = toInt(value);
            return (T) i;
        } else if (clz == lng || clz == plong) {
            Long l = toLong(value);
            return (T) l;
        } else if (clz == dbl || clz == pdouble) {
            Double i = toDouble(value);
            return (T) i;
        } else if (clz == flt || clz == pfloat) {
            Float i = toFloat(value);
            return (T) i;
        } else if (clz == sarray) {
            return (T) toStringArray(value);
        } else if (clz == bool || clz == pboolean) {
            Boolean b = toBoolean(value);
            return (T) b;
        } else if (clz == fileT) {
            //return (T) toFile(set);
            complain("Need to fix this");
            return null;
        } else if (isMap(clz)) {
            if (value instanceof Map) {
                return (T) value;
            }
            return (T) toMap(value);
        } else if (clz.isArray()) {
            return (T) toPrimitiveArrayIfPossible(clz, value);
        } else if (isCollection(clz)) {
            return toCollection(clz, value);
        } else if (clz != null && clz.getPackage() != null && !clz.getPackage().getName().startsWith("java")
                && isMap(value.getClass()) && isKeyTypeString(value)) {
            return (T) Reflection.fromMap((Map<String, Object>) value);
        } else {
            return (T) value;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toPrimitiveArrayIfPossible(Class<T> clz, Object value) {
        if (clz == intA) {
            return (T) iarray(value);
        } else if (clz == byteA) {
            return (T) barray(value);
        } else if (clz == charA) {
            return (T) carray(value);
        } else if (clz == shortA) {
            return (T) sarray(value);
        } else if (clz == longA) {
            return (T) larray(value);
        } else if (clz == floatA) {
            return (T) farray(value);
        } else if (clz == doubleA) {
            return (T) darray(value);
        } else if (value.getClass() == clz) {
            return (T) value;
        } else {
            int index = 0;
            Object newInstance = Array.newInstance(clz.getComponentType(), len(value));
            Iterator<Object> iterator = iterator(object, value);
            while (iterator.hasNext()) {
                idx(newInstance, index, iterator.next());
                index++;
            }
            return (T) newInstance;
        }
    }


    public static double[] darray(Object value) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if (value.getClass() == shortA) {
            return (double[]) value;
        }
        double[] values = new double[len(value)];
        int index = 0;
        Iterator<Object> iterator = iterator(Object.class, value);
        while (iterator.hasNext()) {
            values[index] = toFloat(iterator.next());
            index++;
        }
        return values;
    }

    public static float[] farray(Object value) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if (value.getClass() == floatA) {
            return (float[]) value;
        }
        float[] values = new float[len(value)];
        int index = 0;
        Iterator<Object> iterator = iterator(Object.class, value);
        while (iterator.hasNext()) {
            values[index] = toFloat(iterator.next());
            index++;
        }
        return values;
    }

    public static long[] larray(Object value) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if (value.getClass() == shortA) {
            return (long[]) value;
        }
        long[] values = new long[len(value)];
        int index = 0;
        Iterator<Object> iterator = iterator(Object.class, value);
        while (iterator.hasNext()) {
            values[index] = toLong(iterator.next());
            index++;
        }
        return values;
    }

    public static short[] sarray(Object value) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if (value.getClass() == shortA) {
            return (short[]) value;
        }
        short[] values = new short[len(value)];
        int index = 0;
        Iterator<Object> iterator = iterator(Object.class, value);
        while (iterator.hasNext()) {
            values[index] = toShort(iterator.next());
            index++;
        }
        return values;
    }

    public static int[] iarray(Object value) {
        //You could handleUnexpectedException shorts, bytes, longs and chars more efficiently
        if (value.getClass() == intA) {
            return (int[]) value;
        }
        int[] values = new int[len(value)];
        int index = 0;
        Iterator<Object> iterator = iterator(Object.class, value);
        while (iterator.hasNext()) {
            values[index] = toInt(iterator.next());
            index++;
        }
        return values;
    }

    public static byte[] barray(Object value) {
        //You could handleUnexpectedException shorts, ints, longs and chars more efficiently
        if (value.getClass() == byteA) {
            return (byte[]) value;
        }
        byte[] values = new byte[len(value)];
        int index = 0;
        Iterator<Object> iterator = iterator(Object.class, value);
        while (iterator.hasNext()) {
            values[index] = toByte(iterator.next());
            index++;
        }
        return values;
    }

    public static char[] carray(Object value) {
        //You could handleUnexpectedException shorts, ints, longs and chars more efficiently
        if (value.getClass() == charA) {
            return (char[]) value;
        }
        char[] values = new char[len(value)];
        int index = 0;
        Iterator<Object> iterator = iterator(object, value);
        while (iterator.hasNext()) {
            values[index] = toChar(iterator.next());
            index++;
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> iterator(Class<T> class1, final Object value) {


        if (isArray(value)) {
            final int length = arrayLength(value);

            return new Iterator<T>() {
                int i = 0;

                @Override
                public boolean hasNext() {
                    return i < length;
                }

                @Override
                public T next() {
                    T next = (T) Reflection.idx(value, i);
                    i++;
                    return next;
                }

                @Override
                public void remove() {
                }
            };
        } else if (isCollection(value.getClass())) {
            return ((Collection<T>) value).iterator();
        } else if (isMap(value.getClass())) {
            Iterator<T> iterator = ((Map<String, T>) value).values().iterator();
            return iterator;
        } else {
            return (Iterator<T>) Collections.singleton(value).iterator();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toCollection(Class<T> clz, Object value) {
        if (isList(clz)) {
            return (T) toList(value);
        } else if (isSortedSet(clz)) {
            return (T) toSortedSet(value);
        } else if (isSet(clz)) {
            return (T) toSet(value);
        } else {
            return (T) toList(value);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List toList(Object value) {
        if (value instanceof List) {
            return (List) value;
        } else if (value instanceof Collection) {
            return new ArrayList((Collection) value);
        } else {
            ArrayList list = new ArrayList(len(value));
            Iterator<Object> iterator = iterator(object, value);
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
            return list;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Set toSet(Object value) {
        if (value instanceof Set) {
            return (Set) value;
        } else if (value instanceof Collection) {
            return new HashSet((Collection) value);
        } else {
            HashSet set = new HashSet(len(value));
            Iterator<Object> iterator = iterator(object, value);
            while (iterator.hasNext()) {
                set.add(iterator.next());
            }
            return set;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static SortedSet toSortedSet(Object value) {
        if (value instanceof Set) {
            return (SortedSet) value;
        } else if (value instanceof Collection) {
            return new TreeSet((Collection) value);
        } else {
            TreeSet set = new TreeSet();
            Iterator<Object> iterator = iterator(object, value);
            while (iterator.hasNext()) {
                set.add(iterator.next());
            }
            return set;
        }
    }


    public static boolean isKeyTypeString(Object value) {
        return getKeyType((Map<?, ?>) value) == string;
    }

    public static Map<String, Object> toMap(Object value) {
        return Reflection.toMap(value);
    }

//    public static File toFile(Object set) {
//        if (set instanceof File) {
//            return (File) set;
//        } else if (set instanceof CharSequence) {
//            return file(str(set));
//        } else {
//            return toFile(set.toString());
//        }
//    }

    public static String[] toStringArray(Object value) {
        if (value instanceof CharSequence) {
            String str = toString(value);

            List<Character> delims = charList(new char[]{',', '\t', ' ', '|', ':', ';'});
            char[] chars = chars(str);
            for (char c : delims) {
                if (isIn(c, chars)) {
                    return split(chars, c);
                }
            }

        } else if (value instanceof List) {
            // complete this
        }

        return null;
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return (String) obj;
        } else {
            return obj.toString();
        }
    }

    public static Number toWrapper(long l) {
        if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
            return toWrapper((int) l);
        } else {
            return Long.valueOf(l);
        }
    }

    public static Number toWrapper(int i) {
        if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
            return Byte.valueOf((byte) i);
        } else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
            return Short.valueOf((short) i);
        } else {
            return Integer.valueOf(i);
        }
    }

    public static Object wrapAsObject(boolean i) {
        return Boolean.valueOf(i);
    }


    public static Object wrapAsObject(byte i) {
        return Byte.valueOf(i);
    }

    public static Object wrapAsObject(short i) {
        return Short.valueOf(i);
    }

    public static Object wrapAsObject(int i) {
        return Integer.valueOf(i);
    }

    public static Object wrapAsObject(long i) {
        return Long.valueOf(i);
    }

    public static Object wrapAsObject(double i) {
        return Double.valueOf(i);
    }

    public static Object wrapAsObject(float i) {
        return Float.valueOf(i);
    }

    public static boolean isBasicType(Object value) {
        return (value instanceof Number || value instanceof CharSequence
                || value instanceof Date || value instanceof Calendar);
    }

    public static boolean isBasicType(Class<?> theClass) {
        return (number.isAssignableFrom(theClass)
                || chars.isAssignableFrom(theClass)
                || date.isAssignableFrom(theClass)
                || calendar.isAssignableFrom(theClass) || theClass
                .isPrimitive());
    }

    public static boolean isMap(Class<?> thisType) {
        return implementsInterface(thisType, Map.class);
    }

    public static boolean isCharSequence(Class<?> thisType) {
        return implementsInterface(thisType, CharSequence.class);
    }

    public static boolean isCollection(Class<?> thisType) {
        return implementsInterface(thisType, Collection.class);
    }

    public static boolean isList(Class<?> thisType) {
        return implementsInterface(thisType, List.class);
    }

    public static boolean isSet(Class<?> thisType) {
        return implementsInterface(thisType, Set.class);
    }

    public static boolean isSortedSet(Class<?> thisType) {
        return implementsInterface(thisType, SortedSet.class);
    }

    public static boolean isType(Class<?> thisType, Class<?> isThisType) {
        return isSuperType(thisType, isThisType);
    }

    public static boolean isModifiableCollection(Collection<Object> value) {
        try {
            value.clear();
        } catch (Exception ex) {
            return false;
        }

        @SuppressWarnings("rawtypes")
        Class<? extends Collection> clazz = value.getClass();

        if (clazz == HashSet.class || clazz == TreeSet.class
                || clazz == ArrayList.class || clazz == LinkedList.class) {
            return true;
        } else {
            return false;
        }
    }

    public static Class<?> getKeyType(Map<?, ?> value) {
        if (value.size() > 0) {
            return value.keySet().iterator().next().getClass();
        } else {
            return null;
        }
    }

    public static Object toArrayGuessType(Collection<?> value) {
        Class<?> componentType = Reflection.getComponentType(value);
        Object array = Array.newInstance(componentType, value.size());
        @SuppressWarnings("unchecked")
        Iterator<Object> iterator = (Iterator<Object>) value.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            idx(array, index, iterator.next());
            index++;
        }
        return array;
    }


    public static Object toArray(Class<?> componentType, Collection<?> value) {
        Object array = Array.newInstance(componentType, value.size());
        @SuppressWarnings("unchecked")
        Iterator<Object> iterator = (Iterator<Object>) value.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            idx(array, index, iterator.next());
            index++;
        }
        return array;
    }


    public static Date toDate(Calendar c) {
        return c.getTime();

    }

    public static Date toDateUS(String string) {

        String[] split = splitByArray(string.toCharArray(), new char[]{'.', '\\', '/', ':'});

        if (split.length == 3) {
            return getUSDate(toInt(split[0]), toInt(split[1]), toInt(split[2]));
        } else if (split.length == 6) {
            return getUSDate(toInt(split[0]), toInt(split[1]), toInt(split[2]),
                    toInt(split[3]), toInt(split[4]), toInt(split[5])
            );
        } else {
            die("Not able to parse %s into a US date", string);
            return null;
        }

    }

    public static Date toEuroDate(String string) {

        String[] split = splitByArray(string.toCharArray(), new char[]{'.', '\\', '/', ':'});

        if (split.length == 3) {
            return getEuroDate(toInt(split[0]), toInt(split[1]), toInt(split[2]));
        } else if (split.length == 6) {
            return getEuroDate(toInt(split[0]), toInt(split[1]), toInt(split[2]),
                    toInt(split[3]), toInt(split[4]), toInt(split[5])
            );
        } else {
            die("Not able to parse %s into a US date", string);
            return null;
        }

    }


    public static Date year(int year) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.set(1970, Calendar.JANUARY, 2, 0, 0, 0);
        c.set(Calendar.YEAR, year);
        return c.getTime();
    }

    public static Date getUSDate(int month, int day, int year) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.set(year, month - 1, day + 1, 0, 0, 0);
        return c.getTime();
    }


    public static Date getUSDate(int month, int day, int year, int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.set(year, month - 1, day + 1, hour, minute, second);
        return c.getTime();
    }

    public static Date getEuroDate(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.set(year, month - 1, day + 1, 0, 0, 0);
        return c.getTime();
    }

    public static Date getEuroDate(int day, int month, int year, int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.set(year, month - 1, day + 1, hour, minute, second);
        return c.getTime();
    }

    public static void main(String[] args) {
        print(getUSDate(5, 29, 1970));
        print(getUSDate(8, 15, 1985));

        print(toDateUS("5/29/1970"));

        print(year(2013));

    }

    public static Date toDate(long value) {
        return new Date(value);
    }

    public static Date toDate(Long value) {
        return new Date(value);
    }

    public static Date toDate(String value) {
        try {
            return toDateUS(value);
        } catch (Exception ex) {
            try {
                return DateFormat.getDateInstance(DateFormat.SHORT).parse(value);
            } catch (ParseException e) {
                complain("Unable to parse date");
                return null;
            }

        }
    }

    public static Date toDate(Object value) {
        if (value instanceof Long) {
            return toDate((Long) value);
        } else if (value instanceof String) {
            return toDate((String) value);
        } else {
            if (value != null) {
                return toDate(value.toString());
            } else {
                complain("Unable to convert set to date");
                return null;
            }
        }
    }

    public static boolean isComparable(Object o) {
        return o instanceof Comparable;
    }

    public static boolean isComparable(Class<?> type) {
        return implementsInterface(type, Utils.comparable);
    }

    public static boolean isSuperClass(Class<?> type, Class<?> possibleSuperType) {
        if (possibleSuperType.isInterface()) {
            complain("That is not an class type, bad second argument");
            return false;
        } else {
            return possibleSuperType.isAssignableFrom(type);
        }

    }

    public static boolean isSuperType(Class<?> type, Class<?> possibleSuperType) {
        return possibleSuperType.isAssignableFrom(type);
    }

    public static boolean implementsInterface(Class<?> type, Class<?> interfaceType) {
        if (!interfaceType.isInterface()) {
            complain("That is not an interface type, bad second argument");
            return false;
        } else {
            return interfaceType.isAssignableFrom(type);
        }

    }

}
