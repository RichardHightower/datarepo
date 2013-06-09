package org.datarepo.query;

import org.datarepo.fields.FieldAccess;
import org.datarepo.utils.Reflection;
import org.datarepo.utils.Utils;

import java.util.*;

public class Sort {

    private String name = "this";
    private SortType type;
    private List<Sort> sorts = new ArrayList<>();
    private String toString;
    private int hashCode;


    private List<Comparator> comparators;
    private Comparator comparator;

    public Sort() {
    }

    public Sort(String name, SortType type) {
        this.name = name;
        this.type = type;
        this.hashCode = doHashCode();
        this.toString = doToString();
    }

    public SortType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private String doToString() {
        return "Sort{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';

    }

    public Sort then(String name) {
        this.sorts.add(new Sort(name, SortType.ASCENDING));
        return this;
    }

    public Sort thenAsc(String name) {
        this.sorts.add(new Sort(name, SortType.ASCENDING));
        return this;
    }

    public Sort thenDesc(String name) {
        this.sorts.add(new Sort(name, SortType.DESCENDING));
        return this;
    }

    @Override
    public String toString() {
        return toString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sort)) return false;

        Sort sort = (Sort) o;

        if (!name.equals(sort.name)) return false;
        if (type != sort.type) return false;

        return true;
    }

    private int doHashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;

    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public void sort(List list, Map<String, FieldAccess> fields) {
        Collections.sort(list, this.comparator(fields));
    }


    public void sort(List list) {
        if (list == null || list.size() == 0) {
            return;
        }
        Map<String, FieldAccess> fields = Reflection.getPropertyFieldAccessMap(list.iterator().next().getClass());
        Collections.sort(list, this.comparator(fields));
    }

    public Comparator comparator(Map<String, FieldAccess> fields) {
        if (comparator == null) {
            comparator = Utils.universalComparator(this.getName(), fields,
                    this.getType() == SortType.ASCENDING, this.childComparators(fields));
        }
        return comparator;
    }

    private List<Comparator> childComparators(Map<String, FieldAccess> fields) {
        if (this.comparators == null) {
            this.comparators = new ArrayList<Comparator>(this.sorts.size() + 1);

            for (Sort sort : sorts) {
                Comparator comparator = Utils.universalComparator(
                        sort.getName(),
                        fields,
                        sort.type == SortType.ASCENDING,
                        sort.childComparators(fields)
                );
                this.comparators.add(comparator);
            }
        }
        return this.comparators;
    }

}
