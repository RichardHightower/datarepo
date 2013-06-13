package org.datarepo.impl.decorators;

import org.datarepo.Filter;
import org.datarepo.query.Criteria;
import org.datarepo.query.Expression;
import org.datarepo.query.Group;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FilterWithSimpleCache extends FilterDecoratorBase {

    Map<Expression, List> queryCache = new ConcurrentHashMap<>();
    AtomicInteger flushCount = new AtomicInteger();


    @Override
    public List filter(Expression... expressions) {
        Group and = Criteria.and(expressions);
        checkCache();

        List results = queryCache.get(and);

        if (results != null) {
            return results;
        }


        results = super.filter(expressions);

        queryCache.put(and, results == null ? Collections.EMPTY_LIST : results);
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
