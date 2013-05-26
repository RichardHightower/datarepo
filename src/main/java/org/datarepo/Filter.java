package org.datarepo;

import org.datarepo.query.Expression;

import java.util.List;
import java.util.Map;

public interface Filter {

    List filter(Map<String, LookupIndex> lookupIndexMap,
                Map<String, SearchIndex> searchIndexMap,
                Expression... expressions);

}
