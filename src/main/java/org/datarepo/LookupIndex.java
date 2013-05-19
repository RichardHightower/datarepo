package org.datarepo;

public interface LookupIndex <KEY, ITEM> extends Bag<ITEM> {
    ITEM get (KEY key);
    void invalidate();

}
