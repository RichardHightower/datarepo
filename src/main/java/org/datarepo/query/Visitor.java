package org.datarepo.query;

import org.datarepo.Repo;
import java.util.List;

public interface  Visitor <KEY, ITEM> {

    public abstract void visit(KEY key, ITEM item, Object currentProperty, Repo<KEY, ITEM> repo, List<String> propertyPath);


}