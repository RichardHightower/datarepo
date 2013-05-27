package org.datarepo.spi;

import org.datarepo.SearchableCollection;
import org.datarepo.reflection.FieldAccess;

import java.util.Map;

public interface ObjectEditorComposer <KEY, ITEM> {
    void setFields(Map<String, FieldAccess> fields);

    void setSearchableCollection(SearchableCollection<KEY, ITEM> searchableCollection);

    void init();

}
