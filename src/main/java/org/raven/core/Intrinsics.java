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

/**
 * Intrinsic functions that may be called by the compiler
 */
public class Intrinsics {

    /**
     * Requires that an object be a specific type
     *
     * @param obj     The object
     * @param type    The expected type
     * @param message Error message to be displayed if the types do not match
     */
    public static void requireType(TObject obj, TType type, String message) {
        if (!obj.getType().equals(type)) {
            throw new RuntimeException(message);
        }
    }

    /**
     * Requires that an object is not null. Throws NPE if null
     *
     * @param object The object
     */
    public static void requireNonNull(TObject object) {
        if (object == null || object == TNull.NULL) {
            throw new NullPointerException();
        }
    }

    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type, Class<?> clazz, int paramCount) throws NoSuchMethodException, IllegalAccessException {
        LinkedList<JMethod> list = new LinkedList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == paramCount &&
                    Modifier.isStatic(method.getModifiers())) {
                MethodHandle mh = caller.unreflect(method);
                JMethod jm = new JMethod(mh, method.getParameterTypes(), false);
                list.add(jm);
            }
        }

        for (Class<?> klazz : clazz.getDeclaredClasses()) {
            if (klazz.getSimpleName().equals(name)) {
                for (Constructor<?> constructor : klazz.getDeclaredConstructors()) {
                    if (constructor.getParameterCount() == paramCount) {
                        MethodHandle mh = caller.unreflectConstructor(constructor);
                        JMethod jm = new JMethod(mh, constructor.getParameterTypes(), false);
                        list.add(jm);
                    }
                }
            }
        }

        MethodHandle mh = caller.findStatic(Intrinsics.class, "invoke", MethodType.methodType(TObject.class, LinkedList.class, String.class, TList.class));
        return new ConstantCallSite(mh.bindTo(list).bindTo(name));
    }

    public static TObject invoke(LinkedList<JMethod> jm, String name, TList args) throws Throwable {
        if (jm == null || jm.size() == 0) {
            throw new NoSuchMethodException(name);
        }
        int bestRating = -1;
        JMethod bestMh = null;
        for (JMethod method : jm) {
            int rating = TObject.rate(args, method.types);
            if (rating > bestRating) {
                bestRating = rating;
                bestMh = method;
            }
            // priority based method ordering?
        }
        if (bestRating > TObject.COERCE_IMPOSSIBLE) {
            bestMh.incCount();
            return TObject.wrap(bestMh.methodHandle.invokeWithArguments(TObject.getParams(args, bestMh.types)));
        } else {
            throw new RuntimeException("Type coercion impossible");
        }
    }

    private static class JMethod {
        private MethodHandle methodHandle;
        private Class<?>[] types;
        private int callCount = 0;
        private boolean virtual;

        JMethod(MethodHandle methodHandle, Class<?>[] types, boolean virtual) {
            this.methodHandle = methodHandle;
            this.types = types;
            this.virtual = virtual;
        }

        void incCount() {
            callCount++;
        }
    }
}
