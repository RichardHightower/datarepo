package org.datarepo.impl;

import org.datarepo.spi.MapCreator;

import java.util.Map;
import java.util.NavigableMap;

public class MapCreatorImpl implements MapCreator {
    @Override
    public NavigableMap createNavigableMap(Class<?> keyType) {
        return new JavaUtilNavigableMap();
    }

    @Override
    public Map createMap(Class<?> keyType) {
        return new JavaUtilMap();
    }
}
