package org.datarepo;


import org.datarepo.query.Expression;
import org.datarepo.query.Selector;
import org.datarepo.query.ValueSetter;
import org.datarepo.query.Visitor;

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
    /* Does basic conversion */
    void modifyByValue(ITEM item, String property, String value);

    void modify(ITEM item, String property, Object value);
    void modify(ITEM item, String property, int value);
    void modify(ITEM item, String property, long value);
    void modify(ITEM item, String property, char value);
    void modify(ITEM item, String property, short value);
    void modify(ITEM item, String property, byte value);
    void modify(ITEM item, String property, float value);
    void modify(ITEM item, String property, double value);
    void modify(ITEM item, ValueSetter... values);

    void updateByValue(KEY key, String property, String value);
    void update(KEY key, String property, Object value);
    void update(KEY key, String property, int value);
    void update(KEY key, String property, long value);
    void update(KEY key, String property, char value);
    void update(KEY key, String property, short value);
    void update(KEY key, String property, byte value);
    void update(KEY key, String property, float value);
    void update(KEY key, String property, double value);
    void update(KEY key, ValueSetter... values);

    Object  readObject  (  KEY key, String property);
    <T> T   readValue   (  KEY key, String property, Class<T> type);
    int     readInt     (  KEY key, String property);
    long    readLong    (  KEY key, String property);
    char    readChar    (  KEY key, String property);
    short   readShort   (  KEY key, String property);
    byte    readByte    (  KEY key, String property);
    float   readFloat   (  KEY key, String property);
    double  readDouble  (  KEY key, String property);

    int count   (  KEY key, String property, int value);
    int count   (  KEY key, String property, short value);
    int count   (  KEY key, String property, byte value);
    int count   (  KEY key, String property, long value);
    int count   (  KEY key, String property, char value);
    int count   (  KEY key, String property, float value);
    int count   (  KEY key, String property, double value);
    int count   (  KEY key, String property, Object value);

    <T> T   max   (  KEY key, String property, Class<T> type);
    String  maxString     (  KEY key, String property);
    Number  maxNumber     (  KEY key, String property);
    int     maxInt     (  KEY key, String property);
    long    maxLong    (  KEY key, String property);
    double    maxDouble    (  KEY key, String property);

    <T> T   min   (  KEY key, String property, Class<T> type);
    String  minString     (  KEY key, String property);
    Number  minNumber     (  KEY key, String property);
    int     minInt     (  KEY key, String property);
    long    minLong    (  KEY key, String property);
    double    minDouble    (  KEY key, String property);


    boolean compareAndUpdate(KEY key, String property, Object compare, Object value);
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
    void updateByFilterUsingValue(String property, String value, Expression... expressions);
    void updateByFilter(String property, int value, Expression... expressions);
    void updateByFilter(String property, long value, Expression... expressions);
    void updateByFilter(String property, char value, Expression... expressions);
    void updateByFilter(String property, short value, Expression... expressions);
    void updateByFilter(String property, byte value, Expression... expressions);
    void updateByFilter(String property, float value, Expression... expressions);
    void updateByFilter(String property, double value, Expression... expressions);
    void updateByFilter(List<ValueSetter> values, Expression... expressions);


    List<ITEM> query(Expression... expressions);
    List<ITEM> sortedQuery(String sortBy, Expression... expressions);

    List<Map<String, Object>> queryAsMaps(Expression... expressions);

    List<Map<String, Object>> query(List<Selector> selectors, Expression... expressions);
    List<Map<String, Object>> sortedQuery(String sortBy, List<Selector> selectors, Expression... expressions);

    void query(Visitor<KEY, ITEM> visitor, Expression... expressions);
    void sortedQuery(Visitor<KEY, ITEM> visitor, String sortBy, Expression... expressions);


}
