package org.datarepo;

import java.util.Collection;

public interface CollectionDecorator {
    SearchableCollection searchCollection();
    Collection collection();
}
