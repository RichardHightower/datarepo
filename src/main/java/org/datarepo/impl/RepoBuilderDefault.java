package org.datarepo.impl;

import org.datarepo.*;
import org.datarepo.fields.FieldAccess;
import org.datarepo.impl.decorators.FilterWithSimpleCache;
import org.datarepo.impl.decorators.ObjectEditorCloneDecorator;
import org.datarepo.impl.decorators.ObjectEditorEventDecorator;
import org.datarepo.impl.decorators.ObjectEditorLogNullCheckDecorator;
import org.datarepo.modification.ModificationListener;
import org.datarepo.spi.*;
import org.datarepo.utils.Reflection;
import org.datarepo.utils.Utils;

import java.text.Collator;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;


public class RepoBuilderDefault implements RepoBuilder {

    Function<Class, SearchIndex> searchIndexFactory;
    Function<Class, LookupIndex> lookupIndexFactory;
    Function<Class, LookupIndex> uniqueLookupIndexFactory;
    Function<Class, SearchIndex> uniqueSearchIndexFactory;
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
    boolean storeKeyInIndexOnly;
    boolean debug;
    Level level = Level.FINER;
    private Map<String, FieldAccess> fields;
    private RepoComposer repo;
    private ObjectEditor editor;
    private SearchableCollectionComposer query;
    private boolean cache = true;
    private Map<String, Comparator> collators = new HashMap<String, Comparator>();
    private Map<String, Function> keyTransformers = new HashMap<>();


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
    public RepoBuilder useCache() {
        this.cache = true;
        return this;
    }

    @Override
    public RepoBuilder storeKeyInIndexOnly() {
        this.storeKeyInIndexOnly = true;

        return this;
    }

    boolean events = false;
    ModificationListener[] listeners;

    @Override
    public RepoBuilder events(ModificationListener... listeners) {
        events = true;
        this.listeners = listeners;
        return this;
    }

    @Override
    public RepoBuilder debug() {
        this.debug = true;
        return this;
    }

