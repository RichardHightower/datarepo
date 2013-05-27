package org.datarepo;

import org.datarepo.query.Expression;
import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Reflection;
import org.datarepo.reflection.Types;
import org.datarepo.spi.SPIFactory;
import org.datarepo.spi.SearchableCollectionComposer;
import org.datarepo.utils.Utils;

import java.util.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;


public class Collections {

    public static <T> List<T>  $q(final List<T> list) {
        return listQuery(list, true, true);
    }

    public static <T> List<T>  listQuery(final List<T> list) {
        return listQuery(list, true, true);
    }

    public static <T> List<T>  listQuery(final List<T> list, boolean useField, boolean useUnSafe) {
        if (list == null || list.size()==0) {
            return list;
        }

        Class<?> clazz = list.get(0).getClass();

        SearchableCollectionComposer query = SPIFactory.getSearchableCollectionFactory().get();
        Map<String, FieldAccess> fields = getStringFieldAccessMap(useField, useUnSafe, clazz);
        String primaryKey = findPrimaryKey(fields);
        FieldAccess field = fields.get(primaryKey);
        Function keyGetter = createKeyGetter(field);

        query.setFields(fields);
        query.setPrimaryKeyGetter(keyGetter);
        query.setPrimaryKeyName(primaryKey);
        query.setFilter(SPIFactory.getFilterFactory().get());

        for (FieldAccess f : fields.values()) {
           if (f.getName().equals(primaryKey)) {
               continue;
           }
           if (Types.isBasicType(f.getType())) {
               configIndexes((SearchableCollection)query, f.getName(), fields);
           }
        }

        query.init();

        ((SearchableCollection)query).addAll(list);

        return new QList<T>(list, (SearchableCollection)query);
    }




    public static <T> List<T> query(final List<T> list, Expression... expressions) {
        if (list instanceof QList) {
            QList qlist = (QList)list;
            return qlist.searchCollection().query(expressions);
        }
        return null;
    }

    public static <T> List<T> sortedQuery(final List<T> list, String sortBy, Expression... expressions) {
        if (list instanceof QList) {
            QList qlist = (QList)list;
            return qlist.searchCollection().sortedQuery(sortBy, expressions);
        }
        return null;
    }


    private static String findPrimaryKey(Map<String, FieldAccess> fields) {
        return "id";
    }

    private static Map<String, FieldAccess> getStringFieldAccessMap(boolean useField, boolean useUnSafe, Class<?> clazz) {
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
        return fields;
    }

    private static Function createKeyGetter(final FieldAccess field) {
        Utils.notNull(field);
        return new Function() {
            @Override
            public Object apply(Object o) {
                return field.getValue(o);
            }
        };
    }



    static class QList <T> extends AbstractList<T> implements CollectionDecorator {
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
        public T get(int index) {
            return list.get(index);
        }

        @Override
        public Spliterator<T> spliterator() {
            return list.spliterator();
        }

        @Override
        public Stream<T> stream() {
            return list.stream();
        }

        @Override
        public Stream<T> parallelStream() {
            return list.parallelStream();
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public boolean removeIf(Predicate<? super T> filter) {
            return list.removeIf(filter);
        }

        @Override
        public void replaceAll(UnaryOperator<T> operator) {
            list.replaceAll(operator);
        }

        @Override
        public void sort(Comparator<? super T> c) {
            list.sort(c);
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            list.forEach(action);
        }

        @Override
        public SearchableCollection searchCollection() {
            return query;
        }
    }


    private static void configIndexes(SearchableCollection query, String prop,
                                      Map<String, FieldAccess> fields) {

        SearchIndex searchIndex = SPIFactory.getSearchIndexFactory().get();
        Function kg = createKeyGetter(fields.get(prop));
        searchIndex.setKeyGetter(kg);
        query.addSearchIndex(prop, searchIndex);

        LookupIndex index = SPIFactory.getLookupIndexFactory().get();
        index.setKeyGetter(kg);
        query.addLookupIndex(prop, index);

    }


}