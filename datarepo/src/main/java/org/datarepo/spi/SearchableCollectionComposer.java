package org.datarepo.spi;

import org.datarepo.Filter;
import org.datarepo.fields.FieldAccess;
import org.datarepo.utils.Function;

import java.util.Map;

public interface SearchableCollectionComposer {
    void setPrimaryKeyName(String primaryKey);


    void setPrimaryKeyGetter(Function function);

    void init();

    void setFields(Map<String, FieldAccess> fields);

    void setFilter(Filter filter);

}
