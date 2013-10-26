package org.datarepo.query;


import org.datarepo.predicates.Predicate;

import org.boon.reflection.fields.FieldAccess;
import org.boon.reflection.Reflection;

import java.util.Map;

public abstract class Query implements Predicate {
    private static ThreadLocal<Map<String, FieldAccess>> fieldsLocal = new ThreadLocal<>();

    public static void fields(Map<String, FieldAccess> fields) {
        fieldsLocal.set(fields);
    }

    public static void clearFields() {
        fieldsLocal.set(null);
    }

    public abstract boolean resolve(Map<String, FieldAccess> fields, Object owner);


    @Override
    public boolean test(Object o) {
        Map<String, FieldAccess> fields = getFieldsInternal(o);
        return resolve(fields, o);
    }

    protected Map<String, FieldAccess> getFieldsInternal(Object o) {
        return getFieldsInternal(o.getClass());
    }

    protected Map<String, FieldAccess> getFieldsInternal(Class clazz) {
        Map<String, FieldAccess> fields = fieldsLocal == null ? null : fieldsLocal.get();
        if (fields == null) {
            fields = Reflection.getPropertyFieldAccessMap(clazz);
        }
        return fields;
    }

}
