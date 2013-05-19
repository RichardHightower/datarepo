package org.datarepo;

/**
 * Used to get a key from an item.
 * You can provide your own KeyGetter which allows you to get nested keys
 * or even non-properties as keys.
 * If you don't provide KeyGetter for an Index or a Repo, one gets generated.
 * The generated one uses reflection, byte code manipulation or UnSafe.
 *
 * @param <KEY> The key.
 * @param <ITEM> The item that has the key.
 */
public interface KeyGetter <KEY, ITEM> {
    KEY getKey(ITEM item);
}
