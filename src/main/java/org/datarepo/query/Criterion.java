package org.datarepo.query;

import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Types;
import org.datarepo.utils.Utils;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.notNull;

public abstract class Criterion<VALUE> extends Expression {

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
        notNull(name, operator, values);
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
        builder.append(", \"value\":");
        builder.append(value);
        builder.append(", \"values\":");
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


    private static void initIfNeeded(Criterion criterion, Map<String, FieldAccess> fields) {
        if (!criterion.initialized) {
            criterion.initialized = true;
            FieldAccess field = fields.get(criterion.name);
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
                        criterion.nativeDelegate = Criteria.eqInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = Criteria.notEqInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = Criteria.ltInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.lteInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = Criteria.gtInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.gteInt(criterion.name, Types.toInt(criterion.value));
                        break;

                    case BETWEEN:
                        criterion.nativeDelegate = Criteria.betweenInt(criterion.name, Types.toInt(criterion.value),
                                Types.toInt(criterion.values[1]));
                        break;

                    case IN:
                        criterion.nativeDelegate = Criteria.inInts(criterion.name, Types.iarray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = Criteria.notInInts(criterion.name, Types.iarray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create int delegate");
                        criterion.useDelegate = false;
                }
            } else if (type == Utils.pbyte) {
                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = Criteria.eqByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = Criteria.notEqByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = Criteria.ltByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.lteByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = Criteria.gtByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.gteByte(criterion.name, Types.toByte(criterion.value));
                        break;

                    case IN:
                        criterion.nativeDelegate = Criteria.inBytes(criterion.name, Types.barray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = Criteria.notInBytes(criterion.name, Types.barray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create byte delegate");
                        criterion.useDelegate = false;
                }

            } else if (type == Utils.pshort) {
                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = Criteria.eqShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = Criteria.notEqShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = Criteria.ltShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.lteShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = Criteria.gtShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.gteShort(criterion.name, Types.toShort(criterion.value));
                        break;

                    case IN:
                        criterion.nativeDelegate = Criteria.inShorts(criterion.name, Types.sarray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = Criteria.notInShorts(criterion.name, Types.sarray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create short delegate");
                        criterion.useDelegate = false;
                }

            } else if (type == Utils.pfloat) {
                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = Criteria.eqLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = Criteria.notEqLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = Criteria.ltLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.lteLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = Criteria.gtLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.gteLong(criterion.name, Types.toLong(criterion.value));
                        break;

                    case IN:
                        criterion.nativeDelegate = Criteria.inLongs(criterion.name, Types.larray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = Criteria.notInLongs(criterion.name, Types.larray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create Long delegate");
                        criterion.useDelegate = false;
                }


            } else if (type == Utils.pfloat) {


                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = Criteria.eqFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = Criteria.notEqFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = Criteria.ltFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.lteFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = Criteria.gtFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.gteFloat(criterion.name, Types.toFloat(criterion.value));
                        break;

                    case IN:
                        criterion.nativeDelegate = Criteria.inFloats(criterion.name, Types.farray(criterion.values));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = Criteria.notInFloats(criterion.name, Types.farray(criterion.values));

                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create Float delegate");
                        criterion.useDelegate = false;
                }

            } else if (type == Utils.pdouble) {

                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = Criteria.eqDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = Criteria.notEqDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = Criteria.ltDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.lteDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = Criteria.gtDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.gteDouble(criterion.name, Types.toDouble(criterion.value));
                        break;

                    case BETWEEN:
                        criterion.nativeDelegate = Criteria.betweenDouble(criterion.name, Types.toDouble(criterion.value),
                                Types.toDouble(criterion.values[1]));
                        break;

                    case IN:
                        criterion.nativeDelegate = Criteria.inDoubles(criterion.name, Types.toDouble(criterion.value),
                                Types.toDouble(criterion.values[1]));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = Criteria.notInDoubles(criterion.name, Types.toDouble(criterion.value),
                                Types.toDouble(criterion.values[1]));
                        break;

                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create double delegate");
                        criterion.useDelegate = false;
                }


            } else if (type == Utils.pboolean) {


                switch (criterion.operator) {
                    case EQUAL:
                        criterion.nativeDelegate = Criteria.eqBoolean(criterion.name, Types.toBoolean(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = Criteria.notEqBoolean(criterion.name, Types.toBoolean(criterion.value));
                        break;


                    default:
                        Utils.warning(Utils.log(Criterion.class), "Can't create double delegate");
                        criterion.useDelegate = false;
                }

            } else if (type == Utils.pchar) {
                switch (criterion.operator) {


                    case EQUAL:
                        criterion.nativeDelegate = Criteria.eqChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case NOT_EQUAL:
                        criterion.nativeDelegate = Criteria.notEqChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case LESS_THAN:
                        criterion.nativeDelegate = Criteria.ltChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case LESS_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.lteChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case GREATER_THAN:
                        criterion.nativeDelegate = Criteria.gtChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case GREATER_THAN_EQUAL:
                        criterion.nativeDelegate = Criteria.gteChar(criterion.name, Types.toChar(criterion.value));
                        break;

                    case BETWEEN:
                        criterion.nativeDelegate = Criteria.betweenChar(criterion.name, Types.toChar(criterion.value),
                                Types.toChar(criterion.values[1]));
                        break;

                    case IN:
                        criterion.nativeDelegate = Criteria.inChars(criterion.name, Types.toChar(criterion.value),
                                Types.toChar(criterion.values[1]));
                        break;


                    case NOT_IN:
                        criterion.nativeDelegate = Criteria.notInChars(criterion.name, Types.toChar(criterion.value),
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
