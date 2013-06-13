package org.datarepo;

import org.datarepo.utils.Function;

import java.util.List;

/**
 * Creates a simple lookup index (like a hash map).
 *
 * @param <KEY>  The key
 * @param <ITEM> The item
 */
public interface LookupIndex<KEY, ITEM> extends Bag<ITEM> {
    ITEM get(KEY key);

    void setKeyGetter(Function<ITEM, KEY> keyGetter);

    List<ITEM> getAll(KEY key);

    boolean deleteByKey(KEY key);

    boolean isPrimaryKeyOnly();

    void setInputKeyTransformer(Function<Object, KEY> func);

    void setBucketSize(int size);

    void init();


}
