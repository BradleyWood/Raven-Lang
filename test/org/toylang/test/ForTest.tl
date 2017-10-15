package org.toylang.test;

import org.toylang.test.Assert;


fun testRange1() {
    for i range 0 to 5;
    Assert.assertEqual(i, 5);
}

fun testRange2() {
    var n = 100;
    for i range 0 to n;
    Assert.assertEqual(n, i);
}

fun testRange3() {
    var n = 0;
    for i range 100 downto 0 {
        n += 1;
    }
    Assert.assertEqual(n, 100);
    Assert.assertEqual(i, 0);
}

fun testRange4() {
    var n = 0;
    for i range 0 to 100 {
        n += 1;
    }
    Assert.assertEqual(n, 100);
    Assert.assertEqual(i, 100);
}

fun forTest1() {
    for (var i = 0; i < 100; i += 1);
    Assert.assertEqual(i, 100);
}

fun forTest2() {
    for var i = 0; i < 100; i+=1;
    Assert.assertEqual(i, 100);
}

fun forTest3() {
    for var i = 0; i < 100; i+=2, i-=1;
    Assert.assertEqual(i, 100);
}

fun forTest4() {
    var n = 0;
    for var i = 0; i < 100; i+=1 {
        n += 1;
    }
    Assert.assertEqual(n, 100);
    Assert.assertEqual(i, 100);
}

fun forTest5() {
    var n = 0;
    for var i = 0; i < 100; i+=1, n+=1 {
        n -= 1;
    }
    Assert.assertEqual(n, 0);
    Assert.assertEqual(i, 100);
}