package org.datarepo.spi;

import org.datarepo.Filter;
import org.datarepo.fields.FieldAccess;

import java.util.Map;
import java.util.function.Function;

public interface SearchableCollectionComposer {
    void setPrimaryKeyName(String primaryKey);


    void setPrimaryKeyGetter(Function function);

    void init();

    void setFields(Map<String, FieldAccess> fields);

    void setFilter(Filter filter);
}
