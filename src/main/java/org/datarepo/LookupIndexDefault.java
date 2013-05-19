package org.datarepo;

import java.util.HashMap;
import java.util.Map;

/**
 * A really simple lookup index that uses a standard java.util.HashMap.
 * @param <KEY>
 * @param <ITEM>
 */
public class LookupIndexDefault <KEY, ITEM> implements  LookupIndex <KEY, ITEM> {

    protected KeyGetter <KEY, ITEM> keyGetter;
    protected Map<KEY, MultiValue<ITEM>> map =  new HashMap<>();

    public LookupIndexDefault () {
    }

    public LookupIndexDefault (KeyGetter <KEY, ITEM> keyGetter) {
        map =  new HashMap<>();
        this.keyGetter = keyGetter;

    }

    @Override
    public ITEM get(KEY key) {
        MultiValue<ITEM> mv =  map.get(key);
        if (mv == null) {
            return  null;
        } else {
            return mv.getValue();
        }
    }

    @Override
    public void invalidate() {
    }

    @Override
    public void add(ITEM item) {
        KEY key = keyGetter.getKey(item);
        MultiValue<ITEM> mv =  map.get(key);
        if (mv == null) {
            mv = new MultiValue<ITEM>(item);
        } else {
            mv.add(item);
        }



        map.put(key, mv);
    }

    @Override
    public void remove(ITEM item) {
        KEY key = keyGetter.getKey(item);
        MultiValue<ITEM> mv =  map.get(key);

        if (mv == null) {
            mv = new MultiValue<ITEM>(item);
        } else {
            mv.add(item);
        }



        map.put(key, mv);
    }
}
