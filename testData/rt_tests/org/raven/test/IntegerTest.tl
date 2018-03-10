package org.raven.test;

import org.junit.Assert;

fun addTest() {
    var i = 0;
    i = i + 10;
    i += 10;
    Assert.assertEquals(i, 20);
}

fun subTest() {
    var i = 0;
    i = i - 10;
    i -= 10;
    Assert.assertEquals(i, -20);
}

fun multTest() {
    var i = 10;
    i = i * 10;
    i *= 10;
    Assert.assertEquals(i, 1000);
}

fun divTest() {
    var i = 1000;
    i = i / 10;
    i /= 10;
    Assert.assertEquals(i, 10);
}

fun modTest() {
    var i = 13;
    Assert.assertEquals(i % 10, 3);
    i %= 10;
    Assert.assertEquals(i, 3);
}

fun powTest() {
    var i = 10;
    i = i**2;
    Assert.assertEquals(i, 100);
    i **= 2;
    Assert.assertEquals(i, 100 * 100);
}

fun gtTest() {
    Assert.assertEquals(10 > 9, true);
    Assert.assertEquals(10 > 10, false);
    Assert.assertEquals(9 > 10, false);
}

fun gteTest() {
    Assert.assertEquals(10 >= 9, true);
    Assert.assertEquals(10 >= 10, true);
    Assert.assertEquals(9 >= 10, false);
}

fun ltTest() {
    Assert.assertEquals(10 < 9, false);
    Assert.assertEquals(10 < 10, false);
    Assert.assertEquals(9 < 10, true);
}

fun lteTest() {
    Assert.assertEquals(10 <= 9, false);
    Assert.assertEquals(10 <= 10, true);
    Assert.assertEquals(9 <= 10, true);
}

fun eqTest() {
    Assert.assertEquals(10 == 10, true);
    Assert.assertEquals(10 == 11, false);
    Assert.assertEquals(-10 == 10, false);
}

fun neTest() {
    Assert.assertEquals(10 != 10, false);
    Assert.assertEquals(10 != 11, true);
    Assert.assertEquals(-10 != 10, true);
}
