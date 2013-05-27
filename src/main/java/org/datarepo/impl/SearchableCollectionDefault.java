package org.datarepo.impl;

import org.datarepo.Filter;
import org.datarepo.LookupIndex;
import org.datarepo.SearchIndex;
import org.datarepo.SearchableCollection;
import org.datarepo.query.Expression;
import org.datarepo.query.Selector;
import org.datarepo.query.Visitor;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Reflection;
import org.datarepo.spi.SearchableCollectionComposer;
import org.datarepo.utils.Utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.datarepo.reflection.Reflection.toMap;
import static org.datarepo.utils.Utils.*;

public class SearchableCollectionDefault<KEY, ITEM> implements SearchableCollection<KEY, ITEM>, SearchableCollectionComposer {

    protected Logger log = Utils.log(RepoDefault.class);
    protected Map<String, LookupIndex> lookupIndexMap = new LinkedHashMap<>();
    protected Map<String, SearchIndex> searchIndexMap = new LinkedHashMap<>();
    protected List<LookupIndex> indexes = new ArrayList<LookupIndex>();
    protected Filter filter;

    protected Map<String, FieldAccess> fields = new LinkedHashMap<>();
    protected UniqueLookupIndex<KEY, ITEM> primaryIndex;

    protected Function<ITEM, KEY> primaryKeyGetter;
    protected String primaryKeyName;

    public ITEM get(KEY key) {
        LookupIndex lookupIndex = primaryIndex;
        return (ITEM) lookupIndex.get(key);
    }

    public KEY getKey(ITEM item) {
        return (KEY) this.primaryKeyGetter.apply(item);
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
        return new SearchIndexDefault(null, results, func).all();
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
        return new SearchIndexDefault(null, results, func).all();
    }

    private void visit(KEY key, ITEM item, Visitor<KEY, ITEM> visitor, Object o, List<String> path, int levels) {
        if (o == null) {
            return;
        }
        levels++;
        if (levels > 20) {
            return;
        }
        visitor.visit(key, item, o, path);


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

    public void invalidateIndex(String property, ITEM item) {


        LookupIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            index.delete(item);
        }

        index = this.lookupIndexMap.get(property);
        if (index != null) {
            index.delete(item);
        }

    }

    public void validateIndex(String property, ITEM item) {
        LookupIndex index = this.searchIndexMap.get(property);
        if (index != null) {
            index.add(item);
        }

        index = this.lookupIndexMap.get(property);
        if (index != null) {
            index.add(item);
        }

    }


    @Override
    public void clear() {
        for (LookupIndex index : indexes) {
            index.clear();
        }
    }

    @Override
    public boolean delete(ITEM item) {
        for (LookupIndex index : indexes) {
            index.delete((ITEM) item);
        }
        return true;
    }

    public boolean add(ITEM item) {
        for (LookupIndex index : indexes) {
            index.add(item);
        }
        return true;
    }

    public void setFilter(org.datarepo.Filter filter) {
        this.filter = filter;
    }


    public void addSearchIndex(String name, SearchIndex si) {
        config(log, "search index added name %s", name);
        searchIndexMap.put(name, si);
        indexes.add(si);
    }

    public void addLookupIndex(String name, LookupIndex si) {
        config(log, "lookup index added name %s", name);

        lookupIndexMap.put(name, si);
        indexes.add(si);
    }

    public List<ITEM> all() {
        return primaryIndex.all();
    }


    public Spliterator<ITEM> spliterator() {
        return this.primaryIndex.toCollection().spliterator();
    }

    public Stream<ITEM> stream() {
        return this.primaryIndex.toCollection().stream();
    }

    public Stream<ITEM> parallelStream() {
        return this.primaryIndex.toCollection().parallelStream();
    }


    public void setPrimaryKeyName(String primaryKey) {
        this.primaryKeyName = primaryKey;
    }

    public Collection<ITEM> toCollection() {
        return this.primaryIndex.toCollection();
    }

    public boolean isEmpty() {
        return this.primaryIndex.toCollection().isEmpty();
    }


    public Iterator<ITEM> iterator() {
        return primaryIndex.toCollection().iterator();
    }

    public void forEach(Consumer<? super ITEM> action) {
        primaryIndex.toCollection().forEach(action);
    }

    public Object[] toArray() {
        return primaryIndex.toCollection().toArray();
    }

    public <T> T[] toArray(T[] a) {
        return primaryIndex.toCollection().toArray(a);
    }


    public void setPrimaryKeyGetter(Function getter) {
        config(log, "primary key getter set %s", getter);

        this.primaryKeyGetter = getter;
    }

    @Override
    public void init() {
        this.primaryIndex = (UniqueLookupIndex<KEY, ITEM>) this.lookupIndexMap.get(this.primaryKeyName);
    }

    @Override
    public void setFields(Map<String, FieldAccess> fields) {
        this.fields = fields;
    }


    public int size() {
        return primaryIndex.size();
    }

    public boolean addAll(Collection<? extends ITEM> items) {
        for (ITEM item : items) {
            this.add(item);
        }
        return true;
    }

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

    public void removeByKey(KEY key) {
        ITEM item = this.get(key);
        this.delete(item);
    }


    public boolean containsAll(Collection<?> c) {

        for (Object object : c) {
            KEY key = null;
            ITEM item = null;
            try {
                key = (KEY) object;
                item = get(key);
            } catch (ClassCastException ex) {
                ITEM itemArg = (ITEM) object;
                key = this.getKey(itemArg);
                item = get(key);
            }
            if (item == null) {
                return true;
            }
        }
        return false;
    }


    public boolean removeAll(Collection<?> items) {
        for (Object o : items) {
            remove(o);
        }
        return true;
    }

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


    public boolean contains(Object o) {
        KEY key = null;
        ITEM item = null;
        try {
            key = (KEY) object;
            item = get(key);
        } catch (ClassCastException ex) {
            ITEM itemArg = (ITEM) object;
            key = getKey(itemArg);
            item = get(key);
        }
        if (item == null) {
            return true;
        } else {
            return false;
        }

    }


}
