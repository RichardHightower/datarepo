package org.datarepo;

/**
 * Used by RepoBuilder to add indexes to Repo.
 * @param <ITEM>
 */
public interface RepoComposer <ITEM> {
    <KEY> void addSearchIndex(String name, SearchIndex <ITEM, KEY> si, Class <KEY> keyClass);
    <KEY> void addLookupIndex(String name, LookupIndex <ITEM, KEY> si, Class <KEY> keyClass);
    <KEY> void setPrimaryKeyGetter(KeyGetter <KEY, ITEM> getter, Class<KEY> clazz);
    void setPrimaryKeyName (String primaryKey);
}
