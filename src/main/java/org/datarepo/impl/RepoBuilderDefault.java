package org.datarepo.impl;

import org.datarepo.*;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Reflection;
import org.datarepo.spi.ObjectEditorComposer;
import org.datarepo.spi.RepoBuilderHelper;
import org.datarepo.spi.RepoComposer;
import org.datarepo.spi.SearchableCollectionComposer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;

public class RepoBuilderDefault implements RepoBuilder {

    Supplier<SearchIndex> searchIndexFactory;
    Supplier<LookupIndex> lookupIndexFactory;
    Supplier<LookupIndex> uniqueLookupIndexFactory;
    Supplier<SearchIndex> uniqueSearchIndexFactory;
    Supplier<ObjectEditorComposer> objectEditorFactory;
    Supplier<SearchableCollectionComposer> searchableCollectionFactory;


    Supplier<RepoComposer> repoComposerFactory;
    Supplier<Filter> filterFactory;

    String primaryKey;
    Set<String> searchIndexes = new HashSet<>();
    Set<String> lookupIndexes = new HashSet<>();
    Set<String> uniqueSearchIndexes = new HashSet<>();
    Set<String> uniqueLookupIndexes = new HashSet<>();

    Map<String, Function> keyGetterMap = new HashMap();


    boolean useField = true;
    boolean useUnSafe = true;
    boolean nullChecksAndLogging;
    boolean cloneEdits;

    boolean debug;
    Level level = Level.FINER;


    SearchableCollectionComposer query;


    public RepoBuilder usePropertyForAccess(boolean useProperty) {
        this.useField = !useProperty;
        return this;
    }

    public RepoBuilder useFieldForAccess(boolean useField) {
        this.useField = useField;
        return this;

    }

    public RepoBuilder useUnsafe(boolean useUnSafe) {
        this.useUnSafe = useUnSafe;
        return this;

    }

    @Override
    public RepoBuilder nullChecks(boolean nullChecks) {
        this.nullChecksAndLogging = nullChecks;
        return this;
    }

    @Override
    public RepoBuilder addLogging(boolean logging) {
        this.nullChecksAndLogging = logging;
        return this;
    }

    @Override
    public RepoBuilder cloneEdits(boolean cloneEdits) {
        this.cloneEdits = cloneEdits;
        return this;
    }

    @Override
    public RepoBuilder debug() {
        this.debug = true;
        return this;
    }

