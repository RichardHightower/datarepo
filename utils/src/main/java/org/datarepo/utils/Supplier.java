package org.datarepo.utils;

public interface Supplier<T> {

    /**
     * Returns an object.
     *
     * @return an object
     */
    T get();
}
