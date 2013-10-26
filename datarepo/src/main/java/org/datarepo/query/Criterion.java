package org.datarepo.query;

import org.datarepo.fields.FieldAccess;
import org.datarepo.utils.Types;
import org.datarepo.utils.Utils;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


public abstract class Criterion<VALUE> extends Query {

    private Logger log = Utils.log(Criterion.class);
    private String name;
    private Operator operator;
    protected VALUE value;
    protected VALUE[] values;
    private final int hashCode;
    private final String toString;

    private boolean initialized;
    private Criterion nativeDelegate;
    private boolean useDelegate;

    public Criterion(String name, Operator operator, VALUE... values) {
        Objects.requireNonNull( name,       "name cannot be null");
        Objects.requireNonNull( operator,   "operator cannot be null");
        Objects.requireNonNull( values,     "values cannot be null");

        this.name = name;
        this.operator = operator;
        this.setValues(values);
        hashCode = doHashCode();
        toString = doToString();
    }


    public String getName() {
        return name;
    }

    public Operator getOperator() {
        return operator;
    }


    public VALUE getValue() {
        return value;
    }


    public VALUE[] getValues() {
        return values;
    }

    public void setValues(VALUE[] values) {
        if (values.length > 0) {
            this.value = values[0];
        }
        this.values = values;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Criterion)) return false;

        Criterion criterion = (Criterion) o;

        if (name != null ? !name.equals(criterion.name) : criterion.name != null) return false;
        if (operator != criterion.operator) return false;
        if (value != null ? !value.equals(criterion.value) : criterion.value != null) return false;
        if (!Arrays.equals(values, criterion.values)) return false;

        return true;
    }


    @Override
    public int hashCode() {
        return hashCode;
    }

    public int doHashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (values != null ? Arrays.hashCode(values) : 0);
        return result;
    }

    @Override
    public String toString() {
        return toString;
    }


    public String doToString() {
        StringBuilder builder = new StringBuilder(80);
        builder.append("c{");
        builder.append("\"name\":'");
        builder.append(name);
        builder.append(", \"operator\":");
        builder.append(operator);
        builder.append(", \"set\":");
        builder.append(value);
        builder.append(", \"update\":");
        builder.append(Arrays.toString(values));
        builder.append("}");
        return builder.toString();
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void init(Object o) {
        init(o.getClass());
    }

    public void init(Class clazz) {
        Map<String, FieldAccess> fields = getFieldsInternal(clazz);
        initIfNeeded(this, fields);
    }

    public void init(Map<String, FieldAccess> fields) {
        initIfNeeded(this, fields);
    }


    private static void initIfNeeded(Criterion criterion, Map<String, FieldAccess> fields) {
        if (!criterion.initialized) {
            criterion.initialized = true;
            FieldAccess field = fields.get(criterion.name);

            if (field == null) {
                return;
            }

            Class type = field.getType();


            if (!type.isPrimitive() && type != Utils.date) {
                return;
            }


            if (type == Utils.date && !(criterion.value instanceof Date)) {
                criterion.value = Types.toDate(criterion.value);
                if (criterion.operator == Operator.BETWEEN) {
                    criterion.values[0] = Types.toDate(criterion.values[0]);

                    criterion.values[1] = Types.toDate(criterion.values[1]);

                }
                return;
            }

            criterion.useDelegate = true;

            if (type == Utils.pint) {
                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = QueryFactory.eqInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = QueryFactory.notEqInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = QueryFactory.ltInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.lteInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = QueryFactory.gtInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.gteInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case BETWEEN:
                        criterion.nativeDelegate = QueryFactory.betweenInt(criterion.name, Types.toInt(criterion.value),
                                Types.toInt(criterion.values[1]));
                        break;

                    case IN:
                        criterion.nativeDelegate = QueryFactory.inInts(criterion.name, Types.iarray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = QueryFactory.notInInts(criterion.name, Types.iarray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create int delegate");
                        criterion.useDelegate = false;
                }
            } else if (type == Utils.pbyte) {
                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = QueryFactory.eqByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = QueryFactory.notEqByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = QueryFactory.ltByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.lteByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = QueryFactory.gtByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.gteByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case IN:
                        criterion.nativeDelegate = QueryFactory.inBytes(criterion.name, Types.barray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = QueryFactory.notInBytes(criterion.name, Types.barray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create byte delegate");
                        criterion.useDelegate = false;
                }

            } else if (type == Utils.pshort) {
                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = QueryFactory.eqShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = QueryFactory.notEqShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = QueryFactory.ltShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.lteShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = QueryFactory.gtShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.gteShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case IN:
                        criterion.nativeDelegate = QueryFactory.inShorts(criterion.name, Types.sarray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = QueryFactory.notInShorts(criterion.name, Types.sarray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create short delegate");
                        criterion.useDelegate = false;
                }

            } else if (type == Utils.pfloat) {
                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = QueryFactory.eqLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = QueryFactory.notEqLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = QueryFactory.ltLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.lteLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = QueryFactory.gtLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.gteLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case IN:
                        criterion.nativeDelegate = QueryFactory.inLongs(criterion.name, Types.larray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = QueryFactory.notInLongs(criterion.name, Types.larray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create Long delegate");
                        criterion.useDelegate = false;
                }


            } else if (type == Utils.pfloat) {


                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = QueryFactory.eqFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = QueryFactory.notEqFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = QueryFactory.ltFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.lteFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = QueryFactory.gtFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.gteFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case IN:
                        criterion.nativeDelegate = QueryFactory.inFloats(criterion.name, Types.farray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = QueryFactory.notInFloats(criterion.name, Types.farray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create Float delegate");
                        criterion.useDelegate = false;
                }

            } else if (type == Utils.pdouble) {

                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = QueryFactory.eqDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = QueryFactory.notEqDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = QueryFactory.ltDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.lteDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = QueryFactory.gtDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.gteDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case BETWEEN:
                        criterion.nativeDelegate = QueryFactory.betweenDouble(criterion.name, Types.toDouble(criterion.value),
                                Types.toDouble(criterion.values[1]));
                        break;

                    case IN:
                        criterion.nativeDelegate = QueryFactory.inDoubles(criterion.name, Types.toDouble(criterion.value),
                                Types.toDouble(criterion.values[1]));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = QueryFactory.notInDoubles(criterion.name, Types.toDouble(criterion.value),
                                Types.toDouble(criterion.values[1]));
                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create double delegate");
                        criterion.useDelegate = false;
                }


            } else if (type == Utils.pboolean) {


                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = QueryFactory.eqBoolean(criterion.name, Types.toBoolean(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = QueryFactory.notEqBoolean(criterion.name, Types.toBoolean(criterion.value));
                        break;


                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create double delegate");
                        criterion.useDelegate = false;
                }

            } else if (type == Utils.pchar) {
                switch (criterion.operator) {


                    case EQUAL:
                        criterion.nativeDelegate = QueryFactory.eqChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = QueryFactory.notEqChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = QueryFactory.ltChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.lteChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = QueryFactory.gtChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = QueryFactory.gteChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case BETWEEN:
                        criterion.nativeDelegate = QueryFactory.betweenChar(criterion.name, Types.toChar(criterion.value),
                                Types.toChar(criterion.values[1]));
                        break;

                    case IN:
                        criterion.nativeDelegate = QueryFactory.inChars(criterion.name, Types.toChar(criterion.value),
                                Types.toChar(criterion.values[1]));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = QueryFactory.notInChars(criterion.name, Types.toChar(criterion.value),
                                Types.toChar(criterion.values[1]));
                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create double delegate");
                        criterion.useDelegate = false;
                }

            }
        }

    }

    @Override
    public boolean test(Object o) {

        Map<String, FieldAccess> fields = getFieldsInternal(o);

        initIfNeeded(this, fields);
        if (this.useDelegate) {
            return this.nativeDelegate.resolve(fields, o);
        }
        return resolve(fields, o);
    }

    public static abstract class PrimitiveCriterion extends Criterion {

        public PrimitiveCriterion(String name, Operator operator, Object... objects) {
            super(name, operator, objects);
        }

        @Override
        public boolean test(Object o) {

            Map<String, FieldAccess> fields = getFieldsInternal(o);
            return resolve(fields, o);
        }

    }

}
