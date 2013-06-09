package org.datarepo.impl.indexes;

import java.util.*;

public class StringSearchIndexDefault<KEY, ITEM> extends SearchIndexDefault<KEY, ITEM> {

    NavigableMap<String, MultiValue> navigableMap = new TreeMap<>();

    public StringSearchIndexDefault(Class<?> keyType) {
        super(keyType);
    }

    @Override
    public boolean add(ITEM item) {
        KEY key = keyGetter.apply(item);
        String sKey;

        if (key instanceof String) {
            sKey = (String) key;
        } else {
            sKey = key.toString();
        }


        MultiValue mv = null;
        mv = map.get(key);


        if (storeKeyInIndexOnly) {
            Object primaryKey = primaryKeyGetter.apply(item);

            mv = mvCreateOrAddToMV(mv, primaryKey);
        } else {
            mv = mvCreateOrAddToMV(mv, item);
        }

        navigableMap.put(new StringBuilder(sKey).reverse().toString(), mv);
        return super.add(item);
    }

    @Override
    public boolean delete(ITEM item) {

        KEY key = keyGetter.apply(item);
        String sKey;

        if (key instanceof String) {
            sKey = (String) key;
        } else {
            sKey = key.toString();
        }

        sKey = new StringBuilder(sKey).reverse().toString();

        MultiValue mv = navigableMap.get(sKey);

        if (mv == null) {
            return false;
        }
        mv.remove(item);

        if (mv.size() == 0) {
            navigableMap.remove(sKey);
        }

        return super.delete(item);
    }


    @Override
    public List<ITEM> findEndsWith(KEY frag) {
        List<ITEM> results;

        String sKey;

        if (frag instanceof String) {
            sKey = (String) frag;
        } else {
            sKey = frag.toString();
        }

        sKey = new StringBuilder(sKey).reverse().toString();


        String start = (String) sKey;
        if (start.length() == 0 || start == null) {
            return Collections.EMPTY_LIST;
        }

        char endLetter = start.charAt(start.length() - 1);
        String sub = start.substring(0, start.length() - 1);

        StringBuilder after = new StringBuilder(start.length());

        after.append(sub);
        after.append((char) (endLetter + 1));

        NavigableMap<String, MultiValue> sortMap = (NavigableMap<String, MultiValue>) this.navigableMap;


        SortedMap<String, MultiValue> sortedSubMap = sortMap.subMap(start, after.toString());

        if (sortedSubMap.size() > 0) {
            results = new ArrayList<>();
            for (MultiValue values : sortedSubMap.values()) {
                values.addTo(results);
            }
            return results;
        }
        return Collections.EMPTY_LIST;

    }

}
