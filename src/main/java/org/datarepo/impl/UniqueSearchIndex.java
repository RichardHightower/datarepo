package org.datarepo.impl;

import org.datarepo.SearchIndex;

import java.util.*;
import java.util.function.Function;

/**
 * Default Search Index which uses a TreeMap
 * @param <KEY> Key we are indexing on.
 * @param <ITEM>  The items we are indexing.
 */
public class UniqueSearchIndex<KEY, ITEM> extends UniqueLookupIndex<KEY, ITEM> implements SearchIndex<KEY, ITEM> {
    private NavigableMap<KEY, ITEM> navigableMap;

    public UniqueSearchIndex() {
        super.map = new TreeMap<>();
        this.navigableMap = (NavigableMap<KEY, ITEM>) super.map;

    }

    public UniqueSearchIndex(List<ITEM> items, Function<ITEM, KEY> keyGetter) {
        super.keyGetter = keyGetter;
        super.map = new TreeMap<>();
        this.navigableMap = (NavigableMap<KEY, ITEM>) super.map;

        for (ITEM item : items) {
            add(item);
        }



    }


    @Override
    public List<ITEM> findEquals(KEY key) {
        return Collections.singletonList(navigableMap.get(key));
    }

    @Override
    public List<ITEM> findStartsWith(KEY keyFrag) {
        List<ITEM> results;

        if (keyFrag instanceof String) {
            String start = (String) keyFrag;
            if (start.length() == 0 || start == null) {
                return Collections.EMPTY_LIST;
            }

            char endLetter = start.charAt(start.length() - 1);
            String sub = start.substring(0, start.length() - 1);

            StringBuilder after = new StringBuilder(start.length());

            after.append(sub);
            after.append((char) (endLetter + 1));

            NavigableMap<String, MultiValue<ITEM>> sortMap = (NavigableMap<String, MultiValue<ITEM>>) this.navigableMap;


            SortedMap<String, MultiValue<ITEM>> sortedSubMap = sortMap.subMap(start, after.toString());

            if (sortedSubMap.size() > 0) {
                results = new ArrayList<>();
                for (MultiValue<ITEM> values : sortedSubMap.values()) {
                    if (values.value != null) {
                        results.add(values.value);
                    } else {
                        for (ITEM value : values.values) {
                            results.add(value);
                        }
                    }
                }
                return results;
            }
            return Collections.EMPTY_LIST;
        }
        return Collections.EMPTY_LIST;

    }

    @Override
    public List<ITEM> findEndsWith(KEY keyFrag) {
        List<ITEM> results = new ArrayList<>();

        if (keyFrag instanceof String) {

            Collection<ITEM> values = navigableMap.values();
            for (ITEM value : values) {
                    String svalue = (String) this.keyGetter.apply(value);
                    if (svalue.endsWith((String)keyFrag)) {
                        results.add(value);
                    }
            }
        }
        return results;
    }

    @Override
    public List<ITEM> findContains(KEY keyFrag) {
        List<ITEM> results = new ArrayList<>();

        if (keyFrag instanceof String) {

            Collection<ITEM> values = navigableMap.values();
            for (ITEM value : values) {
                String svalue = (String) this.keyGetter.apply(value);
                if (svalue.endsWith((String)keyFrag)) {
                    results.add(value);
                }
            }
        }
        return results;
    }

    @Override
    public List<ITEM> findBetween(KEY start, KEY end) {
        SortedMap<KEY, ITEM> keyMultiValueSortedMap = this.navigableMap.subMap(start, end);

        return new ArrayList<>(keyMultiValueSortedMap.values());

    }


    @Override
    public List<ITEM> findGreaterThan(KEY key) {
        SortedMap<KEY, ITEM> keyMultiValueSortedMap  = this.navigableMap.tailMap(key, false);
        return new ArrayList<>(keyMultiValueSortedMap.values());
    }

    @Override
    public List<ITEM> findLessThan(KEY key) {
        SortedMap<KEY, ITEM> keyMultiValueSortedMap  = this.navigableMap.headMap(key, false);
        return new ArrayList<>(keyMultiValueSortedMap.values());
    }

    @Override
    public List<ITEM> findGreaterThanEqual(KEY key) {
        SortedMap<KEY, ITEM> keyMultiValueSortedMap = this.navigableMap.tailMap(key);
        return new ArrayList<>(keyMultiValueSortedMap.values());
    }

    @Override
    public List<ITEM> findLessThanEqual(KEY key) {
        SortedMap<KEY, ITEM> keyMultiValueSortedMap = this.navigableMap.headMap(key);
        return new ArrayList<>(keyMultiValueSortedMap.values());
    }

}
