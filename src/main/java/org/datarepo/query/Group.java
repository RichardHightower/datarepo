package org.datarepo.query;

import java.util.Arrays;

public class Group extends Expression {
    
    private Expression[] expressions;

    private final int hashCode;
    private final String toString;

    private Grouping grouping = Grouping.AND;

    public Group(Grouping grouping, Expression... expressions) {
        this.grouping = grouping;
        this.expressions = expressions;
        hashCode = doHashCode();
        toString = doToString();

    }

    private int doHashCode() {
        int result = expressions != null ? Arrays.hashCode(expressions) : 0;
        result = 31 * result + (grouping != null ? grouping.hashCode() : 0);
        return result;

    }

    private String doToString() {

        StringBuilder builder = new StringBuilder(255);
        builder.append("{");
        builder.append("\"expressions\":");
        builder.append(Arrays.toString(expressions));
        builder.append(", \"grouping\":");
        builder.append(grouping);
        builder.append('}');
        return builder.toString();

    }

    public Grouping getGrouping() {
        return grouping;
    }


    public Expression[] getExpressions() {
        return expressions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (!Arrays.equals(expressions, group.expressions)) return false;
        if (grouping != group.grouping) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return toString;
    }
}
