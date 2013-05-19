package org.datarepo;

import java.util.*;

public class SearchIndexDefault<KEY, ITEM> extends LookupIndexDefault<KEY, ITEM> implements SearchIndex<KEY, ITEM> {
    private NavigableMap<KEY, MultiValue<ITEM>> navigableMap;

    public SearchIndexDefault() {

    }

    public SearchIndexDefault(KeyGetter<KEY, ITEM> keyGetter) {
        super.keyGetter = keyGetter;
        super.map = new TreeMap<>();
        this.navigableMap = (NavigableMap<KEY, MultiValue<ITEM>>) super.map;
    }


    @Override
    public List<ITEM> findEquals(KEY key) {
        return navigableMap.get(key).getValues();
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

            Collection<MultiValue<ITEM>> values = navigableMap.values();
            for (MultiValue<ITEM> mv : values) {
                for (ITEM value : mv.getValues()) {
                    String svalue = (String) this.keyGetter.getKey(value);
                    if (svalue.endsWith((String)keyFrag)) {
                        results.add(value);
                    }
                }
            }
        }
        return results;
    }

    @Override
    public List<ITEM> findContains(KEY keyFrag) {
        List<ITEM> results = new ArrayList<>();

        if (keyFrag instanceof String) {

            Collection<MultiValue<ITEM>> values = navigableMap.values();
            for (MultiValue<ITEM> mv : values) {
                for (ITEM value : mv.getValues()) {
                    String svalue = (String) this.keyGetter.getKey(value);
                    if (svalue.endsWith((String)keyFrag)) {
                        results.add(value);
                    }
                }
            }
        }
        return results;
    }

    @Override
    public List<ITEM> findBetween(KEY start, KEY end) {
        SortedMap<KEY, MultiValue<ITEM>> keyMultiValueSortedMap = this.navigableMap.subMap(start, end);

        return getResults(keyMultiValueSortedMap);

    }

    private List<ITEM> getResults(SortedMap<KEY, MultiValue<ITEM>> keyMultiValueSortedMap) {
        List<ITEM> results = null;
        if (keyMultiValueSortedMap.size() > 0) {
            results = new ArrayList<>();
            for (MultiValue<ITEM> values : keyMultiValueSortedMap.values()) {
                values.addTo(results);
            }
            return results;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ITEM> findGreaterThan(KEY key) {
        SortedMap<KEY, MultiValue<ITEM>> keyMultiValueSortedMap = this.navigableMap.tailMap(key, false);
        return getResults(keyMultiValueSortedMap);
    }

    @Override
    public List<ITEM> findLessThan(KEY key) {
        SortedMap<KEY, MultiValue<ITEM>> keyMultiValueSortedMap = this.navigableMap.headMap(key, false);
        return getResults(keyMultiValueSortedMap);
    }

    @Override
    public List<ITEM> findGreaterThanEqual(KEY key) {
        SortedMap<KEY, MultiValue<ITEM>> keyMultiValueSortedMap = this.navigableMap.tailMap(key);
        return getResults(keyMultiValueSortedMap);
    }

    @Override
    public List<ITEM> findLessThanEqual(KEY key) {
        SortedMap<KEY, MultiValue<ITEM>> keyMultiValueSortedMap = this.navigableMap.headMap(key);
        return getResults(keyMultiValueSortedMap);
    }

}
