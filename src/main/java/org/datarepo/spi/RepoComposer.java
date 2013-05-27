package org.datarepo.spi;

import org.datarepo.ObjectEditor;
import org.datarepo.SearchableCollection;
import org.datarepo.reflection.FieldAccess;

import java.util.Map;
import java.util.function.Function;

/**
 * Used by RepoBuilder to add indexes to Repo.
 */
public interface RepoComposer<KEY, ITEM> {



    void setSearchableCollection(SearchableCollection<KEY, ITEM> searchableCollection);

    void init();

    void setObjectEditor(ObjectEditor editor);
}
