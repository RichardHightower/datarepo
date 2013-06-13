package org.datarepo.query;

import org.datarepo.fields.FieldAccess;

import java.util.Map;

public class Not extends Expression {

    private final Expression expression;

    public Not(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
        return !this.expression.resolve(fields, owner);
    }
}
