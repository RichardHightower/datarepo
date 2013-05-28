package org.datarepo.utils;

import org.datarepo.reflection.FieldAccess;

import java.util.*;

import static org.datarepo.reflection.Reflection.*;

public class LinearSearch {


    public static <T> List<T> findBetween(Collection<T> items, String propertyName, Comparable a, Comparable b) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            Comparable value = (Comparable)field.getValue(item);
            if (a != null && b != null &&  value != null && value.compareTo(a) > 0 && value.compareTo(b) < 0) {
                results.add(item);
            }
        }
        return results;
    }


    public static <T> List<T> findNotEquals(Collection<T> items, String propertyName, boolean value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            boolean value2 = field.getBoolean(item);
            if (value != value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotEquals(Collection<T> items, String propertyName, Object value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            Object value2 = field.getValue(item);
            if (value != null && value2 != null && !value.equals(value2)) {
                results.add(item);
            }
        }
        return results;
    }


    public static <T> List<T> findNotEquals(Collection<T> items, String propertyName, byte value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            byte value2 = field.getByte(item);
            if (value != value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotEquals(Collection<T> items, String propertyName, short value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            short value2 = field.getShort(item);
            if (value != value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotEquals(Collection<T> items, String propertyName, int value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            int value2 = field.getInt(item);
            if (value != value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotEquals(Collection<T> items, String propertyName, long value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            long value2 = field.getLong(item);
            if (value != value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotEquals(Collection<T> items, String propertyName, float value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            float value2 = field.getFloat(item);
            if (value != value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotEquals(Collection<T> items, String propertyName, double value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {

            double value2 = field.getDouble(item);
            if (value != value2) {
                results.add(item);
            }
        }
        return results;
    }

    //
    //
    //
    //
    public static <T> List<T> findEquals(Collection<T> items, String propertyName, boolean value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            boolean value2 = field.getBoolean(item);
            if (value == value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findEquals(Collection<T> items, String propertyName, Object value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            Object value2 = field.getValue(item);
            if (value != null && value2 != null && value.equals(value2)) {
                results.add(item);
            }
        }
        return results;
    }


    public static <T> List<T> findEquals(Collection<T> items, String propertyName, byte value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            byte value2 = field.getByte(item);
            if (value == value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findEquals(Collection<T> items, String propertyName, short value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            short value2 = field.getShort(item);
            if (value == value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findEquals(Collection<T> items, String propertyName, int value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            int value2 = field.getInt(item);
            if (value == value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findEquals(Collection<T> items, String propertyName, long value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            long value2 = field.getLong(item);
            if (value == value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findEquals(Collection<T> items, String propertyName, float value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            float value2 = field.getFloat(item);
            if (value == value2) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findEquals(Collection<T> items, String propertyName, double value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {

            double value2 = field.getDouble(item);
            if (value == value2) {
                results.add(item);
            }
        }
        return results;
    }

    //
    //
    //
    //

    public static <T> List<T> findGreaterThan(Collection<T> items, String propertyName, Comparable value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            Comparable value2 = (Comparable) field.getValue(item);
            if (value2 != null && value != null && value2.compareTo(value) > 0) {
                results.add(item);
            }
        }
        return results;
    }


    public static <T> List<T> findGreaterThan(Collection<T> items, String propertyName, byte value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            byte value2 = field.getByte(item);
            if (value2 > value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThan(Collection<T> items, String propertyName, short value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            short value2 = field.getShort(item);
            if (value2 > value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThan(Collection<T> items, String propertyName, int value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            int value2 = field.getInt(item);
            if (value2 > value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThan(Collection<T> items, String propertyName, long value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            long value2 = field.getLong(item);
            if (value2 > value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThan(Collection<T> items, String propertyName, float value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            float value2 = field.getFloat(item);
            if (value2 > value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThan(Collection<T> items, String propertyName, double value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {

            double value2 = field.getDouble(item);
            if (value2 > value) {
                results.add(item);
            }
        }
        return results;
    }

    //
    //


    public static <T> List<T> findGreaterThanEqual(Collection<T> items, String propertyName, Comparable value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            Comparable value2 = (Comparable) field.getValue(item);
            if (value2 != null && value != null && value2.compareTo(value) >= 0) {
                results.add(item);
            }
        }
        return results;
    }


    public static <T> List<T> findGreaterThanEqual(Collection<T> items, String propertyName, byte value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            byte value2 = field.getByte(item);
            if (value2 >= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThanEqual(Collection<T> items, String propertyName, short value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            short value2 = field.getShort(item);
            if (value2 >= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThanEqual(Collection<T> items, String propertyName, int value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            int value2 = field.getInt(item);
            if (value2 >= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThanEqual(Collection<T> items, String propertyName, long value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            long value2 = field.getLong(item);
            if (value2 >= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThanEqual(Collection<T> items, String propertyName, float value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            float value2 = field.getFloat(item);
            if (value2 >= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findGreaterThanEqual(Collection<T> items, String propertyName, double value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {

            double value2 = field.getDouble(item);
            if (value2 >= value) {
                results.add(item);
            }
        }
        return results;
    }


    //Less than

    public static <T> List<T> findLessThan(Collection<T> items, String propertyName, Comparable value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            Comparable value2 = (Comparable) field.getValue(item);
            if (value2 != null && value != null && value2.compareTo(value) < 0) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThan(Collection<T> items, String propertyName, byte value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            byte value2 = field.getByte(item);
            if (value2 < value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThan(Collection<T> items, String propertyName, short value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            short value2 = field.getShort(item);
            if (value2 < value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThan(Collection<T> items, String propertyName, int value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            int value2 = field.getInt(item);
            if (value2 < value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThan(Collection<T> items, String propertyName, long value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            long value2 = field.getLong(item);
            if (value2 < value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThan(Collection<T> items, String propertyName, float value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            float value2 = field.getFloat(item);
            if (value2 < value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThan(Collection<T> items, String propertyName, double value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {

            double value2 = field.getDouble(item);
            if (value2 < value) {
                results.add(item);
            }
        }
        return results;
    }

    //findLessThanEqual


    //
    //
    public static <T> List<T> findLessThanEqual(Collection<T> items, String propertyName, Comparable value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            Comparable value2 = (Comparable) field.getValue(item);
            if (value2 != null && value != null && value2.compareTo(value) <= 0) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThanEqual(Collection<T> items, String propertyName, byte value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            byte value2 = field.getByte(item);
            if (value2 <= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThanEqual(Collection<T> items, String propertyName, short value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            short value2 = field.getShort(item);
            if (value2 <= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThanEqual(Collection<T> items, String propertyName, int value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            int value2 = field.getInt(item);
            if (value2 <= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThanEqual(Collection<T> items, String propertyName, long value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            long value2 = field.getLong(item);
            if (value2 <= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThanEqual(Collection<T> items, String propertyName, float value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            float value2 = field.getFloat(item);
            if (value2 <= value) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findLessThanEqual(Collection<T> items, String propertyName, double value) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {

            double value2 = field.getDouble(item);
            if (value2 <= value) {
                results.add(item);
            }
        }
        return results;
    }

    //
    //
    //
    //
    //
    //
    //
    //


    public static <T> List<T> findIn(Collection<T> items, String propertyName, Object... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            Object value2 =  field.getObject(item);
            for (Object value : values) {
                if (value2 != null && value != null && value2.equals(value)) {
                    results.add(item);
                    break;
                }
            }
        }
        return results;
    }


    public static <T> List<T> findIn(Collection<T> items, String propertyName, byte... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            for (byte value : values) {

                byte value2 = field.getByte(item);
                if (value2 > value) {
                    results.add(item);
                    break;
                }
            }
        }
        return results;
    }

    public static <T> List<T> findIn(Collection<T> items, String propertyName, short... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            short value2 = field.getShort(item);

            for (short value : values) {
                if (value2 > value) {
                    results.add(item);
                    break;
                }
            }
        }
        return results;
    }

    public static <T> List<T> findIn(Collection<T> items, String propertyName, int... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            int value2 = field.getInt(item);
            for (int value : values) {
                if (value2 > value) {
                    results.add(item);
                    break;
                }
            }
        }
        return results;
    }

    public static <T> List<T> findIn(Collection<T> items, String propertyName, long... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            long value2 = field.getLong(item);
            for (long value : values) {
                if (value2 > value) {
                    results.add(item);
                    break;
                }
            }
        }
        return results;
    }

    public static <T> List<T> findIn(Collection<T> items, String propertyName, float... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            float value2 = field.getFloat(item);
            for (float value : values) {
                if (value2 > value) {
                    results.add(item);
                    break;
                }
            }
        }
        return results;
    }

    public static <T> List<T> findIn(Collection<T> items, String propertyName, double... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {

            double value2 = field.getDouble(item);
            for (double value : values) {
                if (value2 > value) {
                    results.add(item);
                    break;
                }
            }
        }
        return results;
    }

    //
    //
    //
    //
    //

    public static <T> List<T> findNotIn(Collection<T> items, String propertyName, Object... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            boolean found = false;
            Object value2 =  field.getValue(item);
            for (Object value : values) {
                if (value2 != null && value != null && value2.equals(value)) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                items.add(item);
            }

        }
        return results;
    }


    public static <T> List<T> findNotIn(Collection<T> items, String propertyName, byte... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            boolean found = false;
            for (byte value : values) {

                byte value2 = field.getByte(item);
                if (value2 == value) {
                    results.add(item);
                    break;
                }
            }
            if (found == false) {
                items.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotIn(Collection<T> items, String propertyName, short... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            boolean found = false;
            for (short value : values) {

                short value2 = field.getByte(item);
                if (value2 == value) {
                    results.add(item);
                    break;
                }
            }
            if (found == false) {
                items.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotIn(Collection<T> items, String propertyName, int... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            boolean found = false;
            for (int value : values) {

                int value2 = field.getByte(item);
                if (value2 == value) {
                    results.add(item);
                    break;
                }
            }
            if (found == false) {
                items.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotIn(Collection<T> items, String propertyName, long... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            boolean found = false;
            for (long value : values) {

                long value2 = field.getByte(item);
                if (value2 == value) {
                    results.add(item);
                    break;
                }
            }
            if (found == false) {
                items.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotIn(Collection<T> items, String propertyName, float... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            boolean found = false;
            for (float value : values) {

                float value2 = field.getByte(item);
                if (value2 == value) {
                    results.add(item);
                    break;
                }
            }
            if (found == false) {
                items.add(item);
            }
        }
        return results;
    }

    public static <T> List<T> findNotIn(Collection<T> items, String propertyName, double... values) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, FieldAccess> fields = getAllAccessorFields(items.iterator().next().getClass());
        FieldAccess field = fields.get(propertyName);

        List<T> results = new ArrayList<>();
        for (T item : items) {
            boolean found = false;
            for (double value : values) {

                double value2 = field.getByte(item);
                if (value2 == value) {
                    results.add(item);
                    break;
                }
            }
            if (found == false) {
                items.add(item);
            }
        }
        return results;
    }

}
