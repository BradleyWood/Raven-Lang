package org.raven.core;

import org.raven.core.wrappers.TList;
import org.raven.core.wrappers.TNull;
import org.raven.core.wrappers.TObject;
import org.raven.core.wrappers.TType;

import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

    /**
     * Requires that an object be a specific type
     *
     * @param obj     The object
     * @param type    The expected type
     * @param message Error message to be displayed if the types do not match
     */
    public static void requireType(TObject obj, TType type, String message) {
        if (!obj.getType().equals(type)) {
            throw sanitizeStackTrace(new RuntimeException(message));
        }
    }

    /**
     * Requires that an object is not null. Throws NPE if null
     *
     * @param object The object
     */
    public static void requireNonNull(TObject object) {
        if (object == null || object == TNull.NULL) {
            throw sanitizeStackTrace(new NullPointerException());
        }
    }

    public static CallSite bootstrapVirtual(MethodHandles.Lookup caller, String name, MethodType type, int paramCount) throws NoSuchMethodException, IllegalAccessException {
        int hash = Objects.hash(name, paramCount);
        LinkedList<JMethod> virtualMethods = virtualMethodCache.getOrDefault(hash, new LinkedList<>());

        if (!virtualMethodCache.containsKey(hash)) {
            virtualMethodCache.put(hash, virtualMethods);
        }

        MethodHandle mh = caller.findStatic(Intrinsics.class, "invokeVirtual", MethodType.methodType(TObject.class, MethodHandles.Lookup.class, LinkedList.class, String.class, Integer.class, TObject.class, TList.class));
        mh = mh.bindTo(caller).bindTo(virtualMethods).bindTo(name).bindTo(paramCount);

        return new ConstantCallSite(mh);
    }

    public static TObject invokeVirtual(MethodHandles.Lookup caller, LinkedList<JMethod> jmethods, String name, Integer paramCount, TObject instance, TList args) throws Throwable {
        Object v = instance.getObject();

        if (instance == TNull.NULL) {
            throw sanitizeStackTrace(new NullPointerException());
        }

        if (instance.getObject() == null) {
            v = instance;
        }

        Class<?> clazz = v.getClass();
        List<JMethod> methods = jmethods.stream().filter(jm -> jm.owner.isAssignableFrom(clazz)).collect(Collectors.toList());
        if (methods.isEmpty()) {
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(name) && method.getParameterCount() == paramCount
                        && !Modifier.isStatic(method.getModifiers())) {
                    MethodHandle mh = caller.unreflect(method);
                    JMethod jm = new JMethod(mh, clazz, method.getParameterTypes());
                    jmethods.add(jm);
                    methods.add(jm);
                }
            }
        }
        if (methods.isEmpty()) {
            throw sanitizeStackTrace(new NoSuchMethodException(name));
        }

        JMethod method = select(methods, args.size(), args);
        if (method != null) {
            method.incCount();
            return TObject.wrap(method.methodHandle.bindTo(v).invokeWithArguments(TObject.getParams(args, method.types)));
        } else {
            throw sanitizeStackTrace(new RuntimeException("Type coercion impossible"));
        }
    }

    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type, Class<?> clazz, int paramCount) throws NoSuchMethodException, IllegalAccessException {
        LinkedList<JMethod> list = new LinkedList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == paramCount &&
                    Modifier.isStatic(method.getModifiers())) {
                MethodHandle mh = caller.unreflect(method);
                JMethod jm = new JMethod(mh, clazz, method.getParameterTypes());
                list.add(jm);
            }
        }

        for (Class<?> klazz : clazz.getDeclaredClasses()) {
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

    public static TObject invokeStatic(LinkedList<JMethod> jm, String name, TList args) throws Throwable {
        if (jm == null || jm.size() == 0) {
            throw sanitizeStackTrace(new NoSuchMethodException(name));
        }
        JMethod method = select(jm, args.size(), args);
        if (method != null) {
            method.incCount();
            return TObject.wrap(method.methodHandle.invokeWithArguments(TObject.getParams(args, method.types)));
        } else {
            throw sanitizeStackTrace(new RuntimeException("Type coercion impossible"));
        }
    }

    private static JMethod select(List<JMethod> methods, int argCount, TList args) {
        int bestRating = -1;
        JMethod bestMh = null;
        if (argCount > 0) {
            for (JMethod method : methods) {
                int rating = TObject.rate(args, method.types);
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

    public static <T extends Throwable> T sanitizeStackTrace(T throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        ArrayList<StackTraceElement> list = new ArrayList<>(stackTrace.length);
        boolean skip = true;
        for (StackTraceElement element : stackTrace) {
            if (!skip) {
                list.add(element);
            } else if (element.getClassName().equals("org.raven.core.Intrinsics")) {
                if (INTRINSIC_METHODS.contains(element.getMethodName())) {
                    skip = false;
                }
            }
        }
        throwable.setStackTrace(list.toArray(new StackTraceElement[list.size()]));
        return throwable;
    }

    private static class JMethod {
        private MethodHandle methodHandle;
        private Class<?>[] types;
        private Class<?> owner;
        private int callCount = 0;

        JMethod(MethodHandle methodHandle, Class<?> owner, Class<?>[] types) {
            this.methodHandle = methodHandle;
            this.owner = owner;
            this.types = types;
        }

        void incCount() {
            callCount++;
        }
    }
}
