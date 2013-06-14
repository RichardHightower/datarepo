package org.datarepo;


import org.datarepo.query.Query;
import org.datarepo.query.Update;

import java.util.List;

/**
 * Repo, A Repo is like a DAO object or a Repository object.
 *
 * @param <KEY>
 * @param <ITEM> //SearchableCollection<KEY, ITEM>,
 */
public interface Repo<KEY, ITEM> extends ObjectEditor<KEY, ITEM>, SearchableCollection<KEY, ITEM> {


    void updateByFilter(String property, Object value, Query... expressions);

    void updateByFilterUsingValue(String property, String value, Query... expressions);

    void updateByFilter(String property, int value, Query... expressions);

    void updateByFilter(String property, long value, Query... expressions);

    void updateByFilter(String property, char value, Query... expressions);

    void updateByFilter(String property, short value, Query... expressions);

    void updateByFilter(String property, byte value, Query... expressions);

    void updateByFilter(String property, float value, Query... expressions);

    void updateByFilter(String property, double value, Query... expressions);

    void updateByFilter(List<Update> values, Query... expressions);


}
