package org.toylang.test;

import org.toylang.test.Assert;

import java.lang.Thread;
import java.lang.Runnable;

public class SomeRunnableClass implements Runnable {
    private var someValue = 0;


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
    Assert.assertEqual(0, runnable.getValue());
    thread.start();
    thread.join();
    Assert.assertEqual(100, runnable.getValue());
}
