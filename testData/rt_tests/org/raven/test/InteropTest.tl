package org.raven.test;

import org.junit.Assert;

import java.lang.Math;
import java.math.BigInteger;
import java.io.File;

fun interopTest1() {
    Assert.assertEquals(Math.sin(Math.PI / 2), 1, 0.00000001);
    Assert.assertEquals(Math.sin(Math.PI / 2),    0.99999999999999999, 0.00000001);
    Assert.assertNotEquals(Math.sin(Math.PI / 2), 0.9999999999999999);
}

fun interopTest2() {
    // test overloaded methods
    Assert.assertEquals(100, Math.abs(100));
    Assert.assertEquals(100.01, Math.abs(100.01), 0.00000001);
}

fun interopTest3() {
    var f = File(".");
    Assert.assertTrue(f.isDirectory());
}
