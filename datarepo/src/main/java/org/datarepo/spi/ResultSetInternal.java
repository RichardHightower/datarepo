package org.datarepo.spi;

import org.datarepo.ResultSet;
import org.datarepo.query.Query;

import java.util.List;

public interface ResultSetInternal<T> extends ResultSet<T> {

    void addResults(List<T> results);

    void filterAndPrune(Query query);

    void andResults();

    int lastSize();
}
