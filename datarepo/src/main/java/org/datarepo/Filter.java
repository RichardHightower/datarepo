package org.datarepo;

import org.datarepo.query.Expression;

import java.util.List;

public interface Filter {

    List filter(Expression... expressions);

    void invalidate();

}
