package org.datarepo;

import org.datarepo.impl.RepoBuilderHelper;

/**
 * Provides a builder for Repos.
 */
public interface RepoBuilder {



    public static void setRepoBuilder(Factory<RepoBuilder> factory) {
        RepoBuilderHelper.setRepoBuilderFactory(factory);
    }

    public static void setDefaultSearchIndexFactory(Factory<SearchIndex> factory) {
        RepoBuilderHelper.setSearchIndexFactory(factory);
    }

    public static void setLookupIndexFactory(Factory<LookupIndex> factory) {
        RepoBuilderHelper.setLookupIndexFactory(factory);
    }

    public static void setRepoFactory(Factory<RepoComposer> factory) {
        RepoBuilderHelper.setRepoFactory(factory);
    }

    public RepoBuilder searchIndexFactory(Factory<SearchIndex> factory);

    public RepoBuilder lookupIndexFactory(Factory<LookupIndex> factory);

    public RepoBuilder repoFactory(Factory<RepoComposer> factory);

    public RepoBuilder primaryKey(String propertyName);

    public RepoBuilder lookupIndex(String propertyName);

    public RepoBuilder searchIndex(String propertyName);

    public RepoBuilder keyGetter(String propertyName, KeyGetter<?, ?> key);

    public RepoBuilder filterFactory(Factory<Filter> factory);


    public void usePropertyForAccess(boolean useProperty);

    public void useFieldForAccess(boolean useField);

    public void useUnsafe(boolean useUnSafe);


    public <KEY, ITEM> Repo<KEY, ITEM> build(Class<KEY> key, Class<ITEM> clazz);

    public static RepoBuilder getInstance() {
        RepoBuilderHelper.init();
        return RepoBuilderHelper.getRepoBuilderFactory().create();
    }

}
