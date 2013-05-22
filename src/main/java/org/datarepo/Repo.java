package org.datarepo;


import org.datarepo.criteria.Expression;
import org.datarepo.criteria.ValueSetter;

import java.util.List;

/**
 * Repo, A Repo is like a DAO object or a Repository object.
 * @param <KEY>
 * @param <ITEM>
 */
public interface Repo <KEY, ITEM> extends Bag <ITEM>{

    ITEM get(KEY key);

    void modify(ITEM item);
    void modify(ITEM item, String property, Object value);
    void modify(ITEM item, String property, String value);
    void modify(ITEM item, String property, int value);
    void modify(ITEM item, String property, long value);
    void modify(ITEM item, String property, char value);
    void modify(ITEM item, String property, short value);
    void modify(ITEM item, String property, byte value);
    void modify(ITEM item, String property, float value);
    void modify(ITEM item, String property, double value);
    void modify(ITEM item, ValueSetter... values);

    void update(KEY key, String property, Object value);
    void update(KEY key, String property, String value);
    void update(KEY key, String property, int value);
    void update(KEY key, String property, long value);
    void update(KEY key, String property, char value);
    void update(KEY key, String property, short value);
    void update(KEY key, String property, byte value);
    void update(KEY key, String property, float value);
    void update(KEY key, String property, double value);
    void update(KEY key, ValueSetter... values);

    void updateByFilter(String property, Object value, Expression... expressions);
    void updateByFilter(String property, String value, Expression... expressions);
    void updateByFilter(String property, int value, Expression... expressions);
    void updateByFilter(String property, long value, Expression... expressions);
    void updateByFilter(String property, char value, Expression... expressions);
    void updateByFilter(String property, short value, Expression... expressions);
    void updateByFilter(String property, byte value, Expression... expressions);
    void updateByFilter(String property, float value, Expression... expressions);
    void updateByFilter(String property, double value, Expression... expressions);
    void updateByFilter(List<ValueSetter> values, Expression... expressions);

    List<ITEM> query(Expression... expressions);


}
