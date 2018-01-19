package org.toylang.test;

import org.toylang.test.Assert;
import org.toylang.core.wrappers.TInt;
import org.toylang.core.wrappers.TBigInt;

fun bigIntMultTest() {
    Assert.assertEqual(100000000000000, 10000000000000 * 10);

    Assert.assertEqual(100000000000000, 10 * 10000000000000);
}

fun bigIntDivTest() {
    Assert.assertEqual(10000000000000, 100000000000000 / 10);
    Assert.assertEqual(0, 10 / 100000000000000);
}

fun bigIntSubTest() {
    Assert.assertEqual(0, 100000000000000 - 100000000000000);
    Assert.assertEqual(100000000000000, 100000000000000 - 0);
}

fun bigIntAddTest() {
    Assert.assertEqual(200000000000000, 100000000000000 + 100000000000000);
    Assert.assertEqual(100000000000000, 100000000000000 + 0);
    Assert.assertEqual(100000000000000, 0 + 100000000000000);
    Assert.assertEqual(100000000000000, 0 + 100000000000000 + 0);
}

fun bigIntPowTest() {
    var bigInt = 10000000000000;
    var result = 100000000000000000000000000;
    bigInt = bigInt ** 2;
    Assert.assertEqual(result, bigInt);

    var a = 0;
    a = bigInt ** a;
    Assert.assertEqual(a, 1);
}

fun bigIntModTest() {
    Assert.assertEqual(10, 10 % 10000000000000);
    Assert.assertEqual(2, bigInt(10) % 8);
}

fun intAddOverflowTest() {
    var i = 2**30;
    var sum = i + i;
    Assert.assertEqual(TInt.TYPE, type(i));
    Assert.assertEqual(TBigInt.TYPE, type(sum));
    Assert.assertTrue(sum > i);
}

fun intSubOverflowTest() {
    var i = -(2**30);
    var diff = i - (2**30 + 11);
    Assert.assertEqual(TInt.TYPE, type(i));
    Assert.assertEqual(TBigInt.TYPE, type(diff));
    Assert.assertTrue(diff < i);
}

fun intMultOverflowTest() {
    var i = 2**30;
    var product = i * 10;
    Assert.assertEqual(TInt.TYPE, type(i));
    Assert.assertEqual(TBigInt.TYPE, type(product));
    Assert.assertTrue(product > i);
    Assert.assertEqual(10737418240, product);
}

fun intPowOverflowTest() {
    Assert.assertEqual(10000000000, 10 ** 10);
}
