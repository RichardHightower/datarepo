package org.datarepo.query;

import org.datarepo.ObjectEditor;

import java.io.Serializable;
import java.util.List;

import static org.datarepo.utils.Utils.list;

public abstract class ValueSetter implements Serializable {

    private String name;

    public String getName() {
        return name;
    }


    public abstract void doSet(ObjectEditor repo, Object item);

    public static ValueSetter value(final String name, final int value) {
        return new ValueSetter() {
            @Override
            public void doSet(ObjectEditor repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter incInt(final String name) {
        return new ValueSetter() {
            @Override
            public void doSet(ObjectEditor repo, Object item) {
                int v = repo.getInt(item, name);
                v++;
                repo.modify(item, name, v);
            }
        };
    }

    public static ValueSetter incPercent(final String name, final int percent) {
        return new ValueSetter() {

            //Avoid the lookup, pass the fields.
            @Override
            public void doSet(ObjectEditor repo, Object item) {
                int value = repo.getInt(item, name);
                double dvalue = value;
                double dprecent = percent / 100.0;
                dvalue = dvalue + (dvalue * dprecent);
                value = (int) dvalue;
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(final String name, final long value) {
        return new ValueSetter() {
            @Override
            public void doSet(ObjectEditor repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(final String name, final Object value) {
        return new ValueSetter() {
            @Override
            public void doSet(ObjectEditor repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(final String name, final byte value) {
        return new ValueSetter() {
            @Override
            public void doSet(ObjectEditor repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(final String name, final float value) {
        return new ValueSetter() {
            @Override
            public void doSet(ObjectEditor repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(final String name, final char value) {
        return new ValueSetter() {
            @Override
            public void doSet(ObjectEditor repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(final String name, final String value) {
        return new ValueSetter() {
            @Override
            public void doSet(ObjectEditor repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static List<ValueSetter> values(ValueSetter... values) {
        return list(values);
    }


}
