package org.boon.impl.decorators;

import org.boon.Filter;
import org.boon.ResultSet;
import org.boon.query.Query;

/**
 * Checking
 */
public class FilterDecoratorBase implements Filter {

    Filter delegate;

    FilterDecoratorBase(Filter delegate) {
        this.delegate = delegate;
    }

    @Override
    public ResultSet filter(Query... expressions) {
        return delegate.filter(expressions);
    }

    @Override
    public void invalidate() {
        delegate.invalidate();
    }
}
