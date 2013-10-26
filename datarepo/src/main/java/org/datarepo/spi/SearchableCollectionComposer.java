package org.datarepo.spi;

import org.datarepo.Filter;
import org.datarepo.predicates.Function;

import java.util.Map;

import org.boon.fields.FieldAccess;

public interface SearchableCollectionComposer {
    void setPrimaryKeyName(String primaryKey);


    void setPrimaryKeyGetter(Function function);

    void init();

    void setFields(Map<String, FieldAccess> fields);

    void setFilter(Filter filter);

    void setRemoveDuplication(boolean b);
}
