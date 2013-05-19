package org.datarepo;

import java.util.HashMap;

/**
 * Creates a simple lookup index (like a hash map).
 * @param <KEY>    The key
 * @param <ITEM>   The item
 */
public interface LookupIndex <KEY, ITEM> extends Bag<ITEM> {
    ITEM get (KEY key);
    void invalidate();
    void setKeyGetter(KeyGetter <KEY, ITEM> keyGetter);

}
