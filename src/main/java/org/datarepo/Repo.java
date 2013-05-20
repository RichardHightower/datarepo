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
    void modify(String property, Object value);
    void modify(String property, String value);
    void modify(String property, int value);
    void modify(String property, long value);
    List<ITEM> filter(Expression... expressions);


}
