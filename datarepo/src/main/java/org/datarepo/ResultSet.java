package org.datarepo;

import org.datarepo.query.Query;
import org.datarepo.query.Selector;
import org.datarepo.query.Sort;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResultSet<T> extends Iterable<T> {

    ResultSet expectOne();

    ResultSet expectMany();

    ResultSet expectNone();

    ResultSet expectOneOrMany();

    ResultSet removeDuplication();

    ResultSet sort(Sort sort);

    Collection<T> filter(Query query);

    ResultSet<List<Map<String, Object>>> select(Selector... selectors);

    Collection<T> asCollection();

    String asJSONString();

    List<Map<String, Object>> asListOfMaps();

    List<T> asList();

    Set<T> asSet();

    List<PlanStep> queryPlan();

    T firstItem();

    List<Map<String, Object>> firstMap();

    String firstJSON();

    List<T> paginate(int start, int size);

    List<Map<String, Object>> paginateMaps(int start, int size);

    String paginateJSON(int start, int size);


}
