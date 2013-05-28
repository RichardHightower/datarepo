package org.datarepo;

import org.datarepo.modification.ModificationListener;
import org.datarepo.spi.SPIFactory;
import org.datarepo.spi.RepoComposer;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * Provides a builder for Repos.
 */
public interface RepoBuilder {


    public static void setRepoBuilder(Supplier<RepoBuilder> factory) {
        SPIFactory.setRepoBuilderFactory(factory);
    }

    public static void setDefaultSearchIndexFactory(Function<Class, SearchIndex>factory) {
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

    public RepoBuilder searchIndexFactory(Function<Class, SearchIndex> factory);

    public RepoBuilder lookupIndexFactory(Function<Class, LookupIndex> factory);

    public RepoBuilder uniqueLookupIndexFactory(Function<Class, LookupIndex> factory);

    public RepoBuilder uniqueSearchIndexFactory(Function<Class, SearchIndex> factory);

    public RepoBuilder repoFactory(Supplier<RepoComposer> factory);

    public RepoBuilder primaryKey(String propertyName);

    public RepoBuilder lookupIndex(String propertyName);

    public RepoBuilder lookupIndex(String propertyName, boolean unique);

    public RepoBuilder searchIndex(String propertyName);

    public RepoBuilder searchIndex(String propertyName, boolean unique);

    public RepoBuilder keyGetter(String propertyName, Function<?, ?> key);

    public RepoBuilder filterFactory(Supplier<Filter> factory);


    public RepoBuilder usePropertyForAccess(boolean useProperty);

    public RepoBuilder useFieldForAccess(boolean useField);

    public RepoBuilder useUnsafe(boolean useUnSafe);

    public RepoBuilder nullChecks(boolean nullChecks);

    public RepoBuilder addLogging(boolean logging);

    public RepoBuilder cloneEdits(boolean cloneEdits);

    RepoBuilder events(ModificationListener... listeners);

    RepoBuilder debug();


    public <KEY, ITEM> Repo<KEY, ITEM> build(Class<KEY> key, Class<ITEM> clazz);

    public static RepoBuilder getInstance() {
        SPIFactory.init();
        return SPIFactory.getRepoBuilderFactory().get();
    }

    RepoBuilder level(Level info);
}
