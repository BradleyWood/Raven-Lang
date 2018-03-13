package org.raven.core;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e = Intrinsics.sanitizeStackTrace(e);
        System.err.println("Exception in thread: " + t.getName() + " (most recent call last)");
        System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            System.err.println("\tat " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName()
                    + "(" + stackTraceElement.getFileName() + " on line " + stackTraceElement.getLineNumber() + ")");
        }
    }
}
