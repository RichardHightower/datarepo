package org.datarepo;

import org.datarepo.query.Query;

import java.util.List;

public interface Filter {

    List filter(Query... expressions);

    void invalidate();

}
