package org.datarepo;

class RepoBuilderHelper {

    static Factory<RepoBuilder> repoBuilderFactory = null;
    static Factory<SearchIndex> searchIndexFactory = null;
    static Factory<LookupIndex> lookupIndexFactory = null;
    static Factory<RepoComposer> repoFactory = null;


    static Factory<RepoBuilder> getRepoBuilderFactory() {
        return repoBuilderFactory;
    }

    static Factory<SearchIndex> getSearchIndexFactory() {
        return searchIndexFactory;
    }

    static Factory<LookupIndex> getLookupIndexFactory() {
        return lookupIndexFactory;
    }

    static Factory<RepoComposer> getRepoFactory() {
        return repoFactory;
    }


    static void init() {
        if (repoBuilderFactory == null) {
            repoBuilderFactory = new Factory<RepoBuilder>() {
                @Override
                public RepoBuilder create() {
                    return null;
                }
            };
        }
        if (searchIndexFactory == null) {
            searchIndexFactory = new Factory<SearchIndex>() {
                @Override
                public SearchIndex create() {
                    return null;
                }
            };
        }
        if (lookupIndexFactory ==  null) {
            lookupIndexFactory = new Factory<LookupIndex>() {
                @Override
                public LookupIndex create() {

                    return null;
                }
            };
        }
        if (repoFactory == null) {
            repoFactory = new Factory<RepoComposer>() {
                @Override
                public RepoComposer create() {
                    return null;
                }
            };
        }
    }
}
