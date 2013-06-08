package org.datarepo.query;

import java.util.List;

public class Sort {

    private String name;
    private SortType type;
    private List<Sort> sorts;
    private String toString;
    private int hashCode;

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
}
