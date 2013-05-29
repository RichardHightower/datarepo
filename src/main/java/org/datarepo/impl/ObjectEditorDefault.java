package org.datarepo.impl;

import org.datarepo.ObjectEditor;
import org.datarepo.SearchableCollection;
import org.datarepo.query.ValueSetter;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.spi.ObjectEditorComposer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.*;

public class ObjectEditorDefault<KEY, ITEM> implements ObjectEditorComposer<KEY, ITEM>, ObjectEditor<KEY, ITEM> {

    private Logger log = log(ObjectEditorDefault.class);
    protected SearchableCollection<KEY, ITEM> query;
    protected Map<String, FieldAccess> fields = new LinkedHashMap<>();


    public void put(ITEM item) {
        this.add(item);
    }

    public void removeByKey(KEY key) {
        query.removeByKey(key);
    }

    public void removeAll(ITEM... items) {
        for (ITEM item : items) {
            this.delete(item);
        }
    }

    public void removeAllAsync(Collection<ITEM> items) {
        for (ITEM item : items) {
            this.delete(item);
        }
    }

    public void addAll(ITEM... items) {
        for (ITEM item : items) {
            this.add(item);
        }
    }


    public void addAllAsync(Collection<ITEM> items) {
        query.addAll(items);
    }

    public void modifyAll(ITEM... items) {
        for (ITEM item : items) {
            this.modify(item);
        }
    }

    public void modifyAll(Collection<ITEM> items) {
        for (ITEM item : items) {
            this.modify(item);
        }
    }

    public void modify(ITEM item) {

        /** See if we have an original. */
        KEY key = query.getKey(item);
        ITEM oldItem = this.doGet(key);

        if (oldItem != null) {
            delete(oldItem);
        } else {
            warning(log, "An original item was not in the repo %s", item);
        }

        this.add(item);

        if (isDebug(log)) {
            debug(log, "This item %s was modified like this %s", oldItem, item);
        }

    }

