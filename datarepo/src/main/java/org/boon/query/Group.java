package org.boon.query;

import org.boon.core.reflection.fields.FieldAccess;

import java.util.Arrays;
import java.util.Map;

public abstract class Group extends Query {

    protected Query[] expressions;

    private final int hashCode;
    private String toString;

    private Grouping grouping = Grouping.AND;

    public Group(Grouping grouping, Query... expressions) {
        this.grouping = grouping;
        this.expressions = expressions;
        hashCode = doHashCode();

    }

    private int doHashCode() {
        int result = expressions != null ? Arrays.hashCode(expressions) : 0;
        result = 31 * result + (grouping != null ? grouping.hashCode() : 0);
        return result;

    }

    private String doToString() {

        if (toString == null) {


            StringBuilder builder = new StringBuilder(255);
            builder.append("{");
            builder.append("\"expressions\":");
            builder.append(Arrays.toString(expressions));
            builder.append(", \"grouping\":");
            builder.append(grouping);
            builder.append('}');
            toString = builder.toString();
        }
        return toString;

    }

    public Grouping getGrouping() {
        return grouping;
    }


    public Query[] getExpressions() {
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
        return doToString();
    }

    public static class And extends Group {

        public And(Query... expressions) {
            super(Grouping.AND, expressions);
        }

        @Override
        public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
            for (Query c : expressions) {
                if (!c.resolve(fields, owner)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class Or extends Group {

        public Or(Query... expressions) {
            super(Grouping.OR, expressions);
        }

        @Override
        public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
            for (Query c : expressions) {
                if (c.resolve(fields, owner)) {
                    return true;
                }
            }
            return false;
        }
    }

}
