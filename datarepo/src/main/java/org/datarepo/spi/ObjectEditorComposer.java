package org.datarepo.spi;

import org.datarepo.SearchableCollection;
import org.datarepo.fields.FieldAccess;

import java.util.Map;

public interface ObjectEditorComposer<KEY, ITEM> {
    void setFields(Map<String, FieldAccess> fields);

    void setSearchableCollection(SearchableCollection<KEY, ITEM> searchableCollection);

    void init();

    void hashCodeOptimizationOn();

    public void setLookupAndExcept(boolean lookupAndExcept);

}
