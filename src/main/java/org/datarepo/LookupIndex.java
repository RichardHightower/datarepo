package org.datarepo;

/**
 * Creates a simple lookup index (like a hash map).
 * @param <KEY>    The key
 * @param <ITEM>   The item
 */
public interface LookupIndex <KEY, ITEM> extends Bag<ITEM> {
    ITEM get (KEY key);
    void invalidate();

}
