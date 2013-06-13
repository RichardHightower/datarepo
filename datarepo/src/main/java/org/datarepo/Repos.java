package org.datarepo;

import org.datarepo.spi.RepoComposer;
import org.datarepo.spi.SPIFactory;
import org.datarepo.spi.SearchIndex;
import org.datarepo.utils.Function;
import org.datarepo.utils.Supplier;

public class Repos {

    public static void setRepoBuilder(Supplier<RepoBuilder> factory) {
        SPIFactory.setRepoBuilderFactory(factory);
    }

    public static void setDefaultSearchIndexFactory(Function<Class, SearchIndex> factory) {
        SPIFactory.setSearchIndexFactory(factory);
    }

    public static void setLookupIndexFactory(Function<Class, LookupIndex> factory) {
        SPIFactory.setLookupIndexFactory(factory);
    }

    public static void setUniqueLookupIndexFactory(Function<Class, LookupIndex> factory) {
        SPIFactory.setUniqueLookupIndexFactory(factory);
    }

    public static void setUniqueSearchIndexFactory(Function<Class, SearchIndex> factory) {
        SPIFactory.setUniqueSearchIndexFactory(factory);
    }

    public static void setRepoFactory(Supplier<RepoComposer> factory) {
        SPIFactory.setRepoFactory(factory);
    }

    public static void setFilterFactory(Supplier<Filter> factory) {
        SPIFactory.setFilterFactory(factory);
    }

    public static RepoBuilder builder() {
        SPIFactory.init();
        return SPIFactory.getRepoBuilderFactory().get();
    }

}
