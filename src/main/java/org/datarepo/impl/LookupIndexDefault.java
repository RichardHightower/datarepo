package org.datarepo.impl;

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


    public LookupIndexDefault(Class<?> keyType) {
        if (keyType == null) {
            return;
        }
        map = SPIFactory.getMapCreatorFactory().get().createMap(keyType);

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


        MultiValue mv = null;
        mv = map.get(key);


        if (storeKeyInIndexOnly) {
            Object primaryKey = primaryKeyGetter.apply(item);

            mv = mvCreateOrAddToMV(mv, primaryKey);
        } else {
            mv = mvCreateOrAddToMV(mv, item);
        }

        map.put(key, mv);
        return true;

    }

    private MultiValue mvCreateOrAddToMV(MultiValue mv, Object obj) {
        if (mv == null) {
            mv = new MultiValue(obj);
        } else {
            mv.add(obj);
        }
        return mv;
    }

    @Override
    public boolean delete(ITEM item) {
        KEY key = keyGetter.apply(item);
        MultiValue mv = map.get(key);

        if (mv == null) {
            return false;
        }
        mv.remove(item);

        if (mv.size() == 0) {
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
            if (values.value != null) {
                results.add(values.value);
            } else {
                for (Object value : values.values) {
                    results.add((ITEM) value);
                }
            }
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


    public List<ITEM> getAll(KEY key) {
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
    public void clear() {
        this.map.clear();
    }

}
