package org.toylang.test;

import org.toylang.test.Assert;

fun addTest() {
    var i = 0;
    i = i + 10;
    i += 10;
    Assert.assertEqual(i, 20);
}

fun subTest() {
    var i = 0;
    i = i - 10;
    i -= 10;
    Assert.assertEqual(i, -20);
}

fun multTest() {
    var i = 10;
    i = i * 10;
    i *= 10;
    Assert.assertEqual(i, 1000);
}

fun divTest() {
    var i = 1000;
    i = i / 10;
    i /= 10;
    Assert.assertEqual(i, 10);
}

fun modTest() {
    var i = 13;
    Assert.assertEqual(i % 10, 3);
    i %= 10;
    Assert.assertEqual(i, 3);
}

fun powTest() {
    var i = 10;
    i = i**2;
    Assert.assertEqual(i, 100);
    i **= 2;
    Assert.assertEqual(i, 100 * 100);
}

fun gtTest() {
    Assert.assertEqual(10 > 9, true);
    Assert.assertEqual(10 > 10, false);
    Assert.assertEqual(9 > 10, false);
}

fun gteTest() {
    Assert.assertEqual(10 >= 9, true);
    Assert.assertEqual(10 >= 10, true);
    Assert.assertEqual(9 >= 10, false);
}

fun ltTest() {
    Assert.assertEqual(10 < 9, false);
    Assert.assertEqual(10 < 10, false);
    Assert.assertEqual(9 < 10, true);
}

fun lteTest() {
    Assert.assertEqual(10 <= 9, false);
    Assert.assertEqual(10 <= 10, true);
    Assert.assertEqual(9 <= 10, true);
}

fun eqTest() {
    Assert.assertEqual(10 == 10, true);
    Assert.assertEqual(10 == 11, false);
    Assert.assertEqual(-10 == 10, false);
}

fun neTest() {
    Assert.assertEqual(10 != 10, false);
    Assert.assertEqual(10 != 11, true);
    Assert.assertEqual(-10 != 10, true);
}
