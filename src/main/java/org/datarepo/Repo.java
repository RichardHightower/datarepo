package org.datarepo;


import org.datarepo.query.Expression;
import org.datarepo.query.Selector;
import org.datarepo.query.ValueSetter;
import org.datarepo.query.Visitor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * Repo, A Repo is like a DAO object or a Repository object.
 *
 * @param <KEY>
 * @param <ITEM>
 */
public interface Repo<KEY, ITEM> extends ObjectEditor<KEY, ITEM>, SearchableCollection<KEY, ITEM>, Collection<ITEM> {

    ITEM get(KEY key);


}
