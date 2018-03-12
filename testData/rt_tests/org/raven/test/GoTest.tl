package org.raven.test;

import org.junit.Assert;
import java.lang.Thread;

var val = 0;

fun goTest1() {
    go toHell(5, 45);
    Thread.sleep(50);
    Assert.assertEquals(50, val);
}

fun goTest2() {
    var thread = go toHell(20, 40);
    thread.join();
    Assert.assertEquals(60, val);
}

fun toHell(with, params) {
    val = with + params;
}

