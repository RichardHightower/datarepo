package org.datarepo;


import org.datarepo.criteria.Expression;
import org.datarepo.criteria.Selector;
import org.datarepo.criteria.ValueSetter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Repo, A Repo is like a DAO object or a Repository object.
 * @param <KEY>
 * @param <ITEM>
 */
public interface Repo <KEY, ITEM> extends Bag <ITEM>{

    ITEM get(KEY key);

    //Maybe these belong on the Bag interface, but I don't feel like adding them to the index classes yet
    void removeAll(ITEM... items);
    void removeAll(Collection<ITEM> items);
    void addAll(ITEM... items);
    void addAll(Collection<ITEM> items);
    void modifyAll(ITEM... items);
    void modifyAll(Collection<ITEM> items);



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

    boolean compareAndUpdate(KEY key, String property, Object compare, Object value);
    boolean compareAndUpdate(KEY key, String property, String compare, String value);
    boolean compareAndUpdate(KEY key, String property, int compare, int value);
    boolean compareAndUpdate(KEY key, String property, long compare, long value);
    boolean compareAndUpdate(KEY key, String property, char compare, char value);
    boolean compareAndUpdate(KEY key, String property, short compare, short value);
    boolean compareAndUpdate(KEY key, String property, byte compare, byte value);
    boolean compareAndUpdate(KEY key, String property, float compare, float value);
    boolean compareAndUpdate(KEY key, String property, double compare, double value);


    boolean compareAndIncrement(KEY key, String property, int compare);
    boolean compareAndIncrement(KEY key, String property, long compare);
    boolean compareAndIncrement(KEY key, String property, short compare);
    boolean compareAndIncrement(KEY key, String property, byte compare);


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
    List<Map<String, Object>> queryAsMaps(Expression... expressions);

    List<Map<String, Object>> query(List<Selector> selectors, Expression... expressions);
    List<Map<String, Object>> sortedQuery(String sortBy, List<Selector> selectors, Expression... expressions);


}
