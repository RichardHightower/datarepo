package org.datarepo.impl;

import org.datarepo.*;
import org.datarepo.Filter;
import org.datarepo.query.Expression;
import org.datarepo.query.Selector;
import org.datarepo.query.ValueSetter;
import org.datarepo.query.Visitor;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Reflection;
import org.datarepo.utils.Utils;

import static org.datarepo.reflection.Reflection.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.datarepo.utils.Utils.*;
import static org.datarepo.utils.Utils.isArray;

/**
 * Default Repo implementation.
 *
 * @param <KEY>  primary key or handle of object.
 * @param <ITEM> item that this repo holds.
 */
public class RepoDefault<KEY, ITEM> implements RepoComposer, Repo<KEY, ITEM> {

    private Logger log = log(RepoDefault.class);
    private Map<String, LookupIndex> lookupIndexMap = new LinkedHashMap<>();
    private Map<String, SearchIndex> searchIndexMap = new LinkedHashMap<>();
    private Map<String, FieldAccess> fields = new LinkedHashMap<>();

    private List<LookupIndex> indexes = new ArrayList<LookupIndex>();
    private Function<ITEM, KEY> primaryKeyGetter;
    private String primaryKeyName;
    private Filter filter;

    private UniqueSearchIndex<KEY, ITEM> primaryIndex;

    @Override
    public void addSearchIndex(String name, SearchIndex si) {
        config(log, "search index added name %s", name);
        searchIndexMap.put(name, si);
        indexes.add(si);
    }

    @Override
    public void addLookupIndex(String name, LookupIndex si) {
        config(log, "lookup index added name %s", name);

        lookupIndexMap.put(name, si);
        indexes.add(si);
    }

    @Override
    public void setPrimaryKeyGetter(Function getter) {
        config(log, "primary key getter set %s", getter);

        this.primaryKeyGetter = getter;
    }


    @Override
    public ITEM get(KEY key) {
        LookupIndex lookupIndex = lookupIndexMap.get(this.primaryKeyName);
        return (ITEM) lookupIndex.get(key);
    }

    @Override
    public void put(ITEM item) {
        this.add(item);
    }

    @Override
    public void removeByKey(KEY key) {
        ITEM item = primaryIndex.get(key);
        this.delete(item);
    }

    @Override
    public void removeAll(ITEM... items) {
        for (ITEM item : items) {
            this.delete(item);
        }
    }


    @Override
    public void removeAllAsync(Collection<ITEM> items) {
        for (ITEM item : items) {
            this.delete(item);
        }
    }

    @Override
    public void addAll(ITEM... items) {
        for (ITEM item : items) {
            this.add(item);
        }
    }


    @Override
    public boolean addAll(Collection<? extends ITEM> items) {
        for (ITEM item : items) {
            this.add(item);
        }
        return true;
    }

    @Override
    public void addAllAsync(Collection<ITEM> items) {
        this.addAll(items);
    }

    @Override
    public void modifyAll(ITEM... items) {
        for (ITEM item : items) {
            this.modify(item);
        }
    }

    @Override
    public void modifyAll(Collection<ITEM> items) {
        for (ITEM item : items) {
            this.modify(item);
        }
    }

    @Override
    public void modify(ITEM item) {

        notNull(item);

        /** See if we have an original. */
        KEY key = (KEY) this.primaryKeyGetter.apply(item);
        ITEM oldItem = this.get(key);

        if (oldItem != null) {
            remove(oldItem);
        } else {
            warning(log, "An original item was not in the repo %s", item);
        }

        this.add(item);

        if (isDebug(log)) {
            debug(log, "This item %s was modified like this %s", oldItem, item);
        }

    }

    private ITEM lookupAndExpect(ITEM item) {
        KEY key = (KEY) this.primaryKeyGetter.apply(item);
        ITEM oldItem = this.get(key);

        if (oldItem == null) {
            complain(sprintf("An original item was not in the repo %s", item));

        }
        return oldItem;
    }

