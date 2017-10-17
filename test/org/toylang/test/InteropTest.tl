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

fun interopTest2() {
    var bigint = BigInteger("100");
    var num = BigInteger("10000");
    Assert.assertEqual(bigint.multiply(bigint), num);
    Assert.assertTrue(bigint.multiply(bigint) == num);
}

fun interopTest3() {
    var f = File(".");
    Assert.assertTrue(f.isDirectory());
}
