package org.datarepo.query;

import org.datarepo.reflection.Types;

public class Criteria {

    public static Group and(Expression... expressions) {
        return new Group(Grouping.AND, expressions);
    }

    public static Group or(Expression... expressions) {
        return new Group(Grouping.OR, expressions);
    }

    public static Expression eq(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.EQUAL, value);
    }

    public static Expression notEq(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.NOT_EQUAL, value);
    }

    public static Expression notIn(Object name, Object... values) {
        return new Criterion<Object>(name.toString(), Operator.NOT_IN, values);
    }

    public static Expression in(Object name, Object... values) {
        return new Criterion<Object>(name.toString(), Operator.IN, values);
    }


    public static Expression lt(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.LESS_THAN, value);
    }

    public static Expression lte(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.LESS_THAN_EQUAL, value);
    }

    public static Expression gt(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN, value);
    }

    public static Expression gte(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.GREATER_THAN_EQUAL, value);
    }

    public static Expression between(Object name, Object value, Object value2) {
        return new Criterion<Object>(name.toString(), Operator.BETWEEN, value, value2);
    }


    public static Expression betweenYears(Object name, int year1, int year2) {

        return new Criterion<Object>(name.toString(), Operator.BETWEEN, Types.year(year1), Types.year(year2));
    }

    public static Expression startsWith(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.STARTS_WITH, value);
    }

    public static Expression endsWith(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.ENDS_WITH, value);
    }

    public static Expression contains(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.CONTAINS, value);
    }

    public static Expression matches(Object name, Object value) {
        return new Criterion<Object>(name.toString(), Operator.MATCHES, value);
    }


}
