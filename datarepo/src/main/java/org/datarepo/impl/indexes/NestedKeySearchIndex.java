package org.datarepo.impl.indexes;

import org.boon.utils.Reflection;

import java.util.List;

import static org.boon.utils.Conversions.toList;


/**
 * This allows nested key look-ups.
 */
public class NestedKeySearchIndex extends BaseIndexWrapper {

    public NestedKeySearchIndex(String... path) {
        super(path);

    }

    @Override
    public boolean add(Object o) {

        List keys = getKeys(o);
        index.addManyKeys(o, keys);
        return true;
    }

    @Override
    protected List getKeys(Object o) {
        Object list = Reflection.getPropByPath(o, this.path);
        return toList(list);
    }

    @Override
    public boolean delete(Object o) {
        List keys = getKeys(o);
        index.removeManyKeys(o, keys);

        return true;
    }

}
