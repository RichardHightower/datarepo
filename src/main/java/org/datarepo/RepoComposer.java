package org.datarepo;

import org.datarepo.reflection.FieldAccess;

import java.util.Map;

/**
 * Used by RepoBuilder to add indexes to Repo.
 */
public interface RepoComposer {
    void addSearchIndex(String name, SearchIndex <?, ?> si);
    void addLookupIndex(String name, LookupIndex <?, ?> si);
    void setPrimaryKeyGetter(KeyGetter <?, ?> getter);

    void setFields(Map<String, FieldAccess> fields);
    void setPrimaryKeyName (String primaryKey);

    void setFilter(Filter filter);
}
