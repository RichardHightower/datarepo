package org.datarepo;

import org.datarepo.fields.FieldAccess;
import org.datarepo.impl.decorators.FilterWithSimpleCache;
import org.datarepo.query.Expression;
import org.datarepo.spi.SPIFactory;
import org.datarepo.spi.SearchIndex;
import org.datarepo.spi.SearchableCollectionComposer;
import org.datarepo.utils.Function;
import org.datarepo.utils.Reflection;
import org.datarepo.utils.Types;
import org.datarepo.utils.Utils;

import java.util.*;


public class Collections {

    public static <T> List<T> $q(final List<T> list, Class<?>... classes) {
        return listQuery(list, true, true, classes);
    }

    public static <T> List<T> $c(final List<T> list) {
        return plainList(list);
    }

    private static <T> List<T> plainList(List<T> list) {
        if (list instanceof QList) {
            return ((QList) list).list;
        } else {
            return list;
        }
    }

    public static <T> List<T> listQuery(final List<T> list) {
        return listQuery(list, true, true);
    }

    public static <T> List<T> listQuery(final List<T> list, boolean useField, boolean useUnSafe, Class<?>... classes) {
        if (list == null || list.size() == 0) {
            return list;
        }

        SearchableCollectionComposer query = null;

        if (classes == null || classes.length == 0) {
            Class<?> clazz = list.get(0).getClass();

            query = getSearchableCollectionComposer(list, useField, useUnSafe, clazz);

        } else {
            query = getSearchableCollectionComposer(list, useField, useUnSafe, classes);

        }

        return new QList<T>(list, (SearchableCollection) query);
    }

    public static <T> Set<T> $q(final Set<T> set) {
        return setQuery(set, true, true);
    }

    public static <T> Set<T> $c(final Set<T> set) {
        return plainSet(set);
    }

    private static <T> Set<T> plainSet(Set<T> set) {
        if (set instanceof QSet) {
            return ((QSet) set).set;
        } else {
            return set;
        }
    }

    public static <T> Set<T> listQuery(final Set<T> set) {
        return setQuery(set, true, true);
    }

    public static <T> Set<T> setQuery(final Set<T> set, boolean useField, boolean useUnSafe) {
        if (set == null || set.size() == 0) {
            return set;
        }

        Class<?> clazz = set.iterator().next().getClass();

        SearchableCollectionComposer query = getSearchableCollectionComposer(set, useField, useUnSafe, clazz);

        return new QSet<T>(set, (SearchableCollection) query);
    }

    private static <T> SearchableCollectionComposer getSearchableCollectionComposer(Collection set, boolean useField, boolean useUnSafe, Class<?>... classes) {
        SearchableCollectionComposer query = SPIFactory.getSearchableCollectionFactory().get();


        Map<String, FieldAccess> fields = new HashMap<>();

        for (Class<?> cls : classes) {

            Map<String, FieldAccess> fieldsSubType
                    = Reflection.getPropertyFieldAccessMap(cls, useField, useUnSafe);

            for (String sKey : fieldsSubType.keySet()) {
                if (!fields.containsKey(sKey)) {
                    fields.put(sKey, fieldsSubType.get(sKey));
                }
            }
        }

        String primaryKey = findPrimaryKey(fields);
        FieldAccess field = fields.get(primaryKey);
        Function keyGetter = createKeyGetter(field);

        query.setFields(fields);
        query.setPrimaryKeyGetter(keyGetter);
        query.setPrimaryKeyName(primaryKey);
        Filter filter = SPIFactory.getFilterFactory().get();
        query.setFilter(filter);


        LookupIndex index = SPIFactory.getUniqueLookupIndexFactory().apply(fields.get(primaryKey).getType());
        index.setKeyGetter(keyGetter);
        ((SearchableCollection) query).addLookupIndex(primaryKey, index);


        for (FieldAccess f : fields.values()) {
            if (f.getName().equals(primaryKey)) {
                continue;
            }
            if (Types.isBasicType(f.getType())) {
                configIndexes((SearchableCollection) query, f.getName(), fields);
            }
        }

        query.init();

        query.setFilter(new FilterWithSimpleCache(filter));

        ((SearchableCollection) query).addAll(set);
        return query;
    }


    public static <T> List<T> query(final List<T> list, Expression... expressions) {
        if (list instanceof QList) {
            QList qlist = (QList) list;
            return qlist.searchCollection().query(expressions);
        }
        return null;
    }

    public static <T> List<T> sortedQuery(final List<T> list, String sortBy, Expression... expressions) {
        if (list instanceof QList) {
            QList qlist = (QList) list;
            return qlist.searchCollection().sortedQuery(sortBy, expressions);
        }
        return null;
    }


    public static <T> List<T> query(final Set<T> set, Expression... expressions) {
        if (set instanceof QSet) {
            QSet qset = (QSet) set;
            return qset.searchCollection().query(expressions);
        }
        return null;
    }

    public static <T> List<T> sortedQuery(final Set<T> set, String sortBy, Expression... expressions) {
        if (set instanceof QSet) {
            QSet qset = (QSet) set;
            return qset.searchCollection().sortedQuery(sortBy, expressions);
        }
        return null;
    }


    private static String findPrimaryKey(Map<String, FieldAccess> fields) {
        return "id";
    }


    private static Function createKeyGetter(final FieldAccess field) {
        Utils.notNull(field);
        return new Function() {
            @Override
            public Object apply(Object o) {

                if (Reflection.hasField(o.getClass(), field.getName())) {
                    return field.getValue(o);
                } else {
                    return null;
                }
            }
        };
    }


    static class QSet<T> extends AbstractSet<T> implements CollectionDecorator {
        final Set<T> set;
        final SearchableCollection searchCollection;

        QSet(Set<T> set, SearchableCollection searchCollection) {
            this.set = set;
            this.searchCollection = searchCollection;
        }

        @Override
        public boolean add(T item) {
            searchCollection.add(item);
            return set.add(item);
        }

        @Override
        public boolean remove(Object item) {
            searchCollection.delete((T) item);
            return set.remove(item);
        }


        @Override
        public Iterator<T> iterator() {
            return set.iterator();
        }

        @Override
        public int size() {
            return set.size();
        }

        @Override
        public SearchableCollection searchCollection() {
            return searchCollection;
        }

        @Override
        public Collection collection() {
            return set;
        }
    }

    static class QList<T> extends AbstractList<T> implements CollectionDecorator {
        List<T> list;
        SearchableCollection query;

        QList(List<T> list, SearchableCollection query) {
            this.list = list;
            this.query = query;
        }

        @Override
        public boolean add(T item) {
            query.add(item);
            return list.add(item);
        }

        @Override
        public boolean remove(Object item) {
            query.delete((T) item);
            return list.remove(item);
        }


        @Override
        public T get(int index) {
            return list.get(index);
        }


        @Override
        public int size() {
            return list.size();
        }


        @Override
        public SearchableCollection searchCollection() {
            return query;
        }

        @Override
        public Collection collection() {
            return this.list;
        }
    }


    private static void configIndexes(SearchableCollection query, String prop,
                                      Map<String, FieldAccess> fields) {

        SearchIndex searchIndex = SPIFactory.getSearchIndexFactory().apply(fields.get(prop).getType());
        searchIndex.init();
        Function kg = createKeyGetter(fields.get(prop));
        searchIndex.setKeyGetter(kg);
        query.addSearchIndex(prop, searchIndex);

        LookupIndex index = SPIFactory.getLookupIndexFactory().apply(fields.get(prop).getType());
        index.setKeyGetter(kg);
        query.addLookupIndex(prop, index);

    }


}