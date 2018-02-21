package org.toylang.test;

import org.junit.Assert;

fun addTest() {
    var i = 0.0;
    i = i + 10.0;
    i += 10.0;
    Assert.assertEquals(i, 20, 0.00000001);
}

fun subTest() {
    var i = 0.0;
    i = i - 10.0;
    i -= 10.0;
    Assert.assertEquals(i, -20.0, 0.00000001);
}

fun multTest() {
    var i = 10.0;
    i = i * 10.0;
    i *= 10.0;
    Assert.assertEquals(i, 1000.0, 0.00000001);
}

fun divTest() {
    var i = 1000.0;
    i = i / 10.0;
    i /= 10.0;
    Assert.assertEquals(i, 10.0, 0.00000001);
}

fun modTest() {
    var i = 13.0;
    Assert.assertEquals(i % 10, 3.0, 0.00000001);
    i %= 10.0;
    Assert.assertEquals(i, 3.0, 0.00000001);
}

fun powTest() {
    var i = 10.0;
    i = i**2.0;
    Assert.assertEquals(i, 100.0, 0.00000001);
    i **= 2.0;
    Assert.assertEquals(i, 100.0 * 100.0, 0.00000001);
}

fun gtTest() {
    Assert.assertEquals(10.0 > 9.0, true);
    Assert.assertEquals(10.0 > 10.0, false);
    Assert.assertEquals(9.0 > 10.0, false);
}

fun gteTest() {
    Assert.assertEquals(10.0 >= 9.0, true);
    Assert.assertEquals(10.0 >= 10.0, true);
    Assert.assertEquals(9.0 >= 10.0, false);
}

fun ltTest() {
    Assert.assertEquals(10.0 < 9.0, false);
    Assert.assertEquals(10.0 < 10.0, false);
    Assert.assertEquals(9.0 < 10.0, true);
}

fun lteTest() {
    Assert.assertEquals(10.0 <= 9.0, false);
    Assert.assertEquals(10.0 <= 10.0, true);
    Assert.assertEquals(9.0 <= 10.0, true);
}

fun eqTest() {
    Assert.assertEquals(10.0 == 10.0, true);
    Assert.assertEquals(10.0 == 11.0, false);
    Assert.assertEquals(-10.0 == 10.0, false);
}

fun neTest() {
    Assert.assertEquals(10.0 != 10.0, false);
    Assert.assertEquals(10.0 != 11.0, true);
    Assert.assertEquals(-10.0 != 10.0, true);
}
