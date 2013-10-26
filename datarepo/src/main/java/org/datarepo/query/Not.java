package org.datarepo.query;

import org.boon.fields.FieldAccess;

import java.util.Map;

public class Not extends Query {

    private final Query expression;

    public Not(Query expression) {
        this.expression = expression;
    }

    @Override
    public boolean resolve(Map<String, FieldAccess> fields, Object owner) {
        return !this.expression.resolve(fields, owner);
    }
}
