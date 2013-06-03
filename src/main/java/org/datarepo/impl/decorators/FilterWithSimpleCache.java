package org.datarepo.impl.decorators;

import org.datarepo.Filter;
import org.datarepo.query.Expression;

import java.util.List;

public class FilterWithSimpleCache extends FilterDecoratorBase {

    @Override
    public List filter(Expression... expressions) {
        return super.filter(expressions);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    FilterWithSimpleCache(Filter delegate) {
        super(delegate);
    }
}
