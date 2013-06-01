package org.datarepo.spi;


import org.datarepo.LookupIndex;
import org.datarepo.SearchIndex;
import org.datarepo.SearchableCollection;
import org.datarepo.reflection.FieldAccess;

import java.util.Map;

public interface FilterComposer {

    public void setSearchableCollection(SearchableCollection searchableCollection);

    public void setFields(Map<String, FieldAccess> fields);

    public void setUseCache(boolean cache);

    public void setSearchIndexMap(Map<String, SearchIndex> searchIndexMap);

    public void setLookupIndexMap(Map<String, LookupIndex> lookupIndexMap);
}
