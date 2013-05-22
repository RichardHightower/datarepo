package org.datarepo.impl;

import org.datarepo.*;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Reflection;
import org.datarepo.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class RepoBuilderDefault implements RepoBuilder {

    Supplier<SearchIndex> searchIndexFactory;
    Supplier<LookupIndex> lookupIndexFactory;
    Supplier<RepoComposer> repoComposerFactory;
    Supplier<Filter> filterFactory;

    String primaryKey;
    Set<String> searchIndexes = new HashSet<>();
    Set<String> lookupIndexes = new HashSet<>();
    Map<String, Function> keyGetterMap = new HashMap();

    boolean useField = true;
    boolean useUnSafe = true;


    public void usePropertyForAccess(boolean useProperty) {
        this.useField = !useProperty;
    }

    public void useFieldForAccess(boolean useField) {
        this.useField = useField;
    }

    public void useUnsafe(boolean useUnSafe) {
        this.useUnSafe = useUnSafe;
    }

    @Override
    public RepoBuilder searchIndexFactory(Supplier<SearchIndex> factory) {
        this.searchIndexFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder lookupIndexFactory(Supplier<LookupIndex> factory) {
        this.lookupIndexFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder repoFactory(Supplier<RepoComposer> factory) {
        this.repoComposerFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder filterFactory(Supplier<Filter> factory) {
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
    public RepoBuilder keyGetter(String propertyName, Function<?, ?> keyGetter) {
        keyGetterMap.put(propertyName, keyGetter);
        return this;
    }

    private void init() {
        if (this.repoComposerFactory == null) {
            this.repoComposerFactory = RepoBuilderHelper.getRepoFactory();
        }
        if (this.lookupIndexFactory == null) {
            this.lookupIndexFactory = RepoBuilderHelper.getLookupIndexFactory();
        }
        if (this.searchIndexFactory == null) {
            this.searchIndexFactory = RepoBuilderHelper.getSearchIndexFactory();
        }
        if (this.filterFactory == null) {
            this.filterFactory = RepoBuilderHelper.getFilterFactory();
        }

    }

    @Override
    public <KEY, ITEM> Repo<KEY, ITEM> build(Class<KEY> key, Class<ITEM> clazz) {
        init();
        RepoComposer  repo = (RepoComposer ) this.repoComposerFactory.get();

        Map<String,FieldAccess> fields = null;

        if (useField) {
            fields = Utils.mp("name", Reflection.getAllAccessorFields(clazz, useUnSafe));

        } else {
            fields = Utils.mp("name", Reflection.getPropertyFieldAccessors(clazz));
        }

        configPrimaryKey(repo, fields);

        repo.setFilter(this.filterFactory.get());



        repo.setFields(fields);
        configSearchIndexes(repo, fields);



        return (Repo<KEY, ITEM>) repo;
    }

    private Function createKeyGetter(final FieldAccess field) {
        return new Function() {
            @Override
            public Object apply(Object o) {
                return field.getValue(o);
            }
        };
    }

    private  void configSearchIndexes(RepoComposer repo,
                                      Map<String,FieldAccess> fields) {
        for (String prop : searchIndexes) {
            SearchIndex searchIndex = this.searchIndexFactory.get();
            Function kg = getKeyGetterOrCreate(fields, prop);
            searchIndex.setKeyGetter(kg);
            repo.addSearchIndex(prop, searchIndex);
        }
    }

    private Function getKeyGetterOrCreate(Map<String, FieldAccess> fields, String prop) {
        Function kg = this.keyGetterMap.get(prop);

        if (kg == null) {
            FieldAccess field = fields.get(prop);
            kg = createKeyGetter(field);
            keyGetterMap.put(prop, kg);
        }
        return kg;
    }

    private  void configPrimaryKey(RepoComposer repo, Map<String,FieldAccess> fields) {
        LookupIndex primaryKeyIndex = this.lookupIndexFactory.get();
        primaryKeyIndex.setKeyGetter(getKeyGetterOrCreate(fields, this.primaryKey));
        repo.setPrimaryKeyName(this.primaryKey);
        repo.setPrimaryKeyGetter(this.keyGetterMap.get(this.primaryKey));
        repo.addLookupIndex(this.primaryKey,  primaryKeyIndex);
    }
}
