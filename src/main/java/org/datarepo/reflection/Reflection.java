package org.datarepo.reflection;

import org.datarepo.utils.Utils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static org.datarepo.utils.Utils.*;
import static org.datarepo.utils.Utils.slc;

public class Reflection {

    static Class<Reflection> reflection = Reflection.class;

    private static final Logger log = log(reflection);


    @SuppressWarnings("serial")
    public static class ReflectionException extends RuntimeException {

        public ReflectionException() {
            super();
        }

        public ReflectionException(String message, Throwable cause) {
            super(message, cause);
        }

        public ReflectionException(String message) {
            super(message);
        }

        public ReflectionException(Throwable cause) {
            super(cause);
        }
    }

    public static boolean isArray(Object obj) {
        return obj.getClass().isArray();
    }

    public static boolean isStaticField(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    @SuppressWarnings("unchecked")
    public static <V> V[] array(List<V> list) {
        if (list.size() > 0) {
            Object newInstance = Array.newInstance(list.get(0).getClass(),
                    list.size());
            return (V[]) list.toArray((V[]) newInstance);
        } else {
            complain("array(list): The list has to have at least one item in it");
            return null;
        }
    }


    public static int arrayLength(Object obj) {
        return Array.getLength(obj);
    }


    private static void handle(Exception ex) {
        throw new ReflectionException(ex);
    }

    private static Class<?> clazz(Object that) {
        if (that instanceof Class) {
            return (Class<?>) that;
        } else {
            return that.getClass();
        }
    }

    public static Object idx(Object object, int index) {
        object = Array.get(object, index);
        return object;
    }

    public static void idx(Object object, int index, Object value) {
        Array.set(object, index, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProperty(Class<T> t, Object object, final String key) {
        return (T) getProp(object, key);
    }

    public static Object getProp(Object object, final String key) {
        if (object == null) {
            return null;
        }
        object.getClass().getDeclaredMethods();
        Class<? extends Object> clz = object.getClass();
        outer: while (clz != Object.class) {
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                if (method.getParameterTypes().length == 0
                        && method.getName().toLowerCase()
                        .endsWith(key.toLowerCase())
                        && (method.getName().startsWith("is")
                        || method.getName().startsWith("get") || method
                        .getName().length() == key.length())) {
                    try {
                        object = method.invoke(object, (Object[]) null);
                        break outer;
                    } catch (Exception ex) {
                        continue;
                    }
                }
            }
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (field.getName().equals(key)) {
                    try {
                        object = field.get(object);
                        break outer;
                    } catch (Exception ex) {
                        break;
                    }
                }
            }

            clz = clz.getSuperclass();
        }
        return object;

    }


    private static boolean _useUnsafe;
    static {
        try {
            Class.forName("sun.misc.Unsafe");
            _useUnsafe = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            _useUnsafe = false;
        }

        _useUnsafe = _useUnsafe && !Utils.sbprop("com.datarepo.noUnsafe");
    }

    private static final boolean useUnsafe = _useUnsafe;

    @SuppressWarnings("unchecked")
    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {

        if (map.get("class")==null) {
            map.put("class", clazz.getName());
        }
        return (T) fromMap(map);
    }

    @SuppressWarnings("unchecked")
    public static Object fromMap(Map<String, Object> map) {
        String className = (String) map.get("class");
        Object newInstance = null;
        Class<?> clazz = null;

        try {
            clazz = Class.forName(className);
            newInstance = clazz.newInstance();
        } catch (Exception ex) {
            info("we were not able to load the class so we are leaving this as a map");
            return map;
        }

        List<FieldAccess> fields = getAllAccessorFields(clazz);

        for (FieldAccess field : fields) {
            String name = field.getName();
            Object value = map.get(name);
            if (value instanceof Map && Types.getKeyType((Map<?, ?>) value) == string) {
                value = fromMap((Map<String, Object>) value);
            } else if (value instanceof Collection) {
                Class<?> componentType = getComponentType((Collection<?>) value);
                if (Types.isMap(componentType)) {
                    handleCollectionOfMaps(newInstance, field,
                            (Collection<Map<?, ?>>) value);
                    continue;
                }
            } else if (value instanceof Map[]) {
                Map<?,?> [] maps = (Map<?,?>[]) value;
                List<Map<?,?>> list = Arrays.asList(maps);
                handleCollectionOfMaps(newInstance, field,
                        list);
                continue;
            }

            if (value != null) {
                field.setValue(newInstance, value);
            }
        }

        return newInstance;
    }

    @SuppressWarnings("unchecked")
    private static void handleCollectionOfMaps(Object newInstance,
                                               FieldAccess field, Collection<Map<?, ?>> value) {

        Class<?> type = field.getType();
        Collection<Object> target = null;
        try {
            if (!type.isInterface()) {
                Constructor<?> constructor = type.getConstructor(pint);
                constructor.setAccessible(true);
                target = (Collection<Object>) constructor.newInstance(value
                        .size());
            } else {
                // the type was an interface so let's see if we can figure out
                // what it should be
                Collection<Object> value2 = (Collection<Object>) field
                        .getValue(newInstance);

                if (value2 != null) {

                    if (Types.isModifiableCollection(value2)) {
                        target = value2;
                    }
                }
                if (target==null) {
                    target = (Collection<Object>) createCollection(type, value.size());
                }
            }

            if (value.size() > 0) {
                Map<?, ?> item = value.iterator().next();
                if (Types.getKeyType(item) == string) {
                    for (Map<?, ?> i : value) {
                        target.add(fromMap((Map<String, Object>) i));
                        field.setValue(newInstance, target);
                        return;
                    }
                } else {
                    warning(log,
                            "This should not happen, but for some reason there is a type and we don't know how to convert it");

                }
            } else {
                field.setValue(newInstance, target);
                return;
            }

        } catch (Exception e) {
            warning(log,
                    e,
                    "This should not happen, but for some reason we were not able to get the constructor");
        }
    }

    public static Collection<Object> createCollection(Class<?> type, int size) {
        if (type==List.class) {
            return new ArrayList<Object>(size);
        } else if (type == SortedSet.class) {
            return new TreeSet<Object>();
        } else if (type == Set.class) {
            return new HashSet<Object>(size);
        } else if (type==Queue.class) {
            return new LinkedList<Object>();
        } else {
            return new ArrayList<Object>(size);
        }
    }

    public static Map<String, Object> toMap(final Object object) {

        if (object==null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();


        class FieldToEntryConverter implements
                Converter<Entry<String, Object>, FieldAccess> {
            @Override
            public Entry<String, Object> convert(FieldAccess from) {
                if (from.isReadOnly()) {
                    return null;
                }
                Entry<String, Object> entry = new EntryImpl<>(from.getName(),
                        from.getValue(object));
                return entry;
            }
        }
        List<FieldAccess> fields = getAllAccessorFields(object.getClass());
        fields = new ArrayList<>(fields);
        Collections.reverse(fields); // make super classes fields first that
        // their values get overriden by
        // subclass fields with the same name

        List<Entry<String, Object>> entries = mapFilterNulls(
                new FieldToEntryConverter(), fields);

        map.put("class", object.getClass().getName());

        for (Entry<String, Object> entry : entries) {
            Object value = entry.value();
            if (value == null) {
                continue;
            }
            if (Types.isBasicType(value)) {
                map.put(entry.key(), entry.value());
            } else if (isArray(value)
                    && Types.isBasicType(value.getClass().getComponentType())) {
                map.put(entry.key(), entry.value());
            } else if (isArray(value)) {
                int length = len(value);
                List<Map<String, Object>> list = new ArrayList<>(length);
                for (int index = 0; index < length; index++) {
                    Object item = idx(value, index);
                    list.add(toMap(item));
                }
                map.put(entry.key(), list);
            } else if (value instanceof Collection) {
                Collection<?> collection = (Collection<?>) value;
                Class<?> componentType = getComponentType(collection);
                if (Types.isBasicType(componentType)) {
                    map.put(entry.key(), value);
                } else {
                    List<Map<String, Object>> list = new ArrayList<>(
                            collection.size());
                    for (Object item : collection) {
                        if (item!=null) {
                            list.add(toMap(item));
                        } else {

                        }
                    }
                    map.put(entry.key(), list);
                }
            } else if (value instanceof Map) {

            } else {
                map.put(entry.key(), toMap(value));
            }
        }
        return map;
    }

    public static Class<?> getComponentType(Collection<?> value) {
        if (value.size() > 0) {
            Object next = value.iterator().next();
            return next.getClass();
        } else {
            return object;
        }
    }

    private static class FieldConverter implements Converter<FieldAccess, Field> {

        boolean thisUseUnsafe;
        FieldConverter(boolean useUnsafe) {
             this.thisUseUnsafe = useUnsafe;
        }
        @Override
        public FieldAccess convert(Field from) {
            if (useUnsafe && thisUseUnsafe) {
                return new UnsafeField(from);
            } else {
                return new ReflectField(from);
            }
        }
    }

    static Map<Class<? extends Object>, List<FieldAccess>> allAccessorFieldsCache = new ConcurrentHashMap<>();

    public static List<FieldAccess> getAllAccessorFields(
            Class<? extends Object> theClass) {
        return getAllAccessorFields(theClass, false);
    }

    public static List<FieldAccess> getAllAccessorFields(
            Class<? extends Object> theClass, boolean useUnsafe) {
        List<FieldAccess> list = allAccessorFieldsCache.get(theClass);
        if (list == null) {
            list = map(new FieldConverter(useUnsafe), getAllFields(theClass));
        } else {
            allAccessorFieldsCache.put(theClass, list);
        }
        return list;
    }

    public static List<Field> getAllFields(Class<? extends Object> theClass) {
        List<Field> list = getFields(theClass);
        while (theClass != object) {
            theClass = theClass.getSuperclass();
            getFields(theClass, list);
        }
        return list;
    }

    public static List<FieldAccess> getPropertyFieldAccessors(
            Class<? extends Object> theClass) {
        Map<String, Pair<Method>> methods = getPropertySetterGetterMethods(theClass);
        List<FieldAccess> fields = new ArrayList<>();
        for (Map.Entry<String, Pair<Method>> entry : methods.entrySet()) {
              fields.add(new PropertyField(entry.getKey(), entry.getValue()));
        }
        return fields;
    }

    public static List<Method> getPropertyGetterMethods(
            Class<? extends Object> theClass) {

        Method[] methods = theClass.getMethods();

        List<Method> methodList = new ArrayList<Method>(methods.length);

        for (int index = 0; index < methods.length; index++) {
            Method method = methods[index];
            String name = method.getName();

            boolean staticFlag = Modifier.isStatic(method.getModifiers());


            if (staticFlag || method.getParameterTypes().length > 0
                    || method.getReturnType() == Void.class
                    || !(name.startsWith("get") || name.startsWith("is"))
                    || name.equals("getClass")) {
                continue;
            }
            methodList.add(method);

        }
        return methodList;
    }


    public static List<Method> getPropertySetterMethods(
            Class<? extends Object> theClass) {

        Method[] methods = theClass.getMethods();

        List<Method> methodList = new ArrayList<>(methods.length);



        for (int index = 0; index < methods.length; index++) {
            Method method = methods[index];
            String name = method.getName();
            boolean staticFlag = Modifier.isStatic(method.getModifiers());


            if (!staticFlag && method.getParameterTypes().length == 1
                    && method.getReturnType() == Void.class
                     && name.startsWith("set")) {
                methodList.add(method);
            }

        }
        return methodList;
    }

    public static Map<String, Pair<Method>> getPropertySetterGetterMethods(
            Class<? extends Object> theClass) {

        Method[] methods = theClass.getMethods();

        Map<String, Pair<Method>> methodMap = new HashMap<>(methods.length);
        List<Method> getterMethodList = new ArrayList<>(methods.length);

        for (int index = 0; index < methods.length; index++) {
            Method method = methods[index];
            String name = method.getName();

            if (method.getParameterTypes().length == 1
                    && method.getReturnType() == void.class
                    && name.startsWith("set")) {
                Pair<Method> pair = new Pair<Method>();
                pair.setFirst(method);
                String propertyName = slc(name, 3);

                propertyName = lower(slc(propertyName, 0, 1)) + slc(propertyName, 1);
                methodMap.put(propertyName, pair);
            }

            if (method.getParameterTypes().length > 0
                    || method.getReturnType() == void.class
                    || !(name.startsWith("get") || name.startsWith("is"))
                    || name.equals("getClass")) {
                continue;
            }
            getterMethodList.add(method);
        }

        for (Method method : getterMethodList) {
            String name = method.getName();
            String propertyName = null;
            if (name.startsWith("is")) {
                propertyName = slc(name, 2);
            } else if (name.startsWith("get"))  {
                propertyName = slc(name, 3);
            }

            propertyName = lower(slc(propertyName, 0, 1)) + slc(propertyName, 1);

            Pair<Method> pair = methodMap.get(propertyName);
            if (pair == null) {
                pair = new Pair<>();
                methodMap.put(propertyName, pair);
            }
            pair.setSecond(method);

        }
        return methodMap;
    }

    public static void getFields(Class<? extends Object> theClass,
                                 List<Field> list) {
        List<Field> more = getFields(theClass);
        list.addAll(more);
    }

    public static List<Field> getFields(Class<? extends Object> theClass) {
        List<Field> list = list(theClass.getDeclaredFields());
        for (Field field : list) {
            field.setAccessible(true);
        }
        return list;
    }

    public static <T> T copy (T item)  {
        if (item instanceof Cloneable) {
            try {
                Method method = item.getClass().getMethod("clone", null);
                return (T) method.invoke(item, null);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                return fieldByFieldCopy(item);
            }
        } else {
            return fieldByFieldCopy(item);
        }
    }

    private static <T> T fieldByFieldCopy(T item) {
        List<FieldAccess> fields = getAllAccessorFields(item.getClass());
        T clone = null;
        try {
            clone = (T)item.getClass().newInstance();
        } catch (Exception e) {
            handle(e);
        }
        for (FieldAccess field : fields) {
               if (field.isStatic() || field.isFinal() || field.isReadOnly()) {
                    continue;
               }
               field.setValue(clone, field.getValue(item));
        }
        return clone;
    }


}
