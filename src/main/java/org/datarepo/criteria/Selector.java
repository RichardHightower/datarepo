package org.datarepo.criteria;

import org.datarepo.reflection.FieldAccess;

import java.util.List;
import java.util.Map;

import static org.datarepo.utils.Utils.list;

public abstract class Selector {
    protected String name;

    public Selector(){}

    public Selector (String n) {name = n;}

    public String getName() {
        return name;
    }

    public static List<Selector> selects (Selector... selects) {
        return list(selects);
    }

    public static Selector select(final String name) {
        return new Selector(name) {
            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                  row.put(this.name, fields.get(this.name).getValue(item));
            }

            @Override
            public void handleStart(List<? extends Object> results) {
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
            }
        };
    }

    public static Selector rowId() {

        return new Selector("rowId") {
            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                row.put(name, index);
            }

            @Override
            public void handleStart(List<? extends Object> results) {
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
            }
        };
    }

    public abstract void handleRow(int index, Map<String,Object> row,
                                   Object item,
                                   Map<String,FieldAccess> fields);

    public abstract void handleStart(List<? extends Object> results);

    public abstract void handleComplete(List<Map<String, Object>> rows);
}
