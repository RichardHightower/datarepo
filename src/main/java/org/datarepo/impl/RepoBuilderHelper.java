package org.datarepo.impl;

import org.datarepo.*;
import org.datarepo.impl.*;

/**
 * Helper class for RepoBuilderHelper interface.
 */
public class RepoBuilderHelper {

    static Factory<RepoBuilder> repoBuilderFactory = null;
    static Factory<SearchIndex> searchIndexFactory = null;
    static Factory<LookupIndex> lookupIndexFactory = null;
    static Factory<RepoComposer> repoFactory = null;
    static Factory<Filter> filterFactory = null;

    public static Factory<RepoBuilder> getRepoBuilderFactory() {
        return repoBuilderFactory;
    }

    public static Factory<SearchIndex> getSearchIndexFactory() {
        return searchIndexFactory;
    }

    public static Factory<LookupIndex> getLookupIndexFactory() {
        return lookupIndexFactory;
    }

    public static Factory<RepoComposer> getRepoFactory() {
        return repoFactory;
    }

    public static Factory<Filter> getFilterFactory() {
        return filterFactory;
    }

    public static void init() {
        if (repoBuilderFactory == null) {
            repoBuilderFactory = new Factory<RepoBuilder>() {
                @Override
                public RepoBuilder create() {
                    return new RepoBuilderDefault();
                }
            };
        }
        if (searchIndexFactory == null) {
            searchIndexFactory = new Factory<SearchIndex>() {
                @Override
                public SearchIndex create() {
                    return new SearchIndexDefault();
                }
            };
        }
        if (lookupIndexFactory ==  null) {
            lookupIndexFactory = new Factory<LookupIndex>() {
                @Override
                public LookupIndex create() {

                    return new LookupIndexDefault();
                }
            };
        }
        if (repoFactory == null) {
            repoFactory = new Factory<RepoComposer>() {
                @Override
                public RepoComposer create() {
                    return new RepoDefault<>();
                }
            };
        }

        if (filterFactory == null ) {
            filterFactory = new Factory<Filter>() {
                @Override
                public Filter create() {
                    return new FilterDefault();
                }
            };
        }
    }


    public static void setRepoBuilderFactory(Factory<RepoBuilder> repoBuilderFactory) {
        RepoBuilderHelper.repoBuilderFactory = repoBuilderFactory;
    }

    public static void setSearchIndexFactory(Factory<SearchIndex> searchIndexFactory) {
        RepoBuilderHelper.searchIndexFactory = searchIndexFactory;
    }

    public static void setLookupIndexFactory(Factory<LookupIndex> lookupIndexFactory) {
        RepoBuilderHelper.lookupIndexFactory = lookupIndexFactory;
    }

    public static void setRepoFactory(Factory<RepoComposer> repoFactory) {
        RepoBuilderHelper.repoFactory = repoFactory;
    }

    public static void setFilterFactory(Factory<Filter> filterFactory) {
        RepoBuilderHelper.filterFactory = filterFactory;
    }

}
