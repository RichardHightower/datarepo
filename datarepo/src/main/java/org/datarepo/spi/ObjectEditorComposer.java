package org.datarepo.spi;

import org.datarepo.SearchableCollection;

import java.util.Map;

import org.boon.fields.FieldAccess;

public interface ObjectEditorComposer<KEY, ITEM> {
    void setFields(Map<String, FieldAccess> fields);

    void setSearchableCollection(SearchableCollection<KEY, ITEM> searchableCollection);

    void init();

    void hashCodeOptimizationOn();

    public void setLookupAndExcept(boolean lookupAndExcept);

}
