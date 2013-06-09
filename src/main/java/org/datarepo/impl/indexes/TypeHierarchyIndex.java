package org.datarepo.impl.indexes;

import org.datarepo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TypeHierarchyIndex extends BaseIndexWrapper {

    @Override
    public boolean add(Object o) {
        List keys = getKeys(o);
        index.addManyKeys(o, keys);
        return true;
    }

    @Override
    protected List getKeys(Object o) {
        List<Class> list = new ArrayList();
        Class cls = o.getClass();

        while (cls != null && cls != Utils.object) {
            list.add(cls);
            cls = cls.getSuperclass();
        }
        return list;
    }

    @Override
    public boolean delete(Object o) {
        List keys = getKeys(o);
        index.removeManyKeys(o, keys);
        return true;

    }
}
