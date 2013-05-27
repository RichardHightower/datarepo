package org.datarepo.impl;


import org.datarepo.ObjectEditor;
import org.datarepo.query.ValueSetter;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.*;

public class ObjectEditorLogNullCheckDecorator<KEY, ITEM> extends ObjectEditorDecoratorBase<KEY, ITEM> {

    Logger logger = log(ObjectEditor.class);
    Level level = Level.FINER;


    private boolean debug = false;

    void l(String msg, Object... items) {
        if (debug) {
            printf(msg, items);
        }
        fprintf(logger, level, msg, items);
    }


    public void setLevel(Level level) {
        this.level = level;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }


    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public ObjectEditorLogNullCheckDecorator () {


    }

    ObjectEditorLogNullCheckDecorator(ObjectEditor oe) {
        super(oe);

    }


    @Override
    public void put(ITEM item) {
        notNull(item) ;
        l("put (item=%s)", item);
        super.put(item);
    }


    @Override
    public boolean add(ITEM item) {
        notNull(item) ;
        l("add (item=%s)", item);
        return super.add(item);
    }

    @Override
    public ITEM get(KEY key) {
        notNull(key) ;
        l("get (key=%s)", key);

        return super.get(key);
    }


    @Override
    public void modify(ITEM item) {
        notNull(item) ;
        l("modify (item=%s)", item);

        super.modify(item);
    }

    @Override
    public void modify(ITEM item, String property, Object value) {
        notNull(item, property);
        l("modify (item=%s, property=%s, value=%s)", item, property, value);

        super.modify(item, property, value);
    }

    @Override
    public void modifyByValue(ITEM item, String property, String value) {
        notNull(item, property);
        l("modify (item=%s, property=%s, value=%s)", item, property, value);

        super.modifyByValue(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, int value) {
        notNull(item, property);
        l("modify (item=%s, property=%s, value=%s)", item, property, value);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, long value) {
        notNull(item, property);
        l("modify (item=%s, property=%s, value=%s)", item, property, value);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, char value) {
        notNull(item, property);
        l("modify (item=%s, property=%s, value=%s)", item, property, value);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, short value) {
        notNull(item, property);
        l("modify (item=%s, property=%s, value=%s)", item, property, value);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, byte value) {
        notNull(item, property);
        l("modify (item=%s, property=%s, value=%s)", item, property, value);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, float value) {
        notNull(item, property);
        l("modify (item=%s, property=%s, value=%s)", item, property, value);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, double value) {
        notNull(item, property);
        l("modify (item=%s, property=%s, value=%s)", item, property, value);

        super.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, ValueSetter... values) {
        notNull(item, values);
        l("modify (item=%s, property=%s, values=%s)", item, values);

        super.modify(item, values);
    }

    @Override
    public void update(KEY key, String property, Object value) {
        notNull(key, property);
        l("update (key=%s, property=%s, value=%s)", key, property, value);

        super.update(key, property, value);
    }

    @Override
    public void updateByValue(KEY key, String property, String value) {
        notNull(key, property);
        l("updateByValue (key=%s, property=%s, value=%s)", key, property, value);

        super.updateByValue(key, property, value);
    }

    @Override
    public void update(KEY key, String property, int value) {
        notNull(key, property);
        l("update (key=%s, property=%s, value=%s)", key, property, value);
        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, long value) {
        notNull(key, property);
        l("update (key=%s, property=%s, value=%s)", key, property, value);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, char value) {
        notNull(key, property);
        l("update (key=%s, property=%s, value=%s)", key, property, value);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, short value) {
        notNull(key, property);
        l("update (key=%s, property=%s, value=%s)", key, property, value);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, byte value) {
        notNull(key, property);
        l("update (key=%s, property=%s, value=%s)", key, property, value);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, float value) {
        notNull(key, property);
        l("update (key=%s, property=%s, value=%s)", key, property, value);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, double value) {
        notNull(key, property);
        l("update (key=%s, property=%s, value=%s)", key, property, value);

        super.update(key, property, value);
    }

    @Override
    public void update(KEY key, ValueSetter... values) {
        notNull(key, values);
        l("update (key=%s, values=%s)", key, values);

        super.update(key, values);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, Object compare, Object value) {
        notNull(key, property);
        l("compareAndUpdate (key=%s, property=%s, compare=%s, value=%s)", key, property, compare, value);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, int compare, int value) {
        notNull(key, property);
        l("compareAndUpdate (key=%s, property=%s, compare=%s, value=%s)", key, property, compare, value);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, long compare, long value) {
        notNull(key, property);
        l("compareAndUpdate (key=%s, property=%s, compare=%s, value=%s)", key, property, compare, value);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, char compare, char value) {
        notNull(key, property);
        l("compareAndUpdate (key=%s, property=%s, compare=%s, value=%s)", key, property, compare, value);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, short compare, short value) {
        notNull(key, property);
        l("compareAndUpdate (key=%s, property=%s, compare=%s, value=%s)", key, property, compare, value);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, byte compare, byte value) {
        notNull(key, property);
        l("compareAndUpdate (key=%s, property=%s, compare=%s, value=%s)", key, property, compare, value);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, float compare, float value) {
        notNull(key, property);
        l("compareAndUpdate (key=%s, property=%s, compare=%s, value=%s)", key, property, compare, value);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, double compare, double value) {
        notNull(key, property);
        l("compareAndUpdate (key=%s, property=%s, compare=%s, value=%s)", key, property, compare, value);

        return super.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, int compare) {
        notNull(key, property);
        l("compareAndIncrement (key=%s, property=%s, compare=%s)", key, property, compare);

        return super.compareAndIncrement(key, property, compare);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, long compare) {
        notNull(key, property);
        l("compareAndIncrement (key=%s, property=%s, compare=%s)", key, property, compare);

        return super.compareAndIncrement(key, property, compare);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, short compare) {
        notNull(key, property);
        l("compareAndIncrement (key=%s, property=%s, value=%s)", key, property, compare);

        return super.compareAndIncrement(key, property, compare);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, byte compare) {
        notNull(key, property);
        l("compareAndIncrement (key=%s, property=%s, value=%s)", key, property, compare);

        return super.compareAndIncrement(key, property, compare);
    }
}