    public void modify(ITEM item, String property, Object value) {
        item = lookupAndExpect(item);
        query.invalidateIndex(property, item);
        fields.get(property).setObject(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    private void optimizeHash(ITEM item) {
        FieldAccess hashCode = fields.get("_hashCode");
        if (hashCode==null) {
            return;
        } else {
          hashCode.setInt(item, -1);
          hashCode.setInt(item, item.hashCode());
        }
    }

    public void modifyByValue(ITEM item, String property, String value) {
        item = lookupAndExpect(item);
        query.invalidateIndex(property, item);
        fields.get(property).setValue(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);

    }

    public void modify(ITEM item, String property, int value) {
        item = lookupAndExpect(item);
        query.invalidateIndex(property, item);
        fields.get(property).setInt(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void modify(ITEM item, String property, long value) {
        item = lookupAndExpect(item);
        query.invalidateIndex(property, item);
        fields.get(property).setLong(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void modify(ITEM item, String property, char value) {
        item = lookupAndExpect(item);
        query.invalidateIndex(property, item);
        fields.get(property).setChar(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void modify(ITEM item, String property, short value) {
        item = lookupAndExpect(item);
        query.invalidateIndex(property, item);
        fields.get(property).setShort(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void modify(ITEM item, String property, byte value) {
        item = lookupAndExpect(item);
        query.invalidateIndex(property, item);
        fields.get(property).setByte(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void modify(ITEM item, String property, float value) {
        item = lookupAndExpect(item);
        query.invalidateIndex(property, item);
        fields.get(property).setFloat(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void modify(ITEM item, String property, double value) {
        item = lookupAndExpect(item);
        query.invalidateIndex(property, item);
        fields.get(property).setDouble(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void modify(ITEM item, ValueSetter... values) {
        item = lookupAndExpect(item);
        for (ValueSetter value : values) {
            query.invalidateIndex(value.getName(), item);
            value.doSet(this, item);
            optimizeHash(item);
            query.validateIndex(value.getName(), item);
        }

    }

    public void update(KEY key, String property, Object value) {
        ITEM item = lookupAndExpectByKey(key);
        query.invalidateIndex(property, item);
        fields.get(property).setObject(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void updateByValue(KEY key, String property, String value) {
        ITEM item = lookupAndExpectByKey(key);
        query.invalidateIndex(property, item);
        fields.get(property).setValue(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void update(KEY key, String property, int value) {
        ITEM item = lookupAndExpectByKey(key);
        query.invalidateIndex(property, item);
        fields.get(property).setInt(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void update(KEY key, String property, long value) {
        ITEM item = lookupAndExpectByKey(key);
        query.invalidateIndex(property, item);
        fields.get(property).setLong(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void update(KEY key, String property, char value) {
        ITEM item = lookupAndExpectByKey(key);
        query.invalidateIndex(property, item);
        fields.get(property).setChar(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void update(KEY key, String property, short value) {
        ITEM item = lookupAndExpectByKey(key);
        query.invalidateIndex(property, item);
        fields.get(property).setShort(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void update(KEY key, String property, byte value) {
        ITEM item = lookupAndExpectByKey(key);
        query.invalidateIndex(property, item);
        fields.get(property).setByte(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void update(KEY key, String property, float value) {
        ITEM item = lookupAndExpectByKey(key);
        query.invalidateIndex(property, item);
        fields.get(property).setFloat(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void update(KEY key, String property, double value) {
        ITEM item = lookupAndExpectByKey(key);
        query.invalidateIndex(property, item);
        fields.get(property).setDouble(item, value);
        optimizeHash(item);
        query.validateIndex(property, item);
    }

    public void update(KEY key, ValueSetter... values) {
        ITEM item = lookupAndExpectByKey(key);

        for (ValueSetter value : values) {
            query.invalidateIndex(value.getName(), item);
            value.doSet(this, item);
            optimizeHash(item);
            query.validateIndex(value.getName(), item);
        }
    }

    public boolean compareAndUpdate(KEY key, String property, Object compare, Object value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getObject(item).equals(compare)) {
            query.invalidateIndex(property, item);
            field.setObject(item, value);
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    public boolean compareAndUpdate(KEY key, String property, int compare, int value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getInt(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setInt(item, value);
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    public boolean compareAndUpdate(KEY key, String property, long compare, long value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getLong(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setLong(item, value);
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    public boolean compareAndUpdate(KEY key, String property, char compare, char value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getChar(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setChar(item, value);
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    public boolean compareAndUpdate(KEY key, String property, short compare, short value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getShort(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setShort(item, value);
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;

    }

    public boolean compareAndUpdate(KEY key, String property, byte compare, byte value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getByte(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setByte(item, value);
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    public boolean compareAndUpdate(KEY key, String property, float compare, float value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getFloat(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setFloat(item, value);
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    public boolean compareAndUpdate(KEY key, String property, double compare, double value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getDouble(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setDouble(item, value);
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    public boolean compareAndIncrement(KEY key, String property, int compare) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getInt(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setInt(item, (compare + 1));
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;

    }

    public boolean compareAndIncrement(KEY key, String property, long compare) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getLong(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setLong(item, (compare + 1));
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    public boolean compareAndIncrement(KEY key, String property, short compare) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getShort(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setShort(item, (short) (compare + 1));
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    public boolean compareAndIncrement(KEY key, String property, byte compare) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getByte(item) == (compare)) {
            query.invalidateIndex(property, item);
            field.setByte(item, (byte) (compare + 1));
            set = true;
            optimizeHash(item);
            query.validateIndex(property, item);
        }
        return set;
    }

    @Override
    public void addAll(List<ITEM> items) {
        query.addAll(items);
    }


    public boolean add(ITEM item) {
        return query.add(item);
    }


    public void setFields(Map<String, FieldAccess> fields) {
        this.fields = fields;
    }


    @Override
    public void setSearchableCollection(SearchableCollection searchableCollection) {
        this.query = searchableCollection;
    }

    @Override
    public void init() {
    }


    public ITEM get(KEY key) {
        return (ITEM) query.get(key);
    }

    private ITEM doGet(KEY key) {
        return (ITEM) query.get(key);
    }

    @Override
    public KEY getKey(ITEM item) {
        return (KEY) query.getKey(item);
    }

    private ITEM lookupAndExpect(ITEM item) {
        KEY key = getKey(item);
        ITEM oldItem = this.doGet(key);


        if (oldItem == null) {
            complain(sprintf("An original item was not in the repo %s", item));

        }
        return oldItem;
    }

    private ITEM lookupAndExpectByKey(KEY key) {
        ITEM oldItem = this.doGet(key);

        if (oldItem == null) {
            complain(sprintf("An original item was not in the repo at this key %s", key));

        }
        return oldItem;
    }


    @Override
    public void clear() {
        query.clear();
    }

    @Override
    public boolean delete(ITEM item) {
        return query.delete(item);

    }

    @Override
    public List<ITEM> all() {
        return query.all();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int size() {
        return query.size();

    }

    @Override
    public Collection<ITEM> toCollection() {
        return query;
    }


    public SearchableCollection<KEY, ITEM> query() {
        return query;
    }


}
