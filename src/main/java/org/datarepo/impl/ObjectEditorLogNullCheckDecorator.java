package org.datarepo.impl;


import org.datarepo.ObjectEditor;
import org.datarepo.query.ValueSetter;

import static org.datarepo.utils.Utils.*;

public class ObjectEditorLogNullCheckDecorator<KEY, ITEM> extends ObjectEditorDecoratorBase<KEY, ITEM> {

    ObjectEditorLogNullCheckDecorator(ObjectEditor oe) {
        super(oe);

    }

    @Override
    public void modify(ITEM item) {
        notNull(item) ;
        super.modify(item);
    }

    @Override
    public void modify(ITEM item, String property, Object value) {
        notNull(item, property);
        super.modify(item, property, value);
    }

    @Override
    public void modifyByValue(ITEM item, String property, String value) {
        notNull(item, property);

        super.modifyByValue(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, int value) {
        notNull(item, property);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, long value) {
        notNull(item, property);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, char value) {
        notNull(item, property);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, short value) {
        notNull(item, property);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, byte value) {
        notNull(item, property);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, float value) {
        notNull(item, property);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, double value) {
        notNull(item, property);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, ValueSetter... values) {
        notNull(item, values);

        super.modify(item, values);
    }

    @Override
    public void update(KEY key, String property, Object value) {
        notNull(key, property);

        super.update(key, property, value);
    }

    @Override
    public void updateByValue(KEY key, String property, String value) {
        notNull(key, property);

        super.updateByValue(key, property, value);
    }

    @Override
    public void update(KEY key, String property, int value) {
        notNull(key, property);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, long value) {
        notNull(key, property);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, char value) {
        notNull(key, property);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, short value) {
        notNull(key, property);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, byte value) {
        notNull(key, property);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, float value) {
        notNull(key, property);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, double value) {
        notNull(key, property);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, ValueSetter... values) {
        notNull(key, values);

        super.update(key, values);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, Object compare, Object value) {
        notNull(key, property);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, int compare, int value) {
        notNull(key, property);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, long compare, long value) {
        notNull(key, property);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, char compare, char value) {
        notNull(key, property);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, short compare, short value) {
        notNull(key, property);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, byte compare, byte value) {
        notNull(key, property);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, float compare, float value) {
        notNull(key, property);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, double compare, double value) {
        notNull(key, property);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, int compare) {
        notNull(key, property);

        return super.compareAndIncrement(key, property, compare);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, long compare) {
        notNull(key, property);

        return super.compareAndIncrement(key, property, compare);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, short compare) {
        notNull(key, property);

        return super.compareAndIncrement(key, property, compare);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, byte compare) {
        notNull(key, property);

        return super.compareAndIncrement(key, property, compare);
    }
}
