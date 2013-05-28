package org.datarepo;

import java.util.List;

/**
 * SearchIndex
 *
 * @param <KEY>  Key we are indexing on.
 * @param <ITEM> The item that we are indexing.
 */
public interface SearchIndex<KEY, ITEM> extends LookupIndex<KEY, ITEM> {
    List<ITEM> findEquals(KEY key);

    List<ITEM> findStartsWith(KEY keyFrag);

    List<ITEM> findEndsWith(KEY keyFrag);

    List<ITEM> findContains(KEY keyFrag);

    List<ITEM> findBetween(KEY start, KEY end);

    List<ITEM> findGreaterThan(KEY key);

    List<ITEM> findLessThan(KEY key);

    List<ITEM> findGreaterThanEqual(KEY key);

    List<ITEM> findLessThanEqual(KEY key);

    ITEM min();

    ITEM max();

    int count(KEY key);


//      List <ITEM> findEquals (KEY key, int start, int length);
//      List <ITEM> findStartsWith(KEY keyFrag, int start, int length);
//      List <ITEM> findEndsWith(KEY keyFrag, int start, int length);
//      List <ITEM> findContains(KEY keyFrag, int start, int length);
//      List <ITEM> findBetween(KEY startKey, KEY endKey, int start, int length);
//      List <ITEM> findGreaterThan(KEY key, int start, int length);
//      List <ITEM> findLessThan(KEY key, int start, int length);
//      List <ITEM> findGreaterThanEqual(KEY key, int start, int length);
//      List <ITEM> findLessThanEqual(KEY key, int start, int length);

}
