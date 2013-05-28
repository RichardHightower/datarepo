package org.datarepo.query;

import java.util.Arrays;

public class Criterion<VALUE> extends Expression {
    private String name;
    private Operator operator;
    private VALUE value;
    private VALUE[] values;

    public Criterion(String name, Operator operator, VALUE... values) {
        this.name = name;
        this.operator = operator;
        this.setValues(values);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public VALUE getValue() {
        return value;
    }

    public void setValue(VALUE value) {
        this.value = value;
        this.values = null;
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
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (values != null ? Arrays.hashCode(values) : 0);
        return result;
    }



    @Override
    public String toString() {
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
}
