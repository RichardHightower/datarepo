package org.datarepo.impl;

import org.datarepo.LookupIndex;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.*;

public class UniqueLookupIndex <KEY, ITEM> implements LookupIndex<KEY, ITEM> {

    protected Function<ITEM, KEY> keyGetter;
    protected Map<KEY, ITEM> map =  new HashMap<>();
    private Logger log = log(UniqueLookupIndex.class);

    @Override
    public ITEM get(KEY key) {
        return map.get(key);
    }

    @Override
    public void setKeyGetter(Function<ITEM, KEY> keyGetter) {
        this.keyGetter = keyGetter;
    }

    @Override
    public void add(ITEM item) {
        notNull(item);

        if (isDebug(log)) {
            debug(log, "add item = %s", item);
        }

        KEY key = keyGetter.apply(item);

        notNull(key);

        if (map.containsKey(key)) {
            die("this index already contains this key %s", key);
        }



        map.put(key, item);
    }

    @Override
    public void remove(ITEM item) {
        notNull(item);
        map.remove(keyGetter.apply(item));
    }

    @Override
    public List<ITEM> all() {
        return new ArrayList<>(this.map.values());
    }

    @Override
    public List<ITEM> getAll(KEY key) {
        return  Collections.singletonList(this.get(key));
    }

    @Override
    public int size() {
        return this.map.size();
    }


}
