package org.datarepo;

import org.datarepo.query.Expression;
import org.datarepo.reflection.FieldAccess;

import java.util.List;
import java.util.Map;

public interface Filter {

    List filter(SearchableCollection searchableCollection, Map<String, FieldAccess> fields, Map<String, LookupIndex> lookupIndexMap,
                Map<String, SearchIndex> searchIndexMap,
                Expression... expressions);

    void invalidate();

}
