package org.datarepo.spi;
import java.util.Map;
import java.util.NavigableMap;

public interface MapCreator {
    NavigableMap createNavigableMap(Class<?> keyType);
    Map createMap(Class<?> keyType);

}
