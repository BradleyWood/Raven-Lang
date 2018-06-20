package org.raven.core;

import org.raven.core.wrappers.TList;
import org.raven.core.wrappers.TObject;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Function;

public class Adaptor {

    public static <T, R> Function<T, R> getFunction(final Class<?> clazz, final String name, final Class<R> returnType,
                                                    final int argCount) throws Throwable {
        final CallSite callSite = Intrinsics.bootstrap(MethodHandles.publicLookup(), name,
                MethodType.methodType(TObject.class, TObject.class), clazz,
                argCount);

        final MethodHandle invoker = callSite.dynamicInvoker();

        return (objects) -> {
            try {
                final TObject wrapped = Intrinsics.wrap(objects);
                final TObject lst = wrapped instanceof TList ? wrapped : new TList().add(wrapped);
                return (R) ((TObject) invoker.invoke(lst)).coerce(returnType);
            } catch (final Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }
}
