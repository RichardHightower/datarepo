package org.datarepo;

import org.datarepo.modification.ModificationListener;
import org.datarepo.spi.RepoComposer;
import org.datarepo.spi.SearchIndex;
import org.datarepo.utils.Function;
import org.datarepo.utils.Supplier;

import java.util.Comparator;
import java.util.Locale;
import java.util.logging.Level;

/**
 * Provides a builder for Repos.
 */
public interface RepoBuilder {


    public RepoBuilder searchIndexFactory(Function<Class, SearchIndex> factory);

    public RepoBuilder lookupIndexFactory(Function<Class, LookupIndex> factory);

    public RepoBuilder uniqueLookupIndexFactory(Function<Class, LookupIndex> factory);

    public RepoBuilder uniqueSearchIndexFactory(Function<Class, SearchIndex> factory);

    public RepoBuilder repoFactory(Supplier<RepoComposer> factory);

    public RepoBuilder primaryKey(String propertyName);

    public RepoBuilder lookupIndex(String propertyName);

    public RepoBuilder uniqueLookupIndex(String propertyName);

    public RepoBuilder searchIndex(String propertyName);

    public RepoBuilder uniqueSearchIndex(String propertyName);

    public RepoBuilder collateIndex(String propertyName, Comparator collator);

    public RepoBuilder collateIndex(String propertyName);

    public RepoBuilder collateIndex(String propertyName, Locale locale);

    public RepoBuilder keyGetter(String propertyName, Function<?, ?> key);

    public RepoBuilder filterFactory(Supplier<Filter> factory);


    public RepoBuilder usePropertyForAccess(boolean useProperty);

    public RepoBuilder useFieldForAccess(boolean useField);

    public RepoBuilder useUnsafe(boolean useUnSafe);

    public RepoBuilder nullChecks(boolean nullChecks);

    public RepoBuilder addLogging(boolean logging);

    public RepoBuilder cloneEdits(boolean cloneEdits);

    public RepoBuilder useCache();

    public RepoBuilder storeKeyInIndexOnly();

    RepoBuilder events(ModificationListener... listeners);

    RepoBuilder debug();


    <KEY, ITEM> Repo<KEY, ITEM> build(Class<KEY> key, Class<ITEM> clazz, Class<?>... all);


    RepoBuilder level(Level info);

    RepoBuilder upperCaseIndex(String property);

    RepoBuilder lowerCaseIndex(String property);

    RepoBuilder camelCaseIndex(String property);

    RepoBuilder underBarCaseIndex(String property);

    RepoBuilder nestedIndex(String... propertyPath);

    RepoBuilder indexHierarchy();

    RepoBuilder indexBucketSize(String propertyName, int size);


}
