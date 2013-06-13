package org.datarepo.impl.maps;

import org.datarepo.spi.MapCreator;
import org.datarepo.utils.Utils;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;

public class MapCreatorImpl implements MapCreator {
    @Override
    public NavigableMap createNavigableMap(Class<?> keyType) {
        if (keyType == Utils.string) {
            return new JavaUtilNavigableMap();
        } else {
            return new JavaUtilNavigableMap();
        }
    }

    @Override
    public NavigableMap createNavigableMap(Class<?> keyType, Comparator collator) {

        if (keyType == Utils.string) {
            return new JavaUtilNavigableMap(collator);
        } else {
            return new JavaUtilNavigableMap();
        }
    }

    @Override
    public Map createMap(Class<?> keyType) {
        return new JavaUtilMap();
    }
}
