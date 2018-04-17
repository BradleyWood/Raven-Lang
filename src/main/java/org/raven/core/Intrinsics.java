package org.raven.core;

import org.raven.core.wrappers.*;
import org.raven.util.Settings;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Intrinsic functions that may be called by the compiler
 */
public class Intrinsics {

    private static final List<String> INTRINSIC_METHODS = Arrays.asList(
            "invokeVirtual", "invokeStatic", "requireType", "requireNonNull"
    );

    private static Map<Integer, LinkedList<JMethod>> virtualMethodCache = Collections.synchronizedMap(new HashMap<>());
    private static Map<Integer, LinkedList<JMethod>> getterCache = Collections.synchronizedMap(new HashMap<>());
    private static Map<Integer, LinkedList<JSetter>> setterCache = Collections.synchronizedMap(new HashMap<>());
    private static Map<Integer, LinkedList<JMethod>> constructorCache = Collections.synchronizedMap(new HashMap<>());
    private static Map<Integer, LinkedList<JMethod>> specialCache = Collections.synchronizedMap(new HashMap<>());

    /**
     * Requires that an object be a specific type
     *
     * @param obj     The object
     * @param type    The expected type
     * @param message Error message to be displayed if the types do not match
     */
    public static void requireType(final TObject obj, final TType type, final String message) {
        if (!obj.getType().equals(type)) {
            throw new RuntimeException(message);
        }
    }

    /**
     * Requires that an object is not null. Throws NPE if null
     *
     * @param object The object
     */
    public static void requireNonNull(final Object object) {
        if (object == null || object == TNull.NULL) {
            throw new NullPointerException();
        }
    }

