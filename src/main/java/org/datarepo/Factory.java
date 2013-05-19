package org.datarepo;

/**
 * Creates something, used by builder so you can change default
 * implementations of search index, builders, property getters, etc.
 * @param <T>   The thing this factory creates.
 */
public interface Factory <T> {
     T create();
}
