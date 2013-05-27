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
 *     //SearchableCollection<KEY, ITEM>,
 */
public interface Repo<KEY, ITEM> extends ObjectEditor<KEY, ITEM>, SearchableCollection<KEY, ITEM> {



    void updateByFilter(String property, Object value, Expression... expressions);

    void updateByFilterUsingValue(String property, String value, Expression... expressions);

    void updateByFilter(String property, int value, Expression... expressions);

    void updateByFilter(String property, long value, Expression... expressions);

    void updateByFilter(String property, char value, Expression... expressions);

    void updateByFilter(String property, short value, Expression... expressions);

    void updateByFilter(String property, byte value, Expression... expressions);

    void updateByFilter(String property, float value, Expression... expressions);

    void updateByFilter(String property, double value, Expression... expressions);

    void updateByFilter(List<ValueSetter> values, Expression... expressions);


}
