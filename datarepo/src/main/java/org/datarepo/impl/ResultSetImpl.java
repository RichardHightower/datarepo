package org.datarepo.impl;

import org.datarepo.DataRepoException;
import org.datarepo.PlanStep;
import org.datarepo.ResultSet;
import org.datarepo.fields.FieldAccess;
import org.datarepo.query.Query;
import org.datarepo.query.QueryFactory;
import org.datarepo.query.Selector;
import org.datarepo.query.Sort;
import org.datarepo.utils.Reflection;
import org.datarepo.utils.Types;

import java.lang.reflect.Array;
import java.util.*;

import static org.datarepo.utils.Reflection.toMap;
import static org.datarepo.utils.Utils.idx;

public class ResultSetImpl<T> implements ResultSet<T> {

    private List<T> results;
    private Map<String, FieldAccess> fields;


    public ResultSetImpl(Map<String, FieldAccess> fields) {
        this.fields = fields;
        this.results = new ArrayList<>();
    }


    public ResultSetImpl(List<T> results, Map<String, FieldAccess> fields) {
        this.fields = fields;
        this.results = results;
    }

    public ResultSetImpl(List<T> results) {
        if (results.size() > 0) {
            this.fields = Reflection.getPropertyFieldAccessMap(results.get(0).getClass());
        } else {
            this.fields = Collections.EMPTY_MAP;
        }
        this.results = results;
    }


    public void addResults(List<T> results) {
        results.addAll(results);
    }

    @Override
    public ResultSet expectOne() {
        if (results.size() == 0) {
            throw new DataRepoException("Expected one result, no results");
        } else if (results.size() > 1) {
            throw new DataRepoException("Expected one result, but have many");
        }
        return this;
    }

    @Override
    public ResultSet expectMany() {
        if (results.size() <= 1) {
            throw new DataRepoException("Expected many");
        }
        return this;
    }

    @Override
    public ResultSet expectNone() {
        if (results.size() != 0) {
            throw new DataRepoException("Expected none");
        }
        return this;
    }

    @Override
    public ResultSet expectOneOrMany() {
        if (results.size() >= 1) {
            throw new DataRepoException("Expected one or many");
        }
        return this;
    }

    @Override
    public ResultSet removeDuplication() {

        results = new ArrayList(new HashSet<T>(results));
        return this;
    }

    @Override
    public ResultSet sort(Sort sort) {
        sort.sort(results);
        return this;
    }

    @Override
    public Collection<T> filter(Query query) {
        return QueryFactory.filter(results, query);
    }

    @Override
    public ResultSet<List<Map<String, Object>>> select(Selector... selectors) {
        return new ResultSetImpl(Selector.performSelection(Arrays.asList(selectors), results, fields), fields);

    }

    @Override
    public int[] selectInts(Selector selector) {
        int[] values = new int[results.size()];

        List<Map<String, Object>> maps = Selector.performSelection(Collections.singletonList(selector), results, fields);

        String keyName = selector.getName();
        for (int index = 0; index < values.length; index++) {
            Map<String, Object> map = maps.get(index);
            values[index] = Types.toInt(map.get(keyName));
        }
        return values;
    }

    @Override
    public float[] selectFloats(Selector selector) {
        float[] values = new float[results.size()];

        List<Map<String, Object>> maps = Selector.performSelection(Collections.singletonList(selector), results, fields);

        String keyName = selector.getName();
        for (int index = 0; index < values.length; index++) {
            Map<String, Object> map = maps.get(index);
            values[index] = Types.toFloat(map.get(keyName));
        }
        return values;
    }

    @Override
    public short[] selectShorts(Selector selector) {
        short[] values = new short[results.size()];

        List<Map<String, Object>> maps = Selector.performSelection(Collections.singletonList(selector), results, fields);

        String keyName = selector.getName();
        for (int index = 0; index < values.length; index++) {
            Map<String, Object> map = maps.get(index);
            values[index] = Types.toShort(map.get(keyName));
        }
        return values;
    }

