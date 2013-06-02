package org.datarepo.query;

import org.datarepo.reflection.FieldAccess;

import java.util.*;

public class Criteria {

    public static boolean test(Object obj, Expression exp) {
        return exp.test(obj);
    }

    public static boolean andTest(Object obj, Expression... exp) {
        return and(exp).test(obj);
    }

    public static boolean orTest(Object obj, Expression... exp) {
        return or(exp).test(obj);
    }

    public static <T> List<T> filter(Collection<T> items, Map<String, FieldAccess> fields, Expression exp) {
        if (items.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        List<T> results = new ArrayList<>();
        for (T item : items) {

            if (exp.test(item)) {
                results.add(item);
            }
        }
        return results;
    }


    public static Group and(Expression... expressions) {
        return new Group.And(expressions);
    }

    public static Group or(Expression... expressions) {
        return new Group.Or(expressions);
    }

    public static Criterion eq(final Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.EQUAL, value) {
            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);
                return value.equals(field.getValue(owner));
            }
        };
    }

    public static Criterion notEq(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.NOT_EQUAL, value) {
            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);

                return !value.equals(field.getValue(owner));
            }
        };
    }

    public static Criterion notIn(Object name, final Object... values) {
        return new Criterion<Object>(name.toString(), Operator.NOT_IN, values) {

            HashSet set = new HashSet<>();

            {
                for (Object value : values) {
                    set.add(value);
                }
            }

            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);

                Object value = field.getValue(owner);
                if (value == null) {
                    return false;
                }
                return !set.contains(value);
            }
        };
    }

    public static Criterion in(Object name, Object... values) {
        return new Criterion<Object>(name.toString(), Operator.IN, values) {
            HashSet set = new HashSet<>();

            {
                for (Object value : values) {
                    set.add(value);
                }
            }

            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);

                return set.contains(field.getValue(owner));
            }
        };
    }


    public static Criterion lt(Object name, final Object value) {
        return new Criterion<Object>(name.toString(), Operator.LESS_THAN, value) {
            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);

                return ((Comparable) value).compareTo(field.getValue(owner)) > 0;
            }
        };
    }

    public static Criterion lte(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.LESS_THAN_EQUAL, value) {
            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);

                return ((Comparable) value).compareTo(field.getValue(owner)) >= 0;

            }
        };
    }

    public static Criterion gt(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, value) {
            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);

                return ((Comparable) value).compareTo(field.getValue(owner)) < 0;
            }
        };
    }

    public static Criterion gte(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, value) {
            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);

                return ((Comparable) value).compareTo(field.getValue(owner)) <= 0;
            }
        };
    }

    public static Criterion between(Object name, final Object value, final Object value2) {
        return new Criterion<Object>(name.toString(), Operator.BETWEEN, value, value2) {
            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);

                return ((Comparable) value).compareTo(field.getValue(owner)) <= 0 &&
                        ((Comparable) value2).compareTo(field.getValue(owner)) >= 0;
            }
        };
    }

//    I am going to add date handling, but not now. TODO
//
//    public static Expression betweenYears(Object name, int year1, int year2) {
//
//        return new Criterion<Object>(name.toString(), Operator.EQUAL, year1) {
//            @Override
//            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
//                return value.equals(field.getValue(owner)) ;
//            }
//        };
//    }

    public static Criterion startsWith(Object name, final Object value) {
        return new Criterion<Object>(name.toString(), Operator.EQUAL, value) {
            String sValue = value instanceof String ? (String) value : value.toString();

            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);
                Object itemValue = field.getValue(owner);
                String sItemvalue = itemValue instanceof String ? (String) itemValue : itemValue.toString();
                return sItemvalue.startsWith(sValue);
            }
        };
    }

    public static Criterion endsWith(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.EQUAL, value) {
            String sValue = value instanceof String ? (String) value : value.toString();

            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);
                Object itemValue = field.getValue(owner);
                String sItemvalue = itemValue instanceof String ? (String) itemValue : itemValue.toString();
                return sItemvalue.endsWith(sValue);
            }
        };
    }

    public static Criterion contains(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.EQUAL, value) {
            String sValue = value instanceof String ? (String) value : value.toString();

            @Override
            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
                FieldAccess field = fields.get(name);
                Object itemValue = field.getValue(owner);
                String sItemvalue = itemValue instanceof String ? (String) itemValue : itemValue.toString();
                return sItemvalue.contains(sValue);
            }
        };
    }

    // TODO regex suppot
