package org.datarepo.query;

import org.datarepo.fields.FieldAccess;
import org.datarepo.utils.Predicate;
import org.datarepo.utils.Reflection;

import java.util.Map;

import static org.datarepo.utils.Utils.log;
import static org.datarepo.utils.Utils.warning;

public abstract class Expression implements Predicate {
    private static ThreadLocal<Map<String, FieldAccess>> fieldsLocal;

    public static void fields(Map<String, FieldAccess> fields) {
        if (fieldsLocal == null) {
            fieldsLocal = new ThreadLocal<>();
        } else {

            warning(log(Expression.class), "Fields called but fields were there, odd");
        }
        fieldsLocal.set(fields);
    }

    public static void clearFields() {
        if (fieldsLocal != null) {
            fieldsLocal.set(null);
            fieldsLocal = null;
        } else {
            warning(log(Expression.class), "Clear fields called but fields were not there, odd");
        }
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
