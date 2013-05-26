package org.datarepo;

import java.util.Collection;
import java.util.List;

/**
 * Holds stuff
 *
 * @param <ITEM> Type of Stuff you want to hold.
 */
public interface Bag<ITEM> {
    boolean add(ITEM item);

    boolean delete(ITEM item);

    List<ITEM> all();

    int size();

    Collection<ITEM> toCollection();

    void clear();


}
