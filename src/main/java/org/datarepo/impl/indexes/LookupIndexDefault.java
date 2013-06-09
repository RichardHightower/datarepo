package org.datarepo.impl.indexes;

import org.datarepo.LookupIndex;
import org.datarepo.spi.SPIFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.*;

/**
 * A really simple lookup index that uses a standard java.util.HashMap.
 *
 * @param <KEY>
 * @param <ITEM>
 */
public class LookupIndexDefault<KEY, ITEM> implements LookupIndex<KEY, ITEM> {


    protected Function<ITEM, KEY> keyGetter;
    protected Function<ITEM, KEY> primaryKeyGetter;

    protected Map<KEY, MultiValue> map;
    private Logger log = log(LookupIndexDefault.class);
    protected boolean storeKeyInIndexOnly;
    private Function<Object, KEY> keyTransformer;


    public LookupIndexDefault(Class<?> keyType) {
        if (keyType == null) {
            return;
        }
        map = SPIFactory.getMapCreatorFactory().get().createMap(keyType);

    }


    protected void addManyKeys(ITEM item, List<KEY> keys) {
        for (KEY key : keys) {
            if (key != null) {
                this.put(item, key);
            }
        }
    }

    @Override
    public boolean add(ITEM item) {
        if (isDebug(log)) {
            debug(log, "add item = %s", item);
        }

        KEY key = keyGetter.apply(item);
        if (key == null) {
            return false;
        }

        put(item, key);
        return true;

    }

    private void put(ITEM item, KEY key) {
        key = getKey(key);


        MultiValue mv = null;
        mv = map.get(key);


        if (storeKeyInIndexOnly) {
            Object primaryKey = primaryKeyGetter.apply(item);

            mv = mvCreateOrAddToMV(mv, primaryKey);
        } else {
            mv = mvCreateOrAddToMV(mv, item);
        }

        map.put(key, mv);
    }

    protected static MultiValue mvCreateOrAddToMV(MultiValue mv, Object obj) {
        return MultiValue.add(mv, obj);
    }


    protected final void removeManyKeys(ITEM item, List<KEY> keys) {
        for (KEY key : keys) {
            if (key != null) {
                removeKey(item, key);
            }
        }
    }

    @Override
    public boolean delete(ITEM item) {
        KEY key = keyGetter.apply(item);


        return removeKey(item, key);

    }

    private boolean removeKey(ITEM item, KEY key) {
        key = getKey(key);

        MultiValue mv = map.get(key);

        if (mv == null) {
            return false;
        }

        mv = MultiValue.remove(mv, item);

        if (mv == null) {
            map.remove(key);
        }
        return true;
    }


    public void setKeyGetter(Function<ITEM, KEY> keyGetter) {
        notNull(keyGetter);
        this.keyGetter = keyGetter;
    }

    public void setPrimaryKeyGetter(Function<ITEM, KEY> keyGetter) {
        notNull(keyGetter);
        storeKeyInIndexOnly = true;
        this.primaryKeyGetter = keyGetter;
    }

    @Override
    public List<ITEM> all() {
        List results = new ArrayList<>(map.size());
        for (MultiValue values : map.values()) {
            values.addTo(results);
        }
        return results;
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public Collection<ITEM> toCollection() {
        return (Collection<ITEM>) this.map.values();
    }


    @Override
    public ITEM get(KEY key) {

        key = getKey(key);

        if (isDebug(log)) {
            debug(log, "key = %s", key);
        }
        MultiValue mv = map.get(key);
        if (mv == null) {
            return null;
        } else {
            return (ITEM) mv.getValue();
        }
    }

    protected KEY getKey(KEY key) {
        if (keyTransformer != null) {
            key = this.keyTransformer.apply(key);
        }
        return key;
    }


    public List<ITEM> getAll(KEY key) {
        key = getKey(key);

        if (isDebug(log)) {
            debug(log, "key = %s", key);
        }
        MultiValue mv = map.get(key);
        if (mv == null) {
            return null;
        } else {
            return mv.getValues();
        }
    }

    @Override
    public boolean deleteByKey(KEY key) {
        key = getKey(key);

        this.map.remove(key);
        return true;
    }


    //TODO implement so we can store only primary keys in a index to make indexes smaller if
    //we ever decide to cache actual items
    @Override
    public boolean isPrimaryKeyOnly() {
        return storeKeyInIndexOnly;
    }

    @Override
    public void setInputKeyTransformer(Function<Object, KEY> func) {
        this.keyTransformer = func;
    }

    @Override
    public void init() {
    }


    @Override
    public void clear() {
        this.map.clear();
    }

}
