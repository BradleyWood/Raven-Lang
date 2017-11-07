package org.toylang.test;

import org.toylang.test.Assert;

import java.lang.Math;
import java.math.BigInteger;
import java.io.File;

fun interopTest1() {
    Assert.assertEqual(Math.sin(Math.PI / 2), 1);
    Assert.assertEqual(Math.sin(Math.PI / 2),    0.99999999999999999);
    Assert.assertNotEqual(Math.sin(Math.PI / 2), 0.9999999999999999);
}

fun interopTest3() {
    var f = File(".");
    Assert.assertTrue(f.isDirectory());
}
