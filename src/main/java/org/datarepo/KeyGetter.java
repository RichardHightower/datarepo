package org.datarepo;

public interface KeyGetter <KEY, ITEM> {
    KEY getKey(ITEM item);
}
