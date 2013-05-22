package org.datarepo.impl;

import org.datarepo.*;
import org.datarepo.Filter;
import org.datarepo.criteria.Expression;
import org.datarepo.criteria.Selector;
import org.datarepo.criteria.ValueSetter;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Reflection;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.*;

/**
 * Default Repo implementation.
 *
 * @param <KEY>  primary key or handle of object.
 * @param <ITEM> item that this repo holds.
 */
public class RepoDefault<KEY, ITEM> implements RepoComposer, Repo<KEY, ITEM> {

    private Logger log = log(RepoDefault.class);
    private Map<String, LookupIndex> lookupIndexMap = new HashMap<>();
    private Map<String, SearchIndex> searchIndexMap = new HashMap<>();
    private Map<String, FieldAccess> fields = new HashMap<>();

    private List<LookupIndex> indexes = new ArrayList<LookupIndex>();
    private Function<ITEM, KEY> primaryKeyGetter;
    private String primaryKeyName;
    private Filter filter;

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
            index.remove(item);
        }

        index = this.lookupIndexMap.get(property);
        if (index != null) {
            index.remove(item);
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


    private ITEM copy(ITEM item) {
        return Reflection.copy(item);
    }


    @Override
    public void modify(ITEM item, String property, Object value) {
        item = lookupAndExpect(item);
        invalidateIndex(property, item);
        fields.get(property).setValue(item, value);
        validateIndex(property, item);

    }

    @Override
    public void modify(ITEM item, String property, String value) {
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
        fields.get(property).setValue(item, value);
        validateIndex(property, item);
    }

    @Override
    public void update(KEY key, String property, String value) {
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
    public void updateByFilter(String property, Object value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, String value, Expression... expressions) {
        List<ITEM> items = query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
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
    public List<Map<String, Object>> sortedQuery(final String sortBy, List<Selector> selectors, Expression... expressions) {

        final List<Map<String, Object>> results = query(selectors, expressions);
        Function<ITEM, KEY> func = new Function<ITEM, KEY>(){
            @Override
            public KEY apply(ITEM item) {
                return (KEY)((Map)item).get(sortBy);
            }
        };
        return new SearchIndexDefault(results, func).all();
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
            index ++;
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
    public void add(ITEM item) {
        item = copy(item);
        for (LookupIndex index : indexes) {
            index.add(item);
        }
    }

    @Override
    public void remove(ITEM item) {
        for (LookupIndex index : indexes) {
            index.remove(item);
        }
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
}