    @Override
    public RepoBuilder searchIndexFactory(Supplier<SearchIndex> factory) {
        this.searchIndexFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder uniqueLookupIndexFactory(Supplier<LookupIndex> factory) {
        this.uniqueLookupIndexFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder uniqueSearchIndexFactory(Supplier<SearchIndex> factory) {
        this.uniqueSearchIndexFactory = factory;
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

    public RepoBuilder lookupIndex(String propertyName, boolean unique) {
        if (unique) {
            this.lookupIndexes.add(propertyName);
        } else {
            this.uniqueLookupIndexes.add(propertyName);
        }
        return this;
    }

    @Override
    public RepoBuilder searchIndex(String propertyName) {
        this.searchIndexes.add(propertyName);
        return this;
    }

    public RepoBuilder searchIndex(String propertyName, boolean unique) {
        if (unique) {
            this.searchIndexes.add(propertyName);
        } else {
            this.uniqueSearchIndexes.add(propertyName);
        }
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
        if (this.uniqueLookupIndexFactory == null) {
            this.uniqueLookupIndexFactory = RepoBuilderHelper.getUniqueLookupIndexFactory();
        }
        if (this.searchableCollectionFactory == null) {
            this.searchableCollectionFactory = RepoBuilderHelper.getSearchableCollectionFactory();
        }
        if (this.filterFactory == null) {
            this.filterFactory = RepoBuilderHelper.getFilterFactory();
        }

        if (this.objectEditorFactory == null) {
            this.objectEditorFactory = RepoBuilderHelper.getObjectEditorFactory();
        }

    }

    @Override
    public <KEY, ITEM> Repo<KEY, ITEM> build(Class<KEY> key, Class<ITEM> clazz) {
        init();
        RepoComposer repo = (RepoComposer) this.repoComposerFactory.get();
        ObjectEditorComposer editorComposer = this.objectEditorFactory.get();
        ObjectEditor editor = (ObjectEditor) editorComposer;

        if (debug || nullChecksAndLogging) {
            ObjectEditorLogNullCheckDecorator logNullCheckDecorator = new ObjectEditorLogNullCheckDecorator(editor);
            logNullCheckDecorator.setLevel(level);
            logNullCheckDecorator.setDebug(debug);

            editor = logNullCheckDecorator;
        }

        if (cloneEdits) {
            editor = new ObjectEditorCloneDecorator(editor);
        }

        repo.setObjectEditor(editor);


        Map<String, FieldAccess> fields = null;

        if (useField) {
            fields = Reflection.getAllAccessorFields(clazz, useUnSafe);

        } else {
            Map<String, FieldAccess> realFields = Reflection.getAllAccessorFields(clazz, useUnSafe);

            fields = Reflection.getPropertyFieldAccessors(clazz);

            /* Add missing fields */
            for (Map.Entry<String, FieldAccess> field : realFields.entrySet()) {
                if (!fields.containsKey(field.getKey())) {
                    fields.put(field.getKey(), field.getValue());
                }
            }
        }

        editorComposer.setFields(fields);


        query = searchableCollectionFactory.get();
        query.setFilter(this.filterFactory.get());
        query.setFields(fields);


        configPrimaryKey(fields);
        configIndexes(repo, fields);
        query.init();


        repo.setSearchableCollection((SearchableCollection<KEY, ITEM>) query);
        editorComposer.setSearchableCollection((SearchableCollection<KEY, ITEM>) query);


        return (Repo<KEY, ITEM>) repo;
    }

    @Override
    public RepoBuilder level(Level level) {
        this.level = level;
        return this;
    }

    private Function createKeyGetter(final FieldAccess field) {
        return new Function() {
            @Override
            public Object apply(Object o) {
                return field.getValue(o);
            }
        };
    }

    private void configIndexes(RepoComposer repo,
                               Map<String, FieldAccess> fields) {
        SearchableCollection query = (SearchableCollection) this.query;
        for (String prop : searchIndexes) {
            SearchIndex searchIndex = this.searchIndexFactory.get();
            Function kg = getKeyGetterOrCreate(fields, prop);
            searchIndex.setKeyGetter(kg);
            query.addSearchIndex(prop, searchIndex);
        }
        for (String prop : uniqueSearchIndexes) {
            SearchIndex searchIndex = this.uniqueSearchIndexFactory.get();
            Function kg = getKeyGetterOrCreate(fields, prop);
            searchIndex.setKeyGetter(kg);
            query.addSearchIndex(prop, searchIndex);
        }

        for (String prop : lookupIndexes) {
            LookupIndex index = this.lookupIndexFactory.get();
            Function kg = getKeyGetterOrCreate(fields, prop);
            index.setKeyGetter(kg);
            query.addLookupIndex(prop, index);
        }
        for (String prop : uniqueLookupIndexes) {
            LookupIndex index = this.uniqueLookupIndexFactory.get();
            Function kg = getKeyGetterOrCreate(fields, prop);
            index.setKeyGetter(kg);
            query.addLookupIndex(prop, index);
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

    private void configPrimaryKey(Map<String, FieldAccess> fields) {
        LookupIndex primaryKeyIndex = this.uniqueLookupIndexFactory.get();
        primaryKeyIndex.setKeyGetter(getKeyGetterOrCreate(fields, this.primaryKey));
        query.setPrimaryKeyName(this.primaryKey);
        query.setPrimaryKeyGetter(this.keyGetterMap.get(this.primaryKey));
        ((SearchableCollection) query).addLookupIndex(this.primaryKey, primaryKeyIndex);
    }


}
