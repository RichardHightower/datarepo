package org.datarepo;

import org.datarepo.impl.RepoBuilderHelper;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides a builder for Repos.
 */
public interface RepoBuilder {



    public static void setRepoBuilder(Supplier<RepoBuilder> factory) {
        RepoBuilderHelper.setRepoBuilderFactory(factory);
    }

    public static void setDefaultSearchIndexFactory(Supplier<SearchIndex> factory) {
        RepoBuilderHelper.setSearchIndexFactory(factory);
    }

    public static void setLookupIndexFactory(Supplier<LookupIndex> factory) {
        RepoBuilderHelper.setLookupIndexFactory(factory);
    }

    public static void setUniqueLookupIndexFactory(Supplier<LookupIndex> factory) {
        RepoBuilderHelper.setUniqueLookupIndexFactory(factory);
    }

    public static void setUniqueSearchIndexFactory(Supplier<SearchIndex> factory) {
        RepoBuilderHelper.setUniqueSearchIndexFactory(factory);
    }

    public static void setRepoFactory(Supplier<RepoComposer> factory) {
        RepoBuilderHelper.setRepoFactory(factory);
    }

    public static void setFilterFactory(Supplier<Filter> factory) {
        RepoBuilderHelper.setFilterFactory(factory);
    }

    public RepoBuilder searchIndexFactory(Supplier<SearchIndex> factory);

    public RepoBuilder lookupIndexFactory(Supplier<LookupIndex> factory);

    public RepoBuilder uniqueLookupIndexFactory(Supplier<LookupIndex> factory);

    public RepoBuilder uniqueSearchIndexFactory(Supplier<SearchIndex> factory);

    public RepoBuilder repoFactory(Supplier <RepoComposer> factory);

    public RepoBuilder primaryKey(String propertyName);

    public RepoBuilder lookupIndex(String propertyName);

    public RepoBuilder lookupIndex(String propertyName, boolean unique);

    public RepoBuilder searchIndex(String propertyName);

    public RepoBuilder searchIndex(String propertyName, boolean unique);

    public RepoBuilder keyGetter(String propertyName, Function<?, ?> key);

    public RepoBuilder filterFactory(Supplier<Filter> factory);


    public void usePropertyForAccess(boolean useProperty);

    public void useFieldForAccess(boolean useField);

    public void useUnsafe(boolean useUnSafe);


    public <KEY, ITEM> Repo<KEY, ITEM> build(Class<KEY> key, Class<ITEM> clazz);

    public static RepoBuilder getInstance() {
        RepoBuilderHelper.init();
        return RepoBuilderHelper.getRepoBuilderFactory().get();
    }

}
