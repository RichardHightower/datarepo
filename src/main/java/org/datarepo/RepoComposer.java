package org.datarepo;

/**
 * Used by RepoBuilder to add indexes to Repo.
 * @param <ITEM>
 */
public interface RepoComposer <ITEM> {
    <KEY> void addSearchIndex(String name, SearchIndex <KEY, ITEM> si);
    <KEY> void addLookupIndex(String name, LookupIndex <KEY, ITEM> si);
    <KEY> void setPrimaryKeyGetter(KeyGetter <KEY, ITEM> getter);
    void setPrimaryKeyName (String primaryKey);
}
