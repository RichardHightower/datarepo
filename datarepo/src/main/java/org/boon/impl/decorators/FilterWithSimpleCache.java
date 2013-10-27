package org.boon.impl.decorators;

import org.boon.Filter;
import org.boon.ResultSet;
import org.boon.query.Group;
import org.boon.query.Query;
import org.boon.query.QueryFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FilterWithSimpleCache extends FilterDecoratorBase {

    Map<Query, ResultSet> queryCache = new ConcurrentHashMap<>();
    AtomicInteger flushCount = new AtomicInteger();


    @Override
    public ResultSet filter(Query... expressions) {
        Group and = QueryFactory.and(expressions);
        checkCache();

        ResultSet results = queryCache.get(and);

        if (results != null) {
            return results;
        }


        results = super.filter(expressions);

        queryCache.put(and, results);
        flushCount.incrementAndGet();

        return results;
    }

    @Override
    public void invalidate() {
        queryCache.clear();
        super.invalidate();
    }

    public FilterWithSimpleCache(Filter delegate) {
        super(delegate);
    }

    private void checkCache() {
        if (flushCount.get() > 10_000 && queryCache.size() > 10_000) {
            queryCache.clear();
            flushCount.set(0);
        }
    }

}