    @Override
    public double[] selectDoubles(Selector selector) {
        double[] values = new double[results.size()];

        List<Map<String, Object>> maps = Selector.performSelection(Collections.singletonList(selector), results, fields);

        String keyName = selector.getName();
        for (int index = 0; index < values.length; index++) {
            Map<String, Object> map = maps.get(index);
            values[index] = Types.toDouble(map.get(keyName));
        }
        return values;
    }

    @Override
    public byte[] selectBytes(Selector selector) {
        byte[] values = new byte[results.size()];

        List<Map<String, Object>> maps = Selector.performSelection(Collections.singletonList(selector), results, fields);

        String keyName = selector.getName();
        for (int index = 0; index < values.length; index++) {
            Map<String, Object> map = maps.get(index);
            values[index] = Types.toByte(map.get(keyName));
        }
        return values;
    }

    @Override
    public char[] selectChars(Selector selector) {
        char[] values = new char[results.size()];

        List<Map<String, Object>> maps = Selector.performSelection(Collections.singletonList(selector), results, fields);

        String keyName = selector.getName();
        for (int index = 0; index < values.length; index++) {
            Map<String, Object> map = maps.get(index);
            values[index] = Types.toChar(map.get(keyName));
        }
        return values;
    }

    @Override
    public Object[] selectObjects(Selector selector) {
        Object[] values = new Object[results.size()];

        List<Map<String, Object>> maps = Selector.performSelection(Collections.singletonList(selector), results, fields);

        String keyName = selector.getName();
        for (int index = 0; index < values.length; index++) {
            Map<String, Object> map = maps.get(index);
            values[index] = map.get(keyName);
        }
        return values;
    }

    @Override
    public <OBJ> OBJ[] selectObjects(Class<OBJ> cls, Selector selector) {
        Object values = Array.newInstance(cls, results.size());

        List<Map<String, Object>> maps = Selector.performSelection(Collections.singletonList(selector), results, fields);

        String keyName = selector.getName();
        for (int index = 0; index < results.size(); index++) {
            Map<String, Object> map = maps.get(index);
            idx(values, index, map.get(keyName));
        }
        return (OBJ[]) values;
    }


    @Override
    public <OBJ> ResultSet<OBJ> selectObjectsAsResultSet(Class<OBJ> cls, Selector selector) {
        Object values = Array.newInstance(cls, results.size());

        List<Map<String, Object>> maps = Selector.performSelection(Collections.singletonList(selector), results, fields);

        String keyName = selector.getName();
        for (int index = 0; index < results.size(); index++) {
            Map<String, Object> map = maps.get(index);
            idx(values, index, map.get(keyName));
        }
        OBJ[] array = (OBJ[]) values;
        List list = new ArrayList(Arrays.asList(array));
        return new ResultSetImpl(list);
    }

    @Override
    public Collection<T> asCollection() {
        return results;
    }

    @Override
    public String asJSONString() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public List<Map<String, Object>> asListOfMaps() {

        List<Map<String, Object>> items = new ArrayList<>(results.size());
        for (T item : results) {
            items.add(toMap(item));
        }
        return items;

    }

    @Override
    public List<T> asList() {
        return results;
    }

    @Override
    public Set<T> asSet() {
        return new HashSet(results);
    }

    @Override
    public List<PlanStep> queryPlan() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public T firstItem() {
        return results.get(0);
    }

    @Override
    public Map<String, Object> firstMap() {
        return toMap(this.firstItem());
    }

    @Override
    public String firstJSON() {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public int firstInt(Selector selector) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public float firstFloat(Selector selector) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public short firstShort(Selector selector) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public double firstDouble(Selector selector) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public byte firstByte(Selector selector) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public char firstChar(Selector selector) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public Object firstObject(Selector selector) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public <OBJ> OBJ firstObject(Class<OBJ> cls, Selector selector) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public List<T> paginate(int start, int size) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public List<Map<String, Object>> paginateMaps(int start, int size) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public String paginateJSON(int start, int size) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public int size() {
        return this.results.size();
    }

    @Override
    public Iterator<T> iterator() {
        return this.results.iterator();
    }
}
