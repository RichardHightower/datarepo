package org.datarepo;

import org.datarepo.criteria.Expression;

import java.util.List;
import java.util.Map;

public interface Filter {

    List filter(Map<String, LookupIndex> lookupIndexMap,
                       Map<String, SearchIndex> searchIndexMap,
                       Expression... expressions) ;

}