    @Override
    public RepoBuilder searchIndexFactory(Function<Class, SearchIndex> factory) {
        this.searchIndexFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder uniqueLookupIndexFactory(Function<Class, LookupIndex> factory) {
        this.uniqueLookupIndexFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder uniqueSearchIndexFactory(Function<Class, SearchIndex> factory) {
        this.uniqueSearchIndexFactory = factory;
        return this;
    }

    @Override
    public RepoBuilder lookupIndexFactory(Function<Class, LookupIndex> factory) {
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
    public RepoBuilder uniqueLookupIndex(String propertyName) {
        return this.lookupIndex(propertyName, true);
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

    @Override
    public RepoBuilder uniqueSearchIndex(String propertyName) {
        return searchIndex(propertyName, true);
    }


    @Override
    public RepoBuilder collateIndex(String propertyName) {
        collators.put(propertyName, Collator.getInstance());
        return this;
    }

    @Override
    public RepoBuilder collateIndex(String propertyName, Locale locale) {
        collators.put(propertyName, Collator.getInstance(locale));
        return this;
    }

    @Override
    public RepoBuilder collateIndex(String propertyName, Comparator collator) {
        collators.put(propertyName, collator);
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
            this.repoComposerFactory = SPIFactory.getRepoFactory();
        }
        if (this.lookupIndexFactory == null) {
            this.lookupIndexFactory = SPIFactory.getLookupIndexFactory();
        }
        if (this.searchIndexFactory == null) {
            this.searchIndexFactory = SPIFactory.getSearchIndexFactory();
        }
        if (this.uniqueLookupIndexFactory == null) {
            this.uniqueLookupIndexFactory = SPIFactory.getUniqueLookupIndexFactory();
        }
        if (this.searchableCollectionFactory == null) {
            this.searchableCollectionFactory = SPIFactory.getSearchableCollectionFactory();
        }
        if (this.filterFactory == null) {
            this.filterFactory = SPIFactory.getFilterFactory();
        }

        if (this.objectEditorFactory == null) {
            this.objectEditorFactory = SPIFactory.getObjectEditorFactory();
        }

    }

    @Override
    public <KEY, ITEM> Repo<KEY, ITEM> build(Class<KEY> key, Class<ITEM> clazz) {
        return build(null, key, clazz);
    }

    public <KEY, ITEM> Repo<KEY, ITEM> build(Class<?> primitiveKey, Class<KEY> key, Class<ITEM> clazz) {
        init();

        this.fields = Reflection.getPropertyFieldAccessMap(clazz, useField, useUnSafe);


        /* Construct */
        this.repo = (RepoComposer) this.repoComposerFactory.get();
        this.editor = constructObjectEditor(fields);
        SearchableCollectionComposer query = constructSearchableCollection(primitiveKey, clazz, repo, fields);

        /* Inject */
        repo.setSearchableCollection((SearchableCollection<KEY, ITEM>) query);
        ((ObjectEditorComposer) editor).setSearchableCollection((SearchableCollection<KEY, ITEM>) query);
        repo.setObjectEditor((ObjectEditor) editor);

        editor = decorateEditor(editor);

        return (Repo<KEY, ITEM>) repo;
    }

    private SearchableCollectionComposer constructSearchableCollection(Class<?> primitiveKey, Class<?> itemClazz, RepoComposer repo, Map<String, FieldAccess> fields) {

        query = searchableCollectionFactory.get();

        Filter filter = this.filterFactory.get();


        configPrimaryKey(primitiveKey == null ? itemClazz : primitiveKey, fields);
        configIndexes(repo, fields);


        query.setFilter(filter);


        query.setFields(fields);

        query.init();

        if (this.cache) {
            filter = new FilterWithSimpleCache(filter);
        }

        query.setFilter(filter);

        return query;
    }

    private ObjectEditor constructObjectEditor(Map<String, FieldAccess> fields) {
        ObjectEditorComposer editorComposer = this.objectEditorFactory.get();

        ObjectEditor editor = (ObjectEditor) editorComposer;
        editorComposer.init();


        editorComposer.setFields(fields);
        return editor;
    }


    private ObjectEditor decorateEditor(ObjectEditor editor) {
        if (debug || nullChecksAndLogging) {
            ObjectEditorLogNullCheckDecorator logNullCheckDecorator = new ObjectEditorLogNullCheckDecorator(editor);
            logNullCheckDecorator.setLevel(level);
            logNullCheckDecorator.setDebug(debug);

            editor = logNullCheckDecorator;
        }

        if (cloneEdits) {
            editor = new ObjectEditorCloneDecorator(editor);
        }

        if (events) {
            ObjectEditorEventDecorator eventManager = new ObjectEditorEventDecorator(editor);

            for (ModificationListener l : listeners) {
                eventManager.add(l);
            }
            editor = eventManager;
        }
        return editor;
    }

    @Override
    public RepoBuilder level(Level level) {
        this.level = level;
        return this;
    }

    @Override
    public RepoBuilder upperCaseIndex(String property) {
        this.keyTransformers.put(property, Utils.upperCase);
        return this;
    }

    @Override
    public RepoBuilder lowerCaseIndex(String property) {
        this.keyTransformers.put(property, Utils.lowerCase);
        return this;

    }

    @Override
    public RepoBuilder camelCaseIndex(String property) {
        this.keyTransformers.put(property, Utils.camelCase);
        return this;

    }

    @Override
    public RepoBuilder underBarCaseIndex(String property) {
        this.keyTransformers.put(property, Utils.underBarCase);
        return this;
    }

    @Override
    public RepoBuilder nestedIndex(String... property) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
        //11
    }

    private Function createKeyGetter(final FieldAccess field) {
        Utils.notNull(field);
        return new Function() {
            @Override
            public Object apply(Object o) {
                return field.getValue(o);
            }
        };
    }

    private void configIndexes(RepoComposer repo,
                               Map<String, FieldAccess> fields) {


        for (String prop : searchIndexes) {
            SearchIndex searchIndex = this.searchIndexFactory.apply(fields.get(prop).getType());
            searchIndex.setComparator(this.collators.get(prop));
            searchIndex.setInputKeyTransformer(this.keyTransformers.get(prop));
            Function kg = getKeyGetterOrCreate(fields, prop);
            searchIndex.setKeyGetter(kg);
            searchIndex.init();
            ((SearchableCollection) query).addSearchIndex(prop, searchIndex);
        }
        for (String prop : uniqueSearchIndexes) {
            SearchIndex searchIndex = this.uniqueSearchIndexFactory.apply(fields.get(prop).getType());
            searchIndex.setComparator(this.collators.get(prop));
            searchIndex.setInputKeyTransformer(this.keyTransformers.get(prop));
            searchIndex.init();
            Function kg = getKeyGetterOrCreate(fields, prop);
            searchIndex.setKeyGetter(kg);
            ((SearchableCollection) query).addSearchIndex(prop, searchIndex);
        }

        for (String prop : lookupIndexes) {
            LookupIndex index = this.lookupIndexFactory.apply(fields.get(prop).getType());
            Function kg = getKeyGetterOrCreate(fields, prop);
            index.setInputKeyTransformer(this.keyTransformers.get(prop));
            index.setKeyGetter(kg);
            index.init();
            ((SearchableCollection) query).addLookupIndex(prop, index);
        }
        for (String prop : uniqueLookupIndexes) {
            LookupIndex index = this.uniqueLookupIndexFactory.apply(fields.get(prop).getType());
            Function kg = getKeyGetterOrCreate(fields, prop);
            index.setInputKeyTransformer(this.keyTransformers.get(prop));
            index.setKeyGetter(kg);
            index.init();
            ((SearchableCollection) query).addLookupIndex(prop, index);
        }

    }

    private Function getKeyGetterOrCreate(Map<String, FieldAccess> fields, String prop) {
        Utils.notNull(fields, prop);
        Function kg = null;

        kg = this.keyGetterMap.get(prop);

        if (kg == null) {
            FieldAccess field = fields.get(prop);
            kg = createKeyGetter(field);

            keyGetterMap.put(prop, kg);
        }
        return kg;

    }

    private void configPrimaryKey(Class<?> type, Map<String, FieldAccess> fields) {
        LookupIndex primaryKeyIndex = this.uniqueLookupIndexFactory.apply(type);


        Utils.notNull(primaryKey);

        if (!fields.containsKey(primaryKey)) {
            Utils.complain("Fields does not have primary key %s", primaryKey);
        }


        primaryKeyIndex.setKeyGetter(getKeyGetterOrCreate(fields, this.primaryKey));
        ((SearchableCollectionComposer) query).setPrimaryKeyName(this.primaryKey);
        ((SearchableCollectionComposer) query).setPrimaryKeyGetter(this.keyGetterMap.get(this.primaryKey));
        ((SearchableCollection) query).addLookupIndex(this.primaryKey, primaryKeyIndex);


    }


}
