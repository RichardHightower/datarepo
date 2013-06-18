package org.datarepo.impl.decorators;

import org.datarepo.Filter;
import org.datarepo.ResultSet;
import org.datarepo.query.Query;

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
