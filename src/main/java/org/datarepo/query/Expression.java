package org.datarepo.query;

import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Reflection;

import java.util.Map;
import java.util.function.Predicate;

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
        Map<String, FieldAccess> fields = fieldsLocal == null ? null : fieldsLocal.get();
        if (fields == null) {
            fields = Reflection.getPropertyFieldAccessMap(o.getClass());
        }
        return resolve(fields, o);
    }
}
