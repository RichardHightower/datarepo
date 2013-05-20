package org.datarepo;

import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Reflection;

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
        RepoComposer  repo = (RepoComposer ) this.repoComposerFactory.create();

        configPrimaryKey(repo);

        repo.setFilter(this.filterFactory.create());


        Map<String,FieldAccess> fields = Utils.mp("name", Reflection.getAllAccessorFields(clazz));

        repo.setFields(fields);
        configSearchIndexes(repo);



        return (Repo<KEY, ITEM>) repo;
    }

    private  void configSearchIndexes(RepoComposer repo) {
        for (String prop : searchIndexes) {
            SearchIndex searchIndex = this.searchIndexFactory.create();
            KeyGetter kg = this.keyGetterMap.get(prop);
            searchIndex.setKeyGetter(kg);
            repo.addSearchIndex(prop, searchIndex);
        }
    }

    private  void configPrimaryKey(RepoComposer repo) {
        LookupIndex primaryKeyIndex = this.lookupIndexFactory.create();
        primaryKeyIndex.setKeyGetter(this.keyGetterMap.get(this.primaryKey));
        repo.setPrimaryKeyName(this.primaryKey);
        repo.setPrimaryKeyGetter(this.keyGetterMap.get(this.primaryKey));
        repo.addLookupIndex(this.primaryKey,  primaryKeyIndex);
    }
}
