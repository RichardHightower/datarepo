package org.datarepo.impl;

import org.datarepo.LookupIndex;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.*;
import static org.datarepo.utils.Utils.debug;

/**
 * A really simple lookup index that uses a standard java.util.HashMap.
 *
 * @param <KEY>
 * @param <ITEM>
 */
public class LookupIndexDefault<KEY, ITEM> implements LookupIndex<KEY, ITEM> {


    protected Function<ITEM, KEY> keyGetter;
    protected Map<KEY, MultiValue<ITEM>> map = new ConcurrentHashMap<>();
    private Logger log = log(LookupIndexDefault.class);

    public LookupIndexDefault() {
    }


    public void setKeyGetter(Function<ITEM, KEY> keyGetter) {
        notNull(keyGetter);
        this.keyGetter = keyGetter;
    }

    @Override
    public List<ITEM> all() {
        List<ITEM> list = new ArrayList<>(map.size());
        for (MultiValue<ITEM> values : map.values()) {
            if (values.value != null) {
                list.add(values.value);
            } else {
                for (Object value : values.values) {
                    list.add((ITEM) value);
                }
            }
        }
        return list;
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
        MultiValue<ITEM> mv = map.get(key);
        if (mv == null) {
            return null;
        } else {
            return mv.getValue();
        }
    }


    public List<ITEM> getAll(KEY key) {
        if (isDebug(log)) {
            debug(log, "key = %s", key);
        }
        MultiValue<ITEM> mv = map.get(key);
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


    @Override
    public boolean add(ITEM item) {
        if (isDebug(log)) {
            debug(log, "add item = %s", item);
        }

        KEY key = keyGetter.apply(item);
        MultiValue<ITEM> mv = map.get(key);
        if (mv == null) {
            mv = new MultiValue<ITEM>(item);
        } else {
            mv.add(item);
        }


        map.put(key, mv);
        return true;
    }

    @Override
    public boolean delete(ITEM item) {
        KEY key = keyGetter.apply(item);
        MultiValue<ITEM> mv = map.get(key);

        if (mv == null) {
            return false;
        }
        mv.remove(item);

        if (mv.size() == 0) {
            map.remove(key);
        }

        return true;

    }

    @Override
    public void clear() {
        this.map.clear();
    }

}
