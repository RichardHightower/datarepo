package org.datarepo;

import java.util.List;

/**
 * Holds stuff
 * @param <ITEM> Type of Stuff you want to hold.
 */
public interface Bag <ITEM> {
    boolean add(ITEM item);

    boolean remove(ITEM item);

    List<ITEM> all ();

    int size();
}
