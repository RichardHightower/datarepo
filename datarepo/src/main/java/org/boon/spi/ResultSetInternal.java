package org.boon.spi;

import org.boon.ResultSet;
import org.boon.query.Query;

import java.util.List;

public interface ResultSetInternal<T> extends ResultSet<T> {

    void addResults(List<T> results);

    void filterAndPrune(Query query);

    void andResults();

    int lastSize();
}
