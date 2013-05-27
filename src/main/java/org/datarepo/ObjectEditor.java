package org.datarepo;

import org.datarepo.query.ValueSetter;
import org.datarepo.reflection.FieldAccess;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ObjectEditor<KEY, ITEM> extends Bag<ITEM> {



    ITEM get(KEY key);

    KEY getKey(ITEM item);


    void put(ITEM item);

    void removeByKey(KEY key);

    void removeAll(ITEM... items);

    void removeAllAsync(Collection<ITEM> items);

    void addAll(ITEM... items);

    void addAllAsync(Collection<ITEM> items);

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


    void addAll(List<ITEM> items);


}
