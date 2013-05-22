package org.datarepo;

import java.util.ArrayList;
import java.util.List;

import static  org.datarepo.utils.Utils.*;

public abstract class ValueSetter {

    private String name;

    public String getName() {
        return name;
    }


    public abstract void doSet(Repo repo, Object item);

    public static ValueSetter value(String name, final int value) {
           return new ValueSetter() {
               @Override
               public void doSet(Repo repo, Object item) {
                   repo.modify(item, name, value);
               }
           };
    }

    public static ValueSetter value(String name, final long value) {
        return new ValueSetter() {
            @Override
            public void doSet(Repo repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(String name, final Object value) {
        return new ValueSetter() {
            @Override
            public void doSet(Repo repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(String name, final byte value) {
        return new ValueSetter() {
            @Override
            public void doSet(Repo repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(String name, final float value) {
        return new ValueSetter() {
            @Override
            public void doSet(Repo repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(String name, final char value) {
        return new ValueSetter() {
            @Override
            public void doSet(Repo repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static ValueSetter value(String name, final String value) {
        return new ValueSetter() {
            @Override
            public void doSet(Repo repo, Object item) {
                repo.modify(item, name, value);
            }
        };
    }

    public static List<ValueSetter> values (ValueSetter... values) {
         return list(values);
    }

}