    public static CallSite bootstrapConstructor(final MethodHandles.Lookup caller, final String name, final MethodType type, final Class<?> clazz, final int argCount) throws Throwable {

        int hash = Objects.hash(clazz, argCount);

        List<JMethod> constructorList;
        if (constructorCache.containsKey(hash)) {
            constructorList = constructorCache.get(hash);
        } else {
            Constructor<?>[] constructors = clazz.getConstructors();
            if (constructors.length == 0) {
                throw new NoSuchMethodException("Class " + clazz.getName() + " has no public constructors");
            }
            constructorList = new LinkedList<>();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == argCount) {
                    MethodHandle ch = caller.unreflectConstructor(constructor);
                    JMethod jMethod = new JMethod(ch, clazz, constructor.getParameterTypes());
                    constructorList.add(jMethod);
                }
            }
        }

        if (constructorList.isEmpty()) {
            throw new NoSuchMethodException("Wrong number of arguments: " + argCount);
        }

        MethodHandle mh;
        if (constructorList.size() == 1) {
            mh = caller.findStatic(Intrinsics.class, "newInstance", MethodType.methodType(TObject.class, JMethod.class, TList.class));
            mh = mh.bindTo(constructorList.get(0));
        } else {
            mh = caller.findStatic(Intrinsics.class, "invokeStatic", MethodType.methodType(TObject.class, LinkedList.class, String.class, TList.class));
            mh = mh.bindTo(constructorList).bindTo(name);
        }

        return new ConstantCallSite(mh);
    }

    public static CallSite bootstrapSpecial(final MethodHandles.Lookup caller, final String name, final MethodType type, final Class<?> superClass, final int argCount) throws Throwable {
        int hash = Objects.hash(name, superClass);

        MethodHandle invokeSpecial = caller.findStatic(Intrinsics.class, "invokeSpecial",
                MethodType.methodType(TObject.class, LinkedList.class, Object.class, TList.class));

        if (specialCache.containsKey(hash)) {
            LinkedList<JMethod> cache = specialCache.get(hash);
            if (cache.size() == 1) {
                return new ConstantCallSite(cache.get(0).methodHandle);
            }
            if (cache.size() > 1) {
                return new ConstantCallSite(invokeSpecial.bindTo(cache));
            } else {
                throw new NoSuchMethodException(name);
            }
        }
        LinkedList<JMethod> methods = new LinkedList<>();
        virtualMethodCache.put(hash, methods);

        MethodHandle mh = caller.findSpecial(superClass, name, type, caller.lookupClass());
        if (mh != null) {
            for (Class<?> clazz : type.parameterArray()) {
                if (!TObject.class.isAssignableFrom(clazz)) {
                    JMethod jm = new JMethod(mh, superClass, type.parameterArray());
                    methods.add(jm);
                    MethodHandle iv = caller.findStatic(Intrinsics.class, "invokeVirtual",
                            MethodType.methodType(TObject.class, JMethod.class, Object.class, TList.class));
                    return new ConstantCallSite(iv.bindTo(jm));
                }
            }
            return new ConstantCallSite(mh);
        }

        if (name.equals("<init>")) {
            List<JMethod> superConstructors = getConstructors(caller, superClass, argCount);
            if (superConstructors.isEmpty()) {
                throw new NoSuchMethodException("Class " + superClass.getName() + " has no public constructors");
            }
            methods.addAll(superConstructors);
            if (methods.size() == 1) {
                return new ConstantCallSite(methods.get(0).methodHandle);
            }
            if (methods.size() > 1) {
                return new ConstantCallSite(invokeSpecial.bindTo(methods));
            }
        }
        throw new NoSuchMethodException(name);
    }

    private static List<JMethod> getConstructors(final MethodHandles.Lookup caller, final Class<?> clazz, final int paramCount) throws IllegalAccessException {
        List<JMethod> methods = new LinkedList<>();
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == paramCount) {
                MethodHandle ch = caller.unreflectConstructor(constructor);
                JMethod jMethod = new JMethod(ch, clazz, constructor.getParameterTypes());
                methods.add(jMethod);
            }
        }
        return methods;
    }

    public static TObject invokeSpecial(final LinkedList<JMethod> methods, final Object instance, final TList arguments) throws Throwable {
        requireNonNull(instance);

        JMethod method = select(methods, arguments.size(), arguments);
        if (method != null) {
            return invokeVirtual(method, instance, arguments);
        }
        throw new RuntimeException("Type coercion impossible");
    }

    public static TObject newInstance(final JMethod constructor, final TList arguments) throws Throwable {
        return wrap(constructor.methodHandle.invokeWithArguments(getParams(arguments, constructor.types)));
    }

    public static CallSite bootstrapSetter(final MethodHandles.Lookup caller, final String name, final MethodType type) throws Throwable {
        int hash = Objects.hash(name);
        LinkedList<JSetter> virtualMethods = setterCache.getOrDefault(hash, new LinkedList<>());

        if (!setterCache.containsKey(hash)) {
            setterCache.put(hash, virtualMethods);
        }

        MethodHandle mh = caller.findStatic(Intrinsics.class, "setField",
                MethodType.methodType(TObject.class, MethodHandles.Lookup.class, LinkedList.class, String.class, TObject.class, TObject.class));
        mh = mh.bindTo(caller).bindTo(virtualMethods).bindTo(name.substring(3));

        return new ConstantCallSite(mh);
    }

    public static TObject setField(final MethodHandles.Lookup caller, final LinkedList<JSetter> jmethods, final String name, final TObject object, final TObject value) throws Throwable {
        Object obj = object.getObject();
        if (object == TNull.NULL || obj == null) {
            throw new NullPointerException();
        }
        Class<?> clazz = obj.getClass();

        List<JSetter> methods = jmethods.stream().filter(jm -> jm.owner.isAssignableFrom(clazz)).collect(Collectors.toList());

        if (methods.isEmpty()) {
            for (Field field : clazz.getFields()) {
                if (field.getName().equals(name) && !Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    MethodHandle getter = caller.unreflectSetter(field);
                    JSetter jMethod = new JSetter(getter, clazz, field.getType());
                    jmethods.add(jMethod);
                    methods.add(jMethod);
                    break;
                }
            }
        }
        if (methods.isEmpty()) {
            throw new NoSuchFieldException(name);
        }
        JSetter setter = methods.get(0);
        setter.incCount();
        setter.methodHandle.bindTo(obj).invoke(value.coerce(setter.targetType));
        return TNull.NULL;
    }

    public static CallSite bootstrapGetter(final MethodHandles.Lookup caller, final String name, final MethodType type) throws Throwable {
        int hash = Objects.hash(name);
        LinkedList<JMethod> virtualMethods = getterCache.getOrDefault(hash, new LinkedList<>());

        if (!getterCache.containsKey(hash)) {
            getterCache.put(hash, virtualMethods);
        }

        MethodHandle mh = caller.findStatic(Intrinsics.class, "getField",
                MethodType.methodType(TObject.class, MethodHandles.Lookup.class, LinkedList.class, String.class, TObject.class));
        mh = mh.bindTo(caller).bindTo(virtualMethods).bindTo(name.substring(3));
        return new ConstantCallSite(mh);
    }

    public static TObject getField(final MethodHandles.Lookup caller, final LinkedList<JMethod> jmethods, final String name, final TObject object) throws Throwable {
        Object obj = object.getObject();
        if (object == TNull.NULL || obj == null) {
            throw new NullPointerException();
        }
        Class<?> clazz = obj.getClass();

        List<JMethod> methods = jmethods.stream().filter(jm -> jm.owner.isAssignableFrom(clazz)).collect(Collectors.toList());

        if (methods.isEmpty()) {
            for (Field field : clazz.getFields()) {
                if (field.getName().equals(name) && !Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    MethodHandle getter = caller.unreflectGetter(field);
                    JMethod jMethod = new JMethod(getter, clazz);
                    jmethods.addFirst(jMethod);
                    methods.add(jMethod);
                    break;
                }
            }
        }
        if (methods.isEmpty()) {
            throw new NoSuchFieldException(name);
        }
        return wrap(methods.get(0).methodHandle.bindTo(obj).invoke());
    }

    public static CallSite bootstrapVirtual(final MethodHandles.Lookup caller, final String name, final MethodType type, final int paramCount) throws Throwable {
        int hash = Objects.hash(name, paramCount);
        LinkedList<JMethod> virtualMethods = virtualMethodCache.getOrDefault(hash, new LinkedList<>());

        if (!virtualMethodCache.containsKey(hash)) {
            virtualMethodCache.put(hash, virtualMethods);
        }

        MethodHandle mh = caller.findStatic(Intrinsics.class, "invokeVirtual",
                MethodType.methodType(TObject.class, MethodHandles.Lookup.class, LinkedList.class, String.class, Integer.class, TObject.class, TList.class));
        mh = mh.bindTo(caller).bindTo(virtualMethods).bindTo(name).bindTo(paramCount);

        return new ConstantCallSite(mh);
    }

    public static TObject invokeVirtual(final MethodHandles.Lookup caller, final LinkedList<JMethod> jmethods, final String name, final Integer paramCount, final TObject instance, final TList args) throws Throwable {
        Object v = instance.getObject();

        requireNonNull(instance);

        if (instance.getObject() == null) {
            v = instance;
        }

        Class<?> clazz = v.getClass();
        List<JMethod> methods = jmethods.stream().filter(jm -> jm.owner.isAssignableFrom(clazz)).collect(Collectors.toList());
        if (methods.isEmpty()) {
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(name) && method.getParameterCount() == paramCount
                        && !Modifier.isStatic(method.getModifiers())) {
                    method.setAccessible(true);
                    MethodHandle mh = caller.unreflect(method);
                    JMethod jm = new JMethod(mh, clazz, method.getParameterTypes());
                    jmethods.add(jm);
                    methods.add(jm);
                }
            }
        }
        if (methods.isEmpty()) {
            throw new NoSuchMethodException(name);
        }

        JMethod method = select(methods, args.size(), args);
        if (method != null) {
            method.incCount();
            return invokeVirtual(method, v, args);
        } else {
            throw new RuntimeException("Type coercion impossible");
        }
    }

    public static TObject invokeVirtual(final JMethod method, final Object instance, final TList args) throws Throwable {
        return wrap(method.methodHandle.bindTo(instance).invokeWithArguments(getParams(args, method.types, 999)));
    }

    public static CallSite bootstrap(final MethodHandles.Lookup caller, final String name, final MethodType type, final Class<?> clazz, final int paramCount) throws Throwable {
        LinkedList<JMethod> list = new LinkedList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == paramCount &&
                    Modifier.isStatic(method.getModifiers())) {
                method.setAccessible(true);
                MethodHandle mh = caller.unreflect(method);
                JMethod jm = new JMethod(mh, clazz, method.getParameterTypes());
                list.add(jm);
            }
        }

        for (Class<?> klazz : clazz.getClasses()) {
            if (klazz.getSimpleName().equals(name)) {
                for (Constructor<?> constructor : klazz.getDeclaredConstructors()) {
                    if (constructor.getParameterCount() == paramCount) {
                        MethodHandle mh = caller.unreflectConstructor(constructor);
                        JMethod jm = new JMethod(mh, clazz, constructor.getParameterTypes());
                        list.add(jm);
                    }
                }
            }
        }

        MethodHandle mh = caller.findStatic(Intrinsics.class, "invokeStatic", MethodType.methodType(TObject.class, LinkedList.class, String.class, TList.class));
        return new ConstantCallSite(mh.bindTo(list).bindTo(name));
    }

    public static TObject invokeStatic(final LinkedList<JMethod> jm, final String name, final TList args) throws Throwable {
        if (jm == null || jm.size() == 0) {
            throw new NoSuchMethodException(name);
        }
        JMethod method = select(jm, args.size(), args);
        if (method != null) {
            method.incCount();
            return wrap(method.methodHandle.invokeWithArguments(getParams(args, method.types)));
        } else {
            throw new RuntimeException("Type coercion impossible");
        }
    }

    private static JMethod select(final List<JMethod> methods, final int argCount, final TList args) {
        int bestRating = -1;
        JMethod bestMh = null;
        if (argCount > 0) {
            for (JMethod method : methods) {
                int rating = rate(args, method.types);
                if (rating > bestRating) {
                    bestRating = rating;
                    bestMh = method;
                }
                // priority based method ordering?
            }
        } else {
            bestMh = methods.get(0);
            bestRating = Integer.MAX_VALUE;
        }

        if (bestRating <= TObject.COERCE_IMPOSSIBLE) {
            return null;
        }

        return bestMh;
    }

    @Hidden
    public static TObject wrap(final Object o) {
        if (o instanceof TObject)
            return (TObject) o;
        if (o instanceof BigInteger) {
            return new TBigInt((BigInteger) o);
        }
        if (o instanceof Integer || o instanceof Long || o instanceof Short || o instanceof Byte) {
            return new TInt(((Number) o).intValue());
        }
        if (o instanceof Float || o instanceof Double) {
            return new TReal(((Number) o).doubleValue());
        }
        if (o instanceof Boolean) {
            return ((boolean) o) ? TBoolean.TRUE : TBoolean.FALSE;
        }
        if (o instanceof String) {
            return new TString((String) o);
        }
        if (o == null) {
            return TNull.NULL;
        }
        if (o instanceof List) {
            return new TList((List) o);
        }
        if (o.getClass().isArray()) {
            TList lst = new TList();
            int length = Array.getLength(o);
            for (int i = 0; i < length; i++) {
                lst.add(Array.get(o, i));
            }
            return lst;
        }
        return new TObject(o);
    }

    @Hidden
    public static int rate(final TObject params, final Class<?>[] types) {
        if (!(params instanceof TList) || params.size() != types.length)
            throw new IllegalArgumentException();
        List<TObject> lst = ((TList) params).getList();

        int rating = 0;

        for (int i = 0; i < lst.size(); i++) {
            int r = lst.get(i).coerceRating(types[i]);
            if (r == TObject.COERCE_IMPOSSIBLE)
                return TObject.COERCE_IMPOSSIBLE;
            rating += r;
        }

        return rating;
    }


    public static Object[] getParams(final TObject params, final Class<?>[] types, final int rating) {
        if (rating == -1) {
            throw new IllegalArgumentException("Cannot coerce arguments: " + rating);
        }
        if (!(params instanceof TList) || params.size() != types.length)
            throw new IllegalArgumentException();

        List<TObject> lst = ((TList) params).getList();
        Object[] ret = new Object[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            ret[i] = lst.get(i).coerce(types[i]);
        }
        return ret;
    }

    public static Object[] getParams(final TObject params, final Class<?>[] types) {
        int rating = rate(params, types);
        if (rating == TObject.COERCE_IMPOSSIBLE)
            return null;
        return getParams(params, types, rating);
    }

    public static void useSanitizedExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
    }

    public static <T extends Throwable> T sanitizeStackTrace(final T throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        ArrayList<StackTraceElement> list = new ArrayList<>(stackTrace.length);
        boolean skip = true;
        for (StackTraceElement element : stackTrace) {
            if (!skip) {
                list.add(element);
            } else if (element.getClassName().startsWith("org.raven.core")) {
                list.clear();
                skip = false;
            }
            if (Settings.getBoolean("REPL") && element.getMethodName().equals("exec")
                    || element.getMethodName().equals("main")) {
                break;
            }
        }
        throwable.setStackTrace(list.toArray(new StackTraceElement[list.size()]));
        return throwable;
    }

    private static class JSetter {
        private MethodHandle methodHandle;
        private Class<?> owner;
        private Class<?> targetType;
        int callCount;

        public JSetter(final MethodHandle methodHandle, final Class<?> owner, final Class<?> targetType) {
            this.methodHandle = methodHandle;
            this.owner = owner;
            this.targetType = targetType;
        }

        void incCount() {
            callCount++;
        }
    }

    private static class JMethod {
        private MethodHandle methodHandle;
        private Class<?>[] types;
        private Class<?> owner;
        private int callCount = 0;

        JMethod(final MethodHandle methodHandle, final Class<?> owner, final Class<?>... types) {
            this.methodHandle = methodHandle;
            this.owner = owner;
            this.types = types;
        }

        void incCount() {
            callCount++;
        }
    }
}
