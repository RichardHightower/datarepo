package org.datarepo.impl.decorators;

import org.datarepo.Filter;
import org.datarepo.query.Expression;

import java.util.List;

public class FilterDecoratorBase implements Filter {

    Filter delegate;

    FilterDecoratorBase(Filter delegate) {
        this.delegate = delegate;
    }

    @Override
    public List filter(Expression... expressions) {
        return delegate.filter(expressions);
    }

    @Override
    public void invalidate() {
        delegate.invalidate();
    }
}
