package org.toylang.test;

import org.toylang.test.Assert;

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
}