    private ITEM lookupAndExpectByKey(KEY key) {
        ITEM oldItem = this.get(key);

        if (oldItem == null) {
            complain(sprintf("An original item was not in the repo at this key %s", key));

        }
        return oldItem;
    }

    private void invalidateIndex(String property, ITEM item) {


        LookupIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            index.delete(item);
        }

        index = this.lookupIndexMap.get(property);
        if (index != null) {
            index.delete(item);
        }

    }

    private void validateIndex(String property, ITEM item) {
        LookupIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            index.add(item);
        }

        index = this.lookupIndexMap.get(property);
        if (index != null) {
            index.add(item);
        }

    }


    protected ITEM copy(ITEM item) {
        return item;
    }


    @Override
    public void modify(ITEM item, String property, Object value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setObject(item, value);
        validateIndex(property, item);

    }

    @Override
    public void modifyByValue(ITEM item, String property, String value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setValue(item, value);
        validateIndex(property, item);

    }

    @Override
    public void modify(ITEM item, String property, int value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setInt(item, value);
        validateIndex(property, item);
    }

    @Override
    public void modify(ITEM item, String property, long value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setLong(item, value);
        validateIndex(property, item);
    }

    @Override
    public void modify(ITEM item, String property, char value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setChar(item, value);
        validateIndex(property, item);
    }

    @Override
    public void modify(ITEM item, String property, short value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setShort(item, value);
        validateIndex(property, item);
    }

    @Override
    public void modify(ITEM item, String property, byte value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setByte(item, value);
        validateIndex(property, item);
    }

    @Override
    public void modify(ITEM item, String property, float value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setFloat(item, value);
        validateIndex(property, item);
    }

    @Override
    public void modify(ITEM item, String property, double value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setDouble(item, value);
        validateIndex(property, item);
    }

    @Override
    public void modify(ITEM item, ValueSetter... values) {
        item = lookupAndExpect(item);
        for (ValueSetter value : values) {
            invalidateIndex(value.getName(), item);
            value.doSet(this, item);
            validateIndex(value.getName(), item);
        }

    }

    @Override
    public void update(KEY key, String property, Object value) {
        ITEM item = lookupAndExpectByKey(key);
        invalidateIndex(property, item);
        fields.get(property).setObject(item, value);
        validateIndex(property, item);
    }

    @Override
    public void updateByValue(KEY key, String property, String value) {
        ITEM item = lookupAndExpectByKey(key);
        invalidateIndex(property, item);
        fields.get(property).setValue(item, value);
        validateIndex(property, item);
    }

    @Override
    public void update(KEY key, String property, int value) {
        ITEM item = lookupAndExpectByKey(key);
        invalidateIndex(property, item);
        fields.get(property).setInt(item, value);
        validateIndex(property, item);
    }

    @Override
    public void update(KEY key, String property, long value) {
        ITEM item = lookupAndExpectByKey(key);
        invalidateIndex(property, item);
        fields.get(property).setLong(item, value);
        validateIndex(property, item);
    }

    @Override
    public void update(KEY key, String property, char value) {
        ITEM item = lookupAndExpectByKey(key);
        invalidateIndex(property, item);
        fields.get(property).setChar(item, value);
        validateIndex(property, item);
    }

    @Override
    public void update(KEY key, String property, short value) {
        ITEM item = lookupAndExpectByKey(key);
        invalidateIndex(property, item);
        fields.get(property).setShort(item, value);
        validateIndex(property, item);
    }

    @Override
    public void update(KEY key, String property, byte value) {
        ITEM item = lookupAndExpectByKey(key);
        invalidateIndex(property, item);
        fields.get(property).setByte(item, value);
        validateIndex(property, item);
    }

    @Override
    public void update(KEY key, String property, float value) {
        ITEM item = lookupAndExpectByKey(key);
        invalidateIndex(property, item);
        fields.get(property).setFloat(item, value);
        validateIndex(property, item);
    }

    @Override
    public void update(KEY key, String property, double value) {
        ITEM item = lookupAndExpectByKey(key);
        invalidateIndex(property, item);
        fields.get(property).setDouble(item, value);
        validateIndex(property, item);
    }

    @Override
    public void update(KEY key, ValueSetter... values) {
        ITEM item = lookupAndExpectByKey(key);

        for (ValueSetter value : values) {
            invalidateIndex(value.getName(), item);
            value.doSet(this, item);
            validateIndex(value.getName(), item);
        }
    }

    @Override
    public Object readObject(KEY key, String property) {
        ITEM item = this.get(key);
        return this.fields.get(property).getObject(item);
    }


    @Override
    public <T> T readValue(KEY key, String property, Class<T> type) {
        ITEM item = this.get(key);
        return (T) this.fields.get(property).getObject(item);
    }

    @Override
    public int readInt(KEY key, String property) {
        ITEM item = this.get(key);
        return this.fields.get(property).getInt(item);
    }

    @Override
    public long readLong(KEY key, String property) {
        ITEM item = this.get(key);
        return this.fields.get(property).getLong(item);
    }

    @Override
    public char readChar(KEY key, String property) {
        ITEM item = this.get(key);
        return this.fields.get(property).getChar(item);
    }

    @Override
    public short readShort(KEY key, String property) {
        ITEM item = this.get(key);
        return this.fields.get(property).getShort(item);
    }

    @Override
    public byte readByte(KEY key, String property) {
        ITEM item = this.get(key);
        return this.fields.get(property).getByte(item);
    }

    @Override
    public float readFloat(KEY key, String property) {
        ITEM item = this.get(key);
        return this.fields.get(property).getFloat(item);

    }

    @Override
    public double readDouble(KEY key, String property) {
        ITEM item = this.get(key);
        return this.fields.get(property).getDouble(item);

    }

    @Override
    public int count(KEY key, String property, int value) {

        SearchIndex index = this.searchIndexMap.get(property);

        if (index == null) {
            die("No searchIndex was found so you can't do a count for \n " +
                    "key %s \t property %s \t value %s", key, property, value);
        }

        return index.count(key);

    }

    @Override
    public int count(KEY key, String property, short value) {
        SearchIndex index = this.searchIndexMap.get(property);

        if (index == null) {
            die("No searchIndex was found so you can't do a count for \n " +
                    "key %s \t property %s \t value %s", key, property, value);
        }

        return index.count(key);
    }

    @Override
    public int count(KEY key, String property, byte value) {
        SearchIndex index = this.searchIndexMap.get(property);

        if (index == null) {
            die("No searchIndex was found so you can't do a count for \n " +
                    "key %s \t property %s \t value %s", key, property, value);
        }

        return index.count(key);
    }

    @Override
    public int count(KEY key, String property, long value) {
        SearchIndex index = this.searchIndexMap.get(property);

        if (index == null) {
            die("No searchIndex was found so you can't do a count for \n " +
                    "key %s \t property %s \t value %s", key, property, value);
        }

        return index.count(key);
    }

    @Override
    public int count(KEY key, String property, char value) {
        SearchIndex index = this.searchIndexMap.get(property);

        if (index == null) {
            die("No searchIndex was found so you can't do a count for \n " +
                    "key %s \t property %s \t value %s", key, property, value);
        }

        return index.count(key);
    }

    @Override
    public int count(KEY key, String property, float value) {
        SearchIndex index = this.searchIndexMap.get(property);

        if (index == null) {
            die("No searchIndex was found so you can't do a count for \n " +
                    "key %s \t property %s \t value %s", key, property, value);
        }

        return index.count(key);
    }

    @Override
    public int count(KEY key, String property, double value) {
        SearchIndex index = this.searchIndexMap.get(property);

        if (index == null) {
            die("No searchIndex was found so you can't do a count for \n " +
                    "key %s \t property %s \t value %s", key, property, value);
        }

        return index.count(key);
    }

    @Override
    public int count(KEY key, String property, Object value) {
        SearchIndex index = this.searchIndexMap.get(property);

        if (index == null) {
            die("No searchIndex was found so you can't do a count for \n " +
                    "key %s \t property %s \t value %s", key, property, value);
        }

        return index.count(key);
    }

    @Override
    public <T> T max(KEY key, String property, Class<T> type) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.max();
            if (item != null) {
                return (T) this.fields.get(property).getValue(item);
            }
        }
        return null;
    }

    @Override
    public String maxString(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.max();
            if (item != null) {
                return (String) this.fields.get(property).getObject(item);
            }
        }
        return null;
    }

    @Override
    public Number maxNumber(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.max();
            if (item != null) {
                return (Number) this.fields.get(property).getValue(item);
            }
        }
        return Double.NaN;
    }

    @Override
    public int maxInt(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.max();
            if (item != null) {
                return this.fields.get(property).getInt(item);
            }
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public long maxLong(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.max();
            if (item != null) {
                return this.fields.get(property).getLong(item);
            }
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public double maxDouble(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.max();
            if (item != null) {
                return this.fields.get(property).getDouble(item);
            }
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public <T> T min(KEY key, String property, Class<T> type) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.min();
            if (item != null) {
                return (T) this.fields.get(property).getValue(item);
            }
        }
        return null;
    }

    @Override
    public String minString(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.min();
            if (item != null) {
                return (String) this.fields.get(property).getObject(item);
            }
        }
        return "";
    }

    @Override
    public Number minNumber(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.min();
            if (item != null) {
                return (Number) this.fields.get(property).getValue(item);
            }
        }
        return Double.NaN;
    }

    @Override
    public int minInt(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.min();
            if (item != null) {
                return this.fields.get(property).getInt(item);
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public long minLong(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.min();
            if (item != null) {
                return this.fields.get(property).getLong(item);
            }
        }
        return Long.MAX_VALUE;
    }

    @Override
    public double minDouble(KEY key, String property) {
        SearchIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            ITEM item = (ITEM) index.min();
            if (item != null) {
                return this.fields.get(property).getDouble(item);
            }
        }
        return Double.MAX_VALUE;
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, Object compare, Object value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getObject(item).equals(compare)) {
            invalidateIndex(property, item);
            field.setObject(item, value);
            set = true;
            validateIndex(property, item);
        }
        return set;
    }


    @Override
    public boolean compareAndUpdate(KEY key, String property, int compare, int value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getInt(item) == (compare)) {
            invalidateIndex(property, item);
            field.setInt(item, value);
            set = true;
            validateIndex(property, item);
        }
        return set;
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, long compare, long value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getLong(item) == (compare)) {
            invalidateIndex(property, item);
            field.setLong(item, value);
            set = true;
            validateIndex(property, item);
        }
        return set;
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, char compare, char value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getChar(item) == (compare)) {
            invalidateIndex(property, item);
            field.setChar(item, value);
            set = true;
            validateIndex(property, item);
        }
        return set;
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, short compare, short value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getShort(item) == (compare)) {
            invalidateIndex(property, item);
            field.setShort(item, value);
            set = true;
            validateIndex(property, item);
        }
        return set;

    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, byte compare, byte value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getByte(item) == (compare)) {
            invalidateIndex(property, item);
            field.setByte(item, value);
            set = true;
            validateIndex(property, item);
        }
        return set;
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, float compare, float value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getFloat(item) == (compare)) {
            invalidateIndex(property, item);
            field.setFloat(item, value);
            set = true;
            validateIndex(property, item);
        }
        return set;
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, double compare, double value) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getDouble(item) == (compare)) {
            invalidateIndex(property, item);
            field.setDouble(item, value);
            set = true;
            validateIndex(property, item);
        }
        return set;
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, int compare) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getInt(item) == (compare)) {
            invalidateIndex(property, item);
            field.setInt(item, (compare + 1));
            set = true;
            validateIndex(property, item);
        }
        return set;

    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, long compare) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getLong(item) == (compare)) {
            invalidateIndex(property, item);
            field.setLong(item, (compare + 1));
            set = true;
            validateIndex(property, item);
        }
        return set;
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, short compare) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getShort(item) == (compare)) {
            invalidateIndex(property, item);
            field.setShort(item, (short) (compare + 1));
            set = true;
            validateIndex(property, item);
        }
        return set;
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, byte compare) {
        ITEM item = lookupAndExpectByKey(key);
        FieldAccess field = fields.get(property);
        boolean set = false;
        if (field.getByte(item) == (compare)) {
            invalidateIndex(property, item);
            field.setByte(item, (byte) (compare + 1));
            set = true;
            validateIndex(property, item);
        }
        return set;
    }

    @Override
    public void updateByFilter(String property, Object value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilterUsingValue(String property, String value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modifyByValue(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, int value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, long value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, char value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, short value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, byte value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, float value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, double value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(List<ValueSetter> values, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {

            for (ValueSetter value : values) {
                invalidateIndex(value.getName(), item);
                value.doSet(this, item);
                validateIndex(value.getName(), item);
            }
        }
    }


    @Override
    public List<ITEM> query(Expression... expressions) {
        if (expressions == null || expressions.length == 0) {
            return this.all();
        } else {
            return (List<ITEM>) this.filter.filter(this.lookupIndexMap, this.searchIndexMap, expressions);
        }
    }

    @Override
    public List<ITEM> sortedQuery(final String sortBy, Expression... expressions) {
        List<ITEM> results = this.query(expressions);
        Function<ITEM, KEY> func = new Function<ITEM, KEY>() {
            @Override
            public KEY apply(ITEM item) {
                return (KEY) ((Map) item).get(sortBy);
            }
        };
        return new SearchIndexDefault(results, func).all();
    }

    @Override
    public List<Map<String, Object>> queryAsMaps(Expression... expressions) {
        List<ITEM> items = this.query(expressions);
        List<Map<String, Object>> results = new ArrayList<>(items.size());
        for (ITEM item : items) {
            results.add(toMap(item));
        }
        return results;
    }

    @Override
    public List<Map<String, Object>> sortedQuery(final String sortBy, List<Selector> selectors, Expression... expressions) {

        final List<Map<String, Object>> results = query(selectors, expressions);
        Function<ITEM, KEY> func = new Function<ITEM, KEY>() {
            @Override
            public KEY apply(ITEM item) {
                return (KEY) ((Map) item).get(sortBy);
            }
        };
        return new SearchIndexDefault(results, func).all();
    }

    private void visit(KEY key, ITEM item, Visitor<KEY, ITEM> visitor, Object o, List<String> path, int levels) {
        if (o == null) {
            return;
        }
        levels++;
        if (levels > 20) {
            return;
        }
        visitor.visit(key, item, o, this, path);


        if (o.getClass().isPrimitive()) {
            return;
        }


        if (o.getClass().getName().startsWith("java")) {
            return;
        }


        if (isArray(o) || o instanceof Collection) {

            int index = 0;
            Iterator iterator = Utils.iterator(o);
            while (iterator.hasNext()) {
                path.add(sprintf("[%s]", index));
                Object objectItem = iterator.next();
                visit(key, item, visitor, objectItem, path, levels);
                path.remove(path.size() - 1);
                index++;

            }

        }

        Map<String, FieldAccess> accessorFields = Reflection.getAllAccessorFields(o.getClass());
        for (FieldAccess field : accessorFields.values()) {
            if (field.isStatic()) {
                continue;
            }
            path.add(field.getName());
            visit(key, item, visitor, field.getValue(o), path, levels);
            path.remove(path.size() - 1);

        }


    }

    @Override
    public void query(Visitor<KEY, ITEM> visitor, Expression... expressions) {
        List<ITEM> items = this.query(expressions);
        for (ITEM item : items) {
            KEY key = (KEY) this.primaryKeyGetter.apply(item);
            int levels = 0;
            visit(key, item, visitor, item, list("root"), levels);
        }

    }

    @Override
    public void sortedQuery(Visitor<KEY, ITEM> visitor, String sortBy, Expression... expressions) {

        List<ITEM> items = this.sortedQuery(sortBy, expressions);
        for (ITEM item : items) {
            KEY key = (KEY) this.primaryKeyGetter.apply(item);
            int levels = 0;
            visit(key, item, visitor, item, list("root"), levels);
        }

    }

    @Override
    public List<Map<String, Object>> query(List<Selector> selectors, Expression... expressions) {

        List<ITEM> results = this.query(expressions);
        List<Map<String, Object>> rows = new ArrayList<>(results.size());

        for (Selector s : selectors) {
            s.handleStart(results);
        }


        int index = 0;
        for (ITEM item : results) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (Selector s : selectors) {
                s.handleRow(index, row, item, fields);
            }
            index++;
            rows.add(row);
        }

        for (Selector s : selectors) {
            s.handleComplete(rows);
        }

        return rows;
    }

    @Override
    public List<ITEM> all() {
        return this.lookupIndexMap.get(primaryKeyName).all();
    }


    @Override
    public boolean add(ITEM item) {
        item = copy(item);
        for (LookupIndex index : indexes) {
            index.add(item);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        KEY key = null;
        ITEM item = null;
        try {
            key = (KEY) object;
            removeByKey(key);
        } catch (ClassCastException ex) {
            item = (ITEM) object;
            delete(item);
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        for (Object object : c) {
            KEY key = null;
            ITEM item = null;
            try {
                key = (KEY) object;
                item = get(key);
            } catch (ClassCastException ex) {
                ITEM itemArg = (ITEM) object;
                key = this.primaryKeyGetter.apply(itemArg);
                item = get(key);
            }
            if (item == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        this.removeAllAsync((Collection<ITEM>) c);
        return true;
    }

    @Override
    public boolean removeIf(Predicate<? super ITEM> filter) {
        List<ITEM> all = this.all();
        boolean removedSum = false;
        for (ITEM item : all) {
            boolean test = filter.test(item);
            if (test) {
                this.delete(item);
            }
        }
        return removedSum;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (Object object : c) {
            KEY key = null;
            ITEM item = null;
            try {
                key = (KEY) object;
                item = get(key);
            } catch (ClassCastException ex) {
                item = (ITEM) object;
            }
            if (item == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (LookupIndex index : indexes) {
            index.clear();
        }
    }

    @Override
    public Spliterator<ITEM> spliterator() {
        return this.primaryIndex.toCollection().spliterator();
    }

    @Override
    public Stream<ITEM> stream() {
        return this.primaryIndex.toCollection().stream();
    }

    @Override
    public Stream<ITEM> parallelStream() {
        return this.primaryIndex.toCollection().parallelStream();
    }

    @Override
    public boolean delete(ITEM item) {
        for (LookupIndex index : indexes) {
            index.delete((ITEM) item);
        }
        return true;
    }

    @Override
    public void setFields(Map<String, FieldAccess> fields) {
        this.fields = fields;
    }

    @Override
    public void setPrimaryKeyName(String primaryKey) {
        this.primaryKeyName = primaryKey;
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void init() {


        primaryIndex = (UniqueSearchIndex<KEY, ITEM>) this.lookupIndexMap.get(this.primaryKeyName);

    }


    @Override
    public int size() {
        return this.lookupIndexMap.get(this.primaryKeyName).size();
    }

    @Override
    public Collection<ITEM> toCollection() {
        return this.primaryIndex.toCollection();
    }

    @Override
    public boolean isEmpty() {
        return this.primaryIndex.toCollection().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        KEY key = null;
        ITEM item = null;
        try {
            key = (KEY) object;
            item = get(key);
        } catch (ClassCastException ex) {
            ITEM itemArg = (ITEM) object;
            key = this.primaryKeyGetter.apply(itemArg);
            item = get(key);
        }
        if (item == null) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Iterator<ITEM> iterator() {
        return primaryIndex.toCollection().iterator();
    }

    @Override
    public void forEach(Consumer<? super ITEM> action) {
        primaryIndex.toCollection().forEach(action);
    }

    @Override
    public Object[] toArray() {
        return primaryIndex.toCollection().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return primaryIndex.toCollection().toArray(a);
    }

}
