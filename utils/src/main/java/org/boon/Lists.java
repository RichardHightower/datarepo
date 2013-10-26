package org.boon;


import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Lists {



    public static <V> List<V> list(Class<V> clazz) {
        return new ArrayList<>();
    }

    public static <V> List<V> list(Iterable<V> iterable) {
        List<V> list = new ArrayList<>();
        for (V o : iterable) {
            list.add(o);
        }
        return list;
    }


    public static List<?> toList(Object item) {
        if ( item == null )  {
            return new ArrayList<>();
        } else if ( item.getClass().isArray() ) {
            final int length = Array.getLength(item);
            List <Object> list = new ArrayList<>();
            for (int index = 0; index < length; index++) {
                list.add( Array.get(item, index) );
            }
            return list;
        } else if ( item instanceof Collection ) {
            return list( (Collection) item);
        } else if ( item instanceof  Iterator ) {
            return list( (Iterator) item );
        } else if ( item instanceof  Enumeration ) {
            return list( (Enumeration) item );
        } else if ( item instanceof  Iterable ) {
            return list( (Iterable) item );
        }  else {
            List <Object> list = new ArrayList<>();
            list.add(item);
            return list;
        }
    }

    public static <V> List<V> list(Collection<V> collection) {
        return new ArrayList<>(collection);
    }

    public static <V> List<V> list(Enumeration<V> enumeration) {
        List<V> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }


    public static <V> Enumeration<V> enumeration(final List<V> list) {
        final Iterator<V> iter = list.iterator();
        return new Enumeration<V>() {
            @Override
            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            @Override
            public V nextElement() {
                return iter.next();
            }
        };

    }



    public static <V> List<V> list(Iterator<V> iterator) {
        List<V> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    @SafeVarargs
    public static <V> List<V> list(final V... array) {
        if (array==null) {
            return new ArrayList<>();
        }
        List<V> list = new ArrayList<>(array.length);
        Collections.addAll(list, array);
        return list;
    }

    @SafeVarargs
    public static <V> List<V> safeList(final V... array) {
        return new CopyOnWriteArrayList<>(array);
    }

    @SafeVarargs
    public static <V> List<V> linkedList(final V... array) {
        if (array==null) {
            return new ArrayList<>();
        }
        List<V> list = new LinkedList<>();
        Collections.addAll(list, array);
        return list;
    }


    public static <V> List<V> safeList(Collection<V> collection) {
        return new CopyOnWriteArrayList<>(collection);
    }

    public static <V> List<V> linkedList(Collection<V> collection) {
        return  new LinkedList<>(collection);
    }

    /** Universal methods */
    @Universal
    public static int len(List<?> list) {
        return list.size();
    }

    @Universal
    public static <V> boolean in(V value, List<?> list) {
        return list.contains(value);
    }

    @Universal
    public static <V> void add(List<V> list, V value) {
        list.add(value);
    }

    @Universal
    public static <T> T idx(List<T> list, final int index) {
        int i = calculateIndex(list, index);
        if ( i > list.size()-1 ) {
            i = list.size()-1;
        }
        return list.get(i);

    }

    @Universal
    public static <V> void idx(List<V> list, int index, V v) {
        int i = calculateIndex(list, index);
        list.set(i, v);
    }

    @Universal
    public static <V> List<V> slc(List<V> list, int startIndex, int endIndex) {
        int start = calculateIndex(list, startIndex);
        int end = calculateIndex(list, endIndex);
        return list.subList(start, end);
    }

    @Universal
    public static <V> List<V> slc(List<V> list, int startIndex) {
        return slc(list, startIndex, list.size());
    }


    @Universal
    public static <V> List<V> slcEnd(List<V> list, int endIndex) {
        return slc(list, 0, endIndex);
    }


    @Universal
    public static <V> List<V> copy(List<V> list) {
        if (list instanceof LinkedList) {
            return new LinkedList<>(list);
        }  else if (list instanceof CopyOnWriteArrayList) {
            return new CopyOnWriteArrayList<>(list);
        } else {
            return new ArrayList<>(list);
        }
    }

    @Universal
    public static <V> List<V> copy(CopyOnWriteArrayList<V> list) {
        Objects.requireNonNull(list, "list cannot be null");
        return new CopyOnWriteArrayList<>(list);
    }

    @Universal
    public static <V> List<V> copy(ArrayList<V> list) {
        Objects.requireNonNull(list, "list cannot be null");
        return new ArrayList<>(list);
    }

    @Universal
    public static <V> List<V> copy(LinkedList<V> list) {
        Objects.requireNonNull(list, "list cannot be null");
        return new LinkedList<>(list);
    }


    @Universal
    public static <V> void insert(List<V> list, int index, V v) {
        int i = calculateIndex(list, index);
        list.add(i, v);
    }


    /* End universal methods. */
    private static <T> int calculateIndex(List<T> list, int originalIndex) {
        final int length = list.size();

        Objects.requireNonNull(list, "list cannot be null");


        int index = originalIndex;

        /* Adjust for reading from the right as in
        -1 reads the 4th element if the length is 5
         */
        if (index < 0) {
            index = (length + index);
        }


        /* Bounds check
            if it is still less than 0, then they
            have an negative index that is greater than length
         */
        if (index < 0) {
            index = 0;
        }
        if (index > length) {
            index = length;
        }
        return index;
    }


}
