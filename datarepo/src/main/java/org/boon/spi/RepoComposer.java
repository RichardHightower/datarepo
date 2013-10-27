package org.boon.spi;

import org.boon.ObjectEditor;
import org.boon.SearchableCollection;

/**
 * Used by RepoBuilder to add indexes to Repo.
 */
public interface RepoComposer<KEY, ITEM> {


    void setSearchableCollection(SearchableCollection<KEY, ITEM> searchableCollection);

    void init();

    void setObjectEditor(ObjectEditor editor);
}
