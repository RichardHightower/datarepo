package org.datarepo.predicates;

public interface Predicate<T> {

    boolean test(T input);
}
