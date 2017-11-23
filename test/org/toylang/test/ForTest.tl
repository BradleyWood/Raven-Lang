package org.toylang.test;

import org.toylang.test.Assert;


fun testRange1() {
    var j = 0;
    for i range 5 to 10 j += 1;
    Assert.assertEqual(5, j);
}

fun testRange2() {
    var n = 0;
    for i range 10 downto n {
        n += 1;
    }
    Assert.assertEqual(5, n);
}

fun testRange3() {
    var n = 0;
    for i range 100 downto 0 {
        n += 1;
    }
    Assert.assertEqual(n, 100);
}

fun testRange4() {
    var n = 0;
    for i range 0 to 100 {
        n += 1;
    }
    Assert.assertEqual(n, 100);
}

fun forTest1() {
    var j = 0;
    for var i = 0; i < 100; i += 1, j += 1;
    Assert.assertEqual(100, j);
}

fun forTest2() {
    var j = 0;
    for var i = 0; i < 100; i+=1, j += 1;
    Assert.assertEqual(100, j);
}

fun forTest3() {
    var j = 0;
    for var i = 0; i < 100; i+=2, i-=1, j+= 1;
    Assert.assertEqual(100, j);
}

fun forTest4() {
    var n = 0;
    for var i = 0; i < 100; i+=1 {
        n += 1;
    }
    Assert.assertEqual(n, 100);
}

fun forTest5() {
    var n = 0;
    for var i = 0; i < 100; i+=1, n+=1 {
        n -= 1;
    }
    Assert.assertEqual(n, 0);
}

fun forTest6() {
    var n = 0;
    for (var i = 0; i < 100; i+=1, n+=1) {
        n -= 1;
    }
    Assert.assertEqual(n, 0);
}

fun forTest7() {
    var n = 0;
    for ((var i = 0; i < 100; i+=1, n+=1)) {
        n -= 1;
    }
    Assert.assertEqual(n, 0);
}

fun testRange8() {
    var n = 0;
    for (((i range 100 downto 0))) {
        n += 1;
    }
    Assert.assertEqual(n, 100);
}

fun forTest9() {
    var i = 100;
    for (i range 0 to 10);

    Assert.assertEqual(i, 10);
}