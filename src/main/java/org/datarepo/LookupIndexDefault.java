package org.datarepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    public void setKeyGetter(KeyGetter<KEY, ITEM> keyGetter) {
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
                    list.add((ITEM)value);
                }
            }
        }
        return list;
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
            return;
        }
        mv.remove(item);

        if (mv.size() == 0) {
            map.remove(key);
        }

    }
}
