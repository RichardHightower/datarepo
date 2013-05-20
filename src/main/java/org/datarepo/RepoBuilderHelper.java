package org.datarepo;

/**
 * Helper class for RepoBuilderHelper interface.
 */
class RepoBuilderHelper {

    static Factory<RepoBuilder> repoBuilderFactory = null;
    static Factory<SearchIndex> searchIndexFactory = null;
    static Factory<LookupIndex> lookupIndexFactory = null;
    static Factory<RepoComposer> repoFactory = null;
    static Factory<Filter> filterFactory = null;



    static void init() {
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
}
