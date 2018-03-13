package org.raven.core;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e = Intrinsics.sanitizeStackTrace(e);
        e.printStackTrace();
        // todo
    }
}
