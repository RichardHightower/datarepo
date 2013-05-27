package org.datarepo.impl;

import org.datarepo.LookupIndex;
import org.datarepo.spi.SPIFactory;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.*;

public class UniqueLookupIndex<KEY, ITEM> implements LookupIndex<KEY, ITEM> {

    protected Function<ITEM, KEY> keyGetter;
    protected Map<KEY, ITEM> map = null;
    protected List<ITEM> items = new LinkedList();

    private Logger log = log(UniqueLookupIndex.class);

    public UniqueLookupIndex(Class<?> keyType) {
        if (keyType == null) {
            return;
        }
        map = SPIFactory.getMapCreatorFactory().get().createMap(keyType);

    }

    @Override
    public ITEM get(KEY key) {
        return map.get(key);
    }

    @Override
    public void setKeyGetter(Function<ITEM, KEY> keyGetter) {
        this.keyGetter = keyGetter;
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


        if (map.containsKey(key)) {
            die("this index already contains this key %s", key);
        }


        map.put(key, item);
        items.add(item);
        return true;
    }

    @Override
    public boolean delete(ITEM item) {
        map.remove(keyGetter.apply(item));
        return items.remove(item);
    }

    @Override
    public List<ITEM> all() {
        return new ArrayList<>(items);
    }

    @Override
    public List<ITEM> getAll(KEY key) {
        return Collections.singletonList(this.get(key));
    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public Collection<ITEM> toCollection() {
        return this.items;
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public boolean deleteByKey(KEY key) {
        this.map.remove(key);
        return true;
    }


}
