package org.datarepo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RepoBuilderDefault implements RepoBuilder {

    Factory<SearchIndex> searchIndexFactory;
    Factory<LookupIndex> lookupIndexFactory;
    Factory<RepoComposer> repoComposerFactory;
    Factory<Filter> filterFactory;

    String primaryKey;
    Set<String> searchIndexes = new HashSet<>();
    Set<String> lookupIndexes = new HashSet<>();
    Map<String, KeyGetter> keyGetterMap = new HashMap();

    @Override
    public RepoBuilder searchIndexFactory(Factory<SearchIndex> factory) {
        this.searchIndexFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder lookupIndexFactory(Factory<LookupIndex> factory) {
        this.lookupIndexFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder repoFactory(Factory<RepoComposer> factory) {
        this.repoComposerFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder filterFactory(Factory<Filter> factory) {
        this.filterFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder primaryKey(String propertyName) {
        this.primaryKey = propertyName;
        return this;
    }

    @Override
    public RepoBuilder lookupIndex(String propertyName) {
        this.lookupIndexes.add(propertyName);
        return this;
    }

    @Override
    public RepoBuilder searchIndex(String propertyName) {
        this.searchIndexes.add(propertyName);
        return this;
    }

    @Override
    public RepoBuilder keyGetter(String propertyName, KeyGetter<?, ?> keyGetter) {
        keyGetterMap.put(propertyName, keyGetter);
        return this;
    }

    private void init() {
        if (this.repoComposerFactory == null) {
            this.repoComposerFactory = RepoBuilderHelper.repoFactory;
        }
        if (this.lookupIndexFactory == null) {
            this.lookupIndexFactory = RepoBuilderHelper.lookupIndexFactory;
        }
        if (this.searchIndexFactory == null) {
            this.searchIndexFactory = RepoBuilderHelper.searchIndexFactory;
        }
        if (this.filterFactory == null) {
            this.filterFactory = RepoBuilderHelper.filterFactory;
        }

    }

    @Override
    public <KEY, ITEM> Repo<KEY, ITEM> build(Class<KEY> key, Class<ITEM> clazz) {
        init();
        RepoComposer <ITEM> repo = (RepoComposer <ITEM>) this.repoComposerFactory.create();

        configPrimaryKey(repo);

        repo.setFilter(this.filterFactory.create());

        configSearchIndexes(repo);


        return (Repo<KEY, ITEM>) repo;
    }

    private <KEY, ITEM> void configSearchIndexes(RepoComposer<ITEM> repo) {
        for (String prop : searchIndexes) {
            SearchIndex<KEY, ITEM> searchIndex = this.searchIndexFactory.create();
            KeyGetter kg = this.keyGetterMap.get(prop);
            searchIndex.setKeyGetter(kg);
            repo.addSearchIndex(prop, searchIndex);
        }
    }

    private <KEY, ITEM> void configPrimaryKey(RepoComposer<ITEM> repo) {
        LookupIndex<KEY, ITEM> primaryKeyIndex = this.lookupIndexFactory.create();
        primaryKeyIndex.setKeyGetter(this.keyGetterMap.get(this.primaryKey));
        repo.setPrimaryKeyName(this.primaryKey);
        repo.setPrimaryKeyGetter(this.keyGetterMap.get(this.primaryKey));
        repo.addLookupIndex(this.primaryKey,  primaryKeyIndex);
    }
}
