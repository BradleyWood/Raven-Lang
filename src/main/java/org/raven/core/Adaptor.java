package org.raven.core;

import org.raven.core.wrappers.TList;
import org.raven.core.wrappers.TObject;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.function.Function;

public class Adaptor {

    public static <T, R> Function<T, R> getFunction(final Method method, final Class<R> returnType)
            throws Throwable {

        final CallSite callSite = Intrinsics.bootstrap(MethodHandles.publicLookup(), method.getName(),
                MethodType.methodType(TObject.class, TObject.class), method.getDeclaringClass(),
                method.getParameterCount());

        return (objects) -> {
            try {
                final TObject wrapped = Intrinsics.wrap(objects);
                final TObject lst = wrapped instanceof TList ? wrapped : new TList().add(wrapped);
                return (R) ((TObject) callSite.dynamicInvoker().invoke(lst)).coerce(returnType);
            } catch (final Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }
}
