package org.datarepo.query;

import org.datarepo.reflection.FieldAccess;

import java.util.HashSet;
import java.util.Map;

public class Criteria {


    public static Group and(Expression... expressions) {
        return new Group(Grouping.AND, expressions);
    }

    public static Group or(Expression... expressions) {
        return new Group(Grouping.OR, expressions);
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


    //
    //
    //
    //
    public static Criterion eq(final Object name, final int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value == compareValue;
            }

        };
    }

    public static Criterion notEq(final Object name, final int compareValue) {
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

    public static Criterion lt(final Object name, int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value < compareValue;
            }
        };
    }


    public static Criterion lte(final Object name, int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value <= compareValue;
            }
        };
    }

    public static Criterion gt(final Object name, int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value > compareValue;
            }
        };
    }

    public static Criterion gte(final Object name, final int compareValue) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, compareValue) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value >= compareValue;
            }
        };
    }

    public static Criterion between(final Object name, final int start, final int stop) {
        return new Criterion<Object>(name.toString(), Operator.BETWEEN, start, stop) {
            @Override
            public boolean resolve(final Map<String, FieldAccess> fields, final Object owner) {
                FieldAccess field = fields.get(name);
                int value = field.getInt(owner);
                return value >= start && value < stop;
            }
        };
    }


}
