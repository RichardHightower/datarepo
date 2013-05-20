package org.datarepo;


import org.datarepo.criteria.Expression;
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

    List<ITEM> query(Expression... expressions);


}
