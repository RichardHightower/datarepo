package org.datarepo;

import org.datarepo.query.Expression;
import org.datarepo.query.Selector;
import org.datarepo.query.ValueSetter;
import org.datarepo.query.Visitor;
import org.datarepo.reflection.FieldAccess;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface SearchableCollection<KEY, ITEM> extends Collection<ITEM> {

    ITEM get(KEY key);

    KEY getKey(ITEM item);

    void invalidateIndex(String property, ITEM item);

    void validateIndex(String property, ITEM item);



    Object readObject(KEY key, String property);

    <T> T readValue(KEY key, String property, Class<T> type);

    int readInt(KEY key, String property);

    long readLong(KEY key, String property);

    char readChar(KEY key, String property);

    short readShort(KEY key, String property);

    byte readByte(KEY key, String property);

    float readFloat(KEY key, String property);

    double readDouble(KEY key, String property);

    int count(KEY key, String property, int value);

    int count(KEY key, String property, short value);

    int count(KEY key, String property, byte value);

    int count(KEY key, String property, long value);

    int count(KEY key, String property, char value);

    int count(KEY key, String property, float value);

    int count(KEY key, String property, double value);

    int count(KEY key, String property, Object value);

    <T> T max(KEY key, String property, Class<T> type);

    String maxString(KEY key, String property);

    Number maxNumber(KEY key, String property);

    int maxInt(KEY key, String property);

    long maxLong(KEY key, String property);

    double maxDouble(KEY key, String property);

    <T> T min(KEY key, String property, Class<T> type);

    String minString(KEY key, String property);

    Number minNumber(KEY key, String property);

    int minInt(KEY key, String property);

    long minLong(KEY key, String property);

    double minDouble(KEY key, String property);


    List<ITEM> query(Expression... expressions);

    List<ITEM> sortedQuery(String sortBy, Expression... expressions);

    List<Map<String, Object>> queryAsMaps(Expression... expressions);

    List<Map<String, Object>> query(List<Selector> selectors, Expression... expressions);

    List<Map<String, Object>> sortedQuery(String sortBy, List<Selector> selectors, Expression... expressions);

    void query(Visitor<KEY, ITEM> visitor, Expression... expressions);

    void sortedQuery(Visitor<KEY, ITEM> visitor, String sortBy, Expression... expressions);


    boolean delete(ITEM item);



    void addSearchIndex(String name, SearchIndex<?, ?> si);

    void addLookupIndex(String name, LookupIndex<?, ?> si);



    List<ITEM> all();

    void removeByKey(KEY key);

}