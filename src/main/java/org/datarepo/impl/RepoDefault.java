package org.datarepo.impl;

import org.datarepo.*;
import org.datarepo.query.Expression;
import org.datarepo.query.Selector;
import org.datarepo.query.ValueSetter;
import org.datarepo.query.Visitor;
import org.datarepo.spi.RepoComposer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Default Repo implementation.
 *
 * @param <KEY>  primary key or handle of object.
 * @param <ITEM> item that this repo holds.
 */
public class RepoDefault<KEY, ITEM> implements Repo<KEY, ITEM>, RepoComposer<KEY, ITEM> {

    private ObjectEditor<KEY, ITEM> editor;
    private SearchableCollection<KEY, ITEM> query;

    @Override
    public void updateByFilter(String property, Object value, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilterUsingValue(String property, String value, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {
            modifyByValue(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, int value, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, long value, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, char value, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, short value, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, byte value, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, float value, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(String property, double value, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {
            modify(item, property, value);
        }
    }

    @Override
    public void updateByFilter(List<ValueSetter> values, Expression... expressions) {
        List<ITEM> items = query.query(expressions);
        for (ITEM item : items) {

            for (ValueSetter value : values) {
                query.invalidateIndex(value.getName(), item);
                value.doSet(this, item);
                query.validateIndex(value.getName(), item);
            }
        }
    }


    @Override
    public ITEM get(KEY key) {
        return editor.get(key);
    }

    @Override
    public KEY getKey(ITEM item) {
        return editor.getKey(item);
    }

    @Override
    public void invalidateIndex(String property, ITEM item) {
        query.invalidateIndex(property, item);
    }

    @Override
    public void validateIndex(String property, ITEM item) {
        query.validateIndex(property, item);
    }

    @Override
    public Object readObject(KEY key, String property) {
        return query.readObject(key, property);
    }

    @Override
    public <T> T readValue(KEY key, String property, Class<T> type) {
        return query.readValue(key, property, type);
    }

    @Override
    public int readInt(KEY key, String property) {
        return query.readInt(key, property);
    }

    @Override
    public long readLong(KEY key, String property) {
        return query.readLong(key, property);
    }

    @Override
    public char readChar(KEY key, String property) {
        return query.readChar(key, property);
    }

    @Override
    public short readShort(KEY key, String property) {
        return query.readShort(key, property);
    }

    @Override
    public byte readByte(KEY key, String property) {
        return query.readByte(key, property);
    }

    @Override
    public float readFloat(KEY key, String property) {
        return query.readFloat(key, property);
    }

    @Override
    public double readDouble(KEY key, String property) {
        return query.readDouble(key, property);
    }

    @Override
    public int count(KEY key, String property, int value) {
        return query.count(key, property, value);
    }

    @Override
    public int count(KEY key, String property, short value) {
        return query.count(key, property, value);
    }

    @Override
    public int count(KEY key, String property, byte value) {
        return query.count(key, property, value);
    }

    @Override
    public int count(KEY key, String property, long value) {
        return query.count(key, property, value);
    }

    @Override
    public int count(KEY key, String property, char value) {
        return query.count(key, property, value);
    }

    @Override
    public int count(KEY key, String property, float value) {
        return query.count(key, property, value);
    }

    @Override
    public int count(KEY key, String property, double value) {
        return query.count(key, property, value);
    }

    @Override
    public int count(KEY key, String property, Object value) {
        return query.count(key, property, value);
    }

    @Override
    public <T> T max(KEY key, String property, Class<T> type) {
        return query.max(key, property, type);
    }

    @Override
    public String maxString(KEY key, String property) {
        return query.maxString(key, property);
    }

    @Override
    public Number maxNumber(KEY key, String property) {
        return query.maxNumber(key, property);
    }

    @Override
    public int maxInt(KEY key, String property) {
        return query.maxInt(key, property);
    }

    @Override
    public long maxLong(KEY key, String property) {
        return query.maxLong(key, property);
    }

    @Override
    public double maxDouble(KEY key, String property) {
        return query.maxDouble(key, property);
    }

    @Override
    public <T> T min(KEY key, String property, Class<T> type) {
        return query.min(key, property, type);
    }

    @Override
    public String minString(KEY key, String property) {
        return query.minString(key, property);
    }

    @Override
    public Number minNumber(KEY key, String property) {
        return query.minNumber(key, property);
    }

    @Override
    public int minInt(KEY key, String property) {
        return query.minInt(key, property);
    }

    @Override
    public long minLong(KEY key, String property) {
        return query.minLong(key, property);
    }

    @Override
    public double minDouble(KEY key, String property) {
        return query.minDouble(key, property);
    }

    @Override
    public List<ITEM> query(Expression... expressions) {
        return query.query(expressions);
    }

    @Override
    public List<ITEM> sortedQuery(String sortBy, Expression... expressions) {
        return query.sortedQuery(sortBy, expressions);
    }

    @Override
    public List<Map<String, Object>> queryAsMaps(Expression... expressions) {
        return query.queryAsMaps(expressions);
    }

    @Override
    public List<Map<String, Object>> query(List<Selector> selectors, Expression... expressions) {
        return query.query(selectors, expressions);
    }

    @Override
    public List<Map<String, Object>> sortedQuery(String sortBy, List<Selector> selectors, Expression... expressions) {
        return query.sortedQuery(sortBy, selectors, expressions);
    }

    @Override
    public void query(Visitor<KEY, ITEM> visitor, Expression... expressions) {
        query.query(visitor, expressions);
    }

    @Override
    public void sortedQuery(Visitor<KEY, ITEM> visitor, String sortBy, Expression... expressions) {
        query.query(visitor, expressions);
    }

    @Override
    public void put(ITEM item) {
        editor.put(item);
    }

    @Override
    public void removeByKey(KEY key) {
        editor.removeByKey(key);
    }

    @Override
    public void removeAll(ITEM... items) {
        editor.removeAll(items);
    }

    @Override
    public void removeAllAsync(Collection<ITEM> items) {
        editor.removeAllAsync(items);
    }

    @Override
    public void addAll(ITEM... items) {
        editor.addAll(items);
    }

    @Override
    public void addAllAsync(Collection<ITEM> items) {
        editor.addAllAsync(items);
    }

    @Override
    public void modifyAll(ITEM... items) {
        editor.modifyAll(items);
    }

    @Override
    public void modifyAll(Collection<ITEM> items) {
        editor.modifyAll(items);
    }

    @Override
    public void modify(ITEM item) {
        editor.modify(item);
    }

    @Override
    public void modifyByValue(ITEM item, String property, String value) {
        editor.modifyByValue(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, Object value) {
        editor.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, int value) {
        editor.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, long value) {
        editor.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, char value) {
        editor.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, short value) {
        editor.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, byte value) {
        editor.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, float value) {
        editor.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, String property, double value) {
        editor.modify(item, property, value);
    }

    @Override
    public void modify(ITEM item, ValueSetter... values) {
        editor.modify(item, values);
    }

    @Override
    public void updateByValue(KEY key, String property, String value) {
        editor.updateByValue(key, property, value);
    }

    @Override
    public void update(KEY key, String property, Object value) {
        editor.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, int value) {
        editor.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, long value) {
        editor.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, char value) {
        editor.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, short value) {
        editor.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, byte value) {
        editor.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, float value) {
        editor.update(key, property, value);
    }

    @Override
    public void update(KEY key, String property, double value) {
        editor.update(key, property, value);
    }

    @Override
    public void update(KEY key, ValueSetter... values) {
        editor.update(key, values);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, Object compare, Object value) {
        return editor.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, int compare, int value) {
        return editor.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, long compare, long value) {
        return editor.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, char compare, char value) {
        return editor.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, short compare, short value) {
        return editor.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, byte compare, byte value) {
        return editor.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, float compare, float value) {
        return editor.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndUpdate(KEY key, String property, double compare, double value) {
        return editor.compareAndUpdate(key, property, compare, value);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, int compare) {
        return editor.compareAndIncrement(key, property, compare);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, long compare) {
        return editor.compareAndIncrement(key, property, compare);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, short compare) {
        return editor.compareAndIncrement(key, property, compare);
    }

    @Override
    public boolean compareAndIncrement(KEY key, String property, byte compare) {
        return editor.compareAndIncrement(key, property, compare);
    }

    @Override
    public void addAll(List<ITEM> items) {
        editor.addAll(items);
    }

    @Override
    public boolean add(ITEM item) {
        return editor.add(item);
    }

    @Override
    public boolean remove(Object o) {
        return query.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return query.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends ITEM> c) {
        return query.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return query.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return query.retainAll(c);
    }

    @Override
    public boolean delete(ITEM item) {
        return editor.delete(item);
    }

    @Override
    public void addSearchIndex(String name, SearchIndex<?, ?> si) {
        query.addSearchIndex(name, si);
    }

    @Override
    public void addLookupIndex(String name, LookupIndex<?, ?> si) {
        query.addLookupIndex(name, si);
    }

    @Override
    public List<ITEM> all() {
        return editor.all();
    }

    @Override
    public int size() {
        return editor.size();
    }

    @Override
    public boolean isEmpty() {

        return query.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return query.contains(o);
    }

    @Override
    public Iterator<ITEM> iterator() {
        return query.iterator();
    }

    @Override
    public Object[] toArray() {
        return query.toArray();

    }

    @Override
    public <T> T[] toArray(T[] a) {
        return query.toArray(a);
    }

    @Override
    public Collection<ITEM> toCollection() {
        return editor.toCollection();
    }

    @Override
    public void clear() {
        editor.clear();
    }

    @Override
    public void setSearchableCollection(SearchableCollection<KEY, ITEM> searchableCollection) {
        this.query = searchableCollection;
    }

    @Override
    public void init() {
    }

    @Override
    public void setObjectEditor(ObjectEditor editor) {
        this.editor = editor;
    }
}
