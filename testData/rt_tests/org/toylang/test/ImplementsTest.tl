package org.toylang.test;

import org.toylang.test.Assert;

import java.lang.Thread;
import java.lang.Runnable;

public class SomeRunnableClass implements Runnable {
    private var someValue;

    @Override
    public fun run() {
        someValue = 100;
    }

    public fun getValue() {
        return someValue;
    }
}

fun testRunnable() {
    var runnable = SomeRunnableClass();
    var thread = Thread(runnable);
    thread.start();
    thread.join();
    Assert.assertEqual(100, runnable.getValue());
}
