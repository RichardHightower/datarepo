package org.datarepo.impl;

import org.datarepo.*;
import org.datarepo.impl.*;

import java.util.function.Supplier;

/**
 * Helper class for RepoBuilderHelper interface.
 */
public class RepoBuilderHelper {

    static Supplier<RepoBuilder> repoBuilderFactory = null;
    static Supplier<SearchIndex> searchIndexFactory = null;
    static Supplier<LookupIndex> lookupIndexFactory = null;
    static Supplier<RepoComposer> repoFactory = null;
    static Supplier<Filter> filterFactory = null;

    public static Supplier<RepoBuilder> getRepoBuilderFactory() {
        return repoBuilderFactory;
    }

    public static Supplier<SearchIndex> getSearchIndexFactory() {
        return searchIndexFactory;
    }

    public static Supplier<LookupIndex> getLookupIndexFactory() {
        return lookupIndexFactory;
    }

    public static Supplier<RepoComposer> getRepoFactory() {
        return repoFactory;
    }

    public static Supplier<Filter> getFilterFactory() {
        return filterFactory;
    }

    public static void init() {
        if (repoBuilderFactory == null) {
            repoBuilderFactory = new Supplier<RepoBuilder>() {
                @Override
                public RepoBuilder get() {
                    return new RepoBuilderDefault();
                }
            };
        }
        if (searchIndexFactory == null) {
            searchIndexFactory = new Supplier<SearchIndex>() {
                @Override
                public SearchIndex get() {
                    return new SearchIndexDefault();
                }
            };
        }
        if (lookupIndexFactory ==  null) {
            lookupIndexFactory = new Supplier<LookupIndex>() {
                @Override
                public LookupIndex get() {

                    return new LookupIndexDefault();
                }
            };
        }
        if (repoFactory == null) {
            repoFactory = new Supplier<RepoComposer>() {
                @Override
                public RepoComposer get() {
                    return new RepoDefault<>();
                }
            };
        }

        if (filterFactory == null ) {
            filterFactory = new Supplier<Filter>() {
                @Override
                public Filter get() {
                    return new FilterDefault();
                }
            };
        }
    }


    public static void setRepoBuilderFactory(Supplier<RepoBuilder> repoBuilderFactory) {
        RepoBuilderHelper.repoBuilderFactory = repoBuilderFactory;
    }

    public static void setSearchIndexFactory(Supplier<SearchIndex> searchIndexFactory) {
        RepoBuilderHelper.searchIndexFactory = searchIndexFactory;
    }

    public static void setLookupIndexFactory(Supplier<LookupIndex> lookupIndexFactory) {
        RepoBuilderHelper.lookupIndexFactory = lookupIndexFactory;
    }

    public static void setRepoFactory(Supplier<RepoComposer> repoFactory) {
        RepoBuilderHelper.repoFactory = repoFactory;
    }

    public static void setFilterFactory(Supplier<Filter> filterFactory) {
        RepoBuilderHelper.filterFactory = filterFactory;
    }

}