//    public static Expression matches(Object name, Object value) {
//        return new Criterion<Object>(name.toString(), Operator.EQUAL, value) {
//            @Override
//            public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
//                return value.equals(field.getValue(owner)) ;
//            }
//        };
//    }


    //Int Int Int
    //
    //
    //
    public static Criterion eqInt(final Object name, final int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value == compareValue;
            }

        };
    }

    public static Criterion notEqInt(final Object name, final int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value != compareValue;
            }
        };
    }

    public static Criterion notInInts(final Object name, final int... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);

                for (int compareValue : compareValues) {
                    if (value == compareValue) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static Criterion inInts(final Object name, final int... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);

                for (int compareValue : compareValues) {
                    if (value == compareValue) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Criterion ltInt(final Object name, int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value < compareValue;
            }
        };
    }


    public static Criterion lteInt(final Object name, int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value <= compareValue;
            }
        };
    }

    public static Criterion gtInt(final Object name, int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value > compareValue;
            }
        };
    }

    public static Criterion gteInt(final Object name, final int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value >= compareValue;
            }
        };
    }

    public static Criterion betweenInt(final Object name, final int start, final int stop) {
        return new Criterion<Object>(name.toString(), Operator.BETWEEN, start, stop) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value >= start && value < stop;
            }
        };
    }


    //Float
    //
    //
    //
    public static Criterion eqFloat(final Object name, final float compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                float value = field.getFloat(owner);
                return value == compareValue;
            }

        };
    }

    public static Criterion notEqFloat(final Object name, final float compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                float value = field.getFloat(owner);
                return value != compareValue;
            }
        };
    }

    public static Criterion notInFloats(final Object name, final float... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                float value = field.getFloat(owner);

                for (float compareValue : compareValues) {
                    if (value == compareValue) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static Criterion inFloats(final Object name, final float... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                float value = field.getFloat(owner);

                for (float compareValue : compareValues) {
                    if (value == compareValue) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Criterion ltFloat(final Object name, float compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                float value = field.getFloat(owner);
                return value < compareValue;
            }
        };
    }


    public static Criterion lteFloat(final Object name, float compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                float value = field.getFloat(owner);
                return value <= compareValue;
            }
        };
    }

    public static Criterion gtFloat(final Object name, float compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                float value = field.getFloat(owner);
                return value > compareValue;
            }
        };
    }

    public static Criterion gteFloat(final Object name, final float compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                float value = field.getFloat(owner);
                return value >= compareValue;
            }
        };
    }

    public static Criterion betweenFloat(final Object name, final float start, final float stop) {
        return new Criterion<Object>(name.toString(), Operator.BETWEEN, start, stop) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                float value = field.getFloat(owner);
                return value >= start && value < stop;
            }
        };
    }

    //Double
    //
    //
    //
    public static Criterion eqDouble(final Object name, final double compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                double value = field.getDouble(owner);
                return value == compareValue;
            }

        };
    }

    public static Criterion notEqDouble(final Object name, final double compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                double value = field.getDouble(owner);
                return value != compareValue;
            }
        };
    }

    public static Criterion notInDoubles(final Object name, final double... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                double value = field.getDouble(owner);

                for (double compareValue : compareValues) {
                    if (value == compareValue) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static Criterion inDoubles(final Object name, final double... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                double value = field.getDouble(owner);

                for (double compareValue : compareValues) {
                    if (value == compareValue) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Criterion ltDouble(final Object name, double compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                double value = field.getDouble(owner);
                return value < compareValue;
            }
        };
    }


    public static Criterion lteDouble(final Object name, double compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                double value = field.getDouble(owner);
                return value <= compareValue;
            }
        };
    }

    public static Criterion gtDouble(final Object name, double compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                double value = field.getDouble(owner);
                return value > compareValue;
            }
        };
    }

    public static Criterion gteDouble(final Object name, final double compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                double value = field.getDouble(owner);
                return value >= compareValue;
            }
        };
    }

    public static Criterion betweenDouble(final Object name, final double start, final double stop) {
        return new Criterion<Object>(name.toString(), Operator.BETWEEN, start, stop) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                double value = field.getDouble(owner);
                return value >= start && value < stop;
            }
        };
    }


    //Short
    //
    //
    //
    public static Criterion eqShort(final Object name, final short compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                short value = field.getShort(owner);
                return value == compareValue;
            }

        };
    }

    public static Criterion notEqShort(final Object name, final short compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                short value = field.getShort(owner);
                return value != compareValue;
            }
        };
    }

    public static Criterion notInShorts(final Object name, final short... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                short value = field.getShort(owner);

                for (short compareValue : compareValues) {
                    if (value == compareValue) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static Criterion inShorts(final Object name, final short... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                short value = field.getShort(owner);

                for (short compareValue : compareValues) {
                    if (value == compareValue) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Criterion ltShort(final Object name, short compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                short value = field.getShort(owner);
                return value < compareValue;
            }
        };
    }


    public static Criterion lteShort(final Object name, short compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                short value = field.getShort(owner);
                return value <= compareValue;
            }
        };
    }

    public static Criterion gtShort(final Object name, short compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                short value = field.getShort(owner);
                return value > compareValue;
            }
        };
    }

    public static Criterion gteShort(final Object name, final short compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                short value = field.getShort(owner);
                return value >= compareValue;
            }
        };
    }

    public static Criterion betweenShort(final Object name, final short start, final short stop) {
        return new Criterion<Object>(name.toString(), Operator.BETWEEN, start, stop) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                short value = field.getShort(owner);
                return value >= start && value < stop;
            }
        };
    }


    //Byte
    //
    //
    //
    public static Criterion eqByte(final Object name, final byte compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                byte value = field.getByte(owner);
                return value == compareValue;
            }

        };
    }

    public static Criterion notEqByte(final Object name, final byte compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                byte value = field.getByte(owner);
                return value != compareValue;
            }
        };
    }

    public static Criterion notInBytes(final Object name, final byte... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                byte value = field.getByte(owner);

                for (byte compareValue : compareValues) {
                    if (value == compareValue) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static Criterion inBytes(final Object name, final byte... compareValues) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValues) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                byte value = field.getByte(owner);

                for (byte compareValue : compareValues) {
                    if (value == compareValue) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Criterion ltByte(final Object name, byte compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                byte value = field.getByte(owner);
                return value < compareValue;
            }
        };
    }


    public static Criterion lteByte(final Object name, byte compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                byte value = field.getByte(owner);
                return value <= compareValue;
            }
        };
    }

    public static Criterion gtByte(final Object name, byte compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                byte value = field.getByte(owner);
                return value > compareValue;
            }
        };
    }

    public static Criterion gteByte(final Object name, final byte compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                byte value = field.getByte(owner);
                return value >= compareValue;
            }
        };
    }

    public static Criterion betweenByte(final Object name, final byte start, final byte stop) {
        return new Criterion<Object>(name.toString(), Operator.BETWEEN, start, stop) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                byte value = field.getByte(owner);
                return value >= start && value < stop;
            }
        };
    }

}
