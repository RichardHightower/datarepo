package org.datarepo;

import java.util.List;

/**
 * Holds stuff
 * @param <ITEM> Type of Stuff you want to hold.
 */
public interface Bag <ITEM> {
    void add(ITEM item);
    void remove(ITEM item);
    List<ITEM> all ();


}
