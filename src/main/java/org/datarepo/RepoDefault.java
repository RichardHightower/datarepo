package org.datarepo;

import org.datarepo.criteria.Expression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default Repo implementation.
 * @param <KEY> primary key or handle of object.
 * @param <ITEM> item that this repo holds.
 */
public class RepoDefault <KEY, ITEM> implements RepoComposer, Repo<KEY, ITEM> {

    private Logger log = Logger.getLogger("org.datarepo.RepoDefault");
    private Map<String, LookupIndex> lookupIndexMap = new HashMap<>();
    private Map<String, SearchIndex> searchIndexMap = new HashMap<>();
    private Map<String, Modifier> modifiers = new HashMap<>();

    private List<LookupIndex> indexes = new ArrayList<LookupIndex>();
    private KeyGetter <KEY, ITEM> primaryKeyGetter;
    private String primaryKeyName;
    private Filter filter;

    @Override
    public void addSearchIndex(String name, SearchIndex si) {
        searchIndexMap.put(name, si);
        indexes.add(si);
    }

    @Override
    public void addLookupIndex(String name, LookupIndex si) {
        lookupIndexMap.put(name, si);
        indexes.add(si);
    }

    @Override
    public void setPrimaryKeyGetter(KeyGetter getter) {
        this.primaryKeyGetter = getter;
    }


    @Override
    public ITEM get(KEY key) {
        LookupIndex lookupIndex = lookupIndexMap.get(this.primaryKeyName);
        return (ITEM) lookupIndex.get(key);
    }

    @Override
    public void modify(ITEM item) {
        KEY key = (KEY) this.primaryKeyGetter.getKey(item);
        LookupIndex lookupIndex = lookupIndexMap.get(this.primaryKeyName);
        ITEM oldItem = (ITEM) lookupIndex.get(key);
        if (oldItem != null) {
            lookupIndex.remove(oldItem);
        }

        this.add(item);

        invalidate();
    }

    private void invalidate() {
        for (LookupIndex index : indexes) {
            index.invalidate();
        }
    }
    private void invalidate(String property) {
        LookupIndex index = this.searchIndexMap.get(property);
        if (index!=null) {
            index.invalidate();
        }

        index = this.lookupIndexMap.get(property);
        if (index!=null) {
            index.invalidate();
        }

    }



    //Move this to a strategy class at some point TODO
    private ITEM copy (ITEM item)  {
        if (item instanceof Cloneable) {
            try {
                Method method = item.getClass().getMethod("clone", null);
                return (ITEM) method.invoke(item, null);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                log.log(Level.WARNING, "Unable to get clone method", ex);
                return item;
            }
        }
        return item;
    }


    @Override
    public void modify(String property, Object value) {
        modifiers.get(property).setValue(value);
        invalidate(property);
    }

    @Override
    public void modify(String property, String value) {
        modifiers.get(property).setString(value);
        invalidate(property);

    }

    @Override
    public void modify(String property, int value) {
        modifiers.get(property).setInt(value);
        invalidate(property);

    }

    @Override
    public void modify(String property, long value) {
        modifiers.get(property).setLong(value);
        invalidate(property);

    }


    @Override
    public List filter(Expression... expressions) {
        return this.filter.filter(this.lookupIndexMap, this.searchIndexMap, expressions);
    }


    @Override
    public void add(ITEM item) {
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
    public void setPrimaryKeyName(String primaryKey) {
        this.primaryKeyName = primaryKey;
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
