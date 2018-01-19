package org.toylang.test;

import org.toylang.test.Assert;

fun addTest() {
    var i = 0.0;
    i = i + 10.0;
    i += 10.0;
    Assert.assertEqual(i, 20);
}

fun subTest() {
    var i = 0.0;
    i = i - 10.0;
    i -= 10.0;
    Assert.assertEqual(i, -20.0);
}

fun multTest() {
    var i = 10.0;
    i = i * 10.0;
    i *= 10.0;
    Assert.assertEqual(i, 1000.0);
}

fun divTest() {
    var i = 1000.0;
    i = i / 10.0;
    i /= 10.0;
    Assert.assertEqual(i, 10.0);
}

fun modTest() {
    var i = 13.0;
    Assert.assertEqual(i % 10, 3.0);
    i %= 10.0;
    Assert.assertEqual(i, 3.0);
}

fun powTest() {
    var i = 10.0;
    i = i**2.0;
    Assert.assertEqual(i, 100.0);
    i **= 2.0;
    Assert.assertEqual(i, 100.0 * 100.0);
}

fun gtTest() {
    Assert.assertEqual(10.0 > 9.0, true);
    Assert.assertEqual(10.0 > 10.0, false);
    Assert.assertEqual(9.0 > 10.0, false);
}

fun gteTest() {
    Assert.assertEqual(10.0 >= 9.0, true);
    Assert.assertEqual(10.0 >= 10.0, true);
    Assert.assertEqual(9.0 >= 10.0, false);
}

fun ltTest() {
    Assert.assertEqual(10.0 < 9.0, false);
    Assert.assertEqual(10.0 < 10.0, false);
    Assert.assertEqual(9.0 < 10.0, true);
}

fun lteTest() {
    Assert.assertEqual(10.0 <= 9.0, false);
    Assert.assertEqual(10.0 <= 10.0, true);
    Assert.assertEqual(9.0 <= 10.0, true);
}

fun eqTest() {
    Assert.assertEqual(10.0 == 10.0, true);
    Assert.assertEqual(10.0 == 11.0, false);
    Assert.assertEqual(-10.0 == 10.0, false);
}

fun neTest() {
    Assert.assertEqual(10.0 != 10.0, false);
    Assert.assertEqual(10.0 != 11.0, true);
    Assert.assertEqual(-10.0 != 10.0, true);
}
