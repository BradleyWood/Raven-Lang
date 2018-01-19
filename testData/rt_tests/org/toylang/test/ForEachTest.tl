package org.toylang.test;

import org.toylang.test.Assert;


fun forEachTest() {
    var lst = [1, 5, 10, 100, 1000, 1000000];
    var sum = 0;
    for i : lst {
        sum += i;
    }
    Assert.assertEqual(sum(lst), sum);
}

fun forEachTest2() {
    var sum = 0;
    for i : [100, 1000, 10000] {
        sum += i;
    }
    Assert.assertEqual(11100, sum);
}

fun forEachTest3() {
    var sum = 0;
    for (i : [100, 1000, 10000]) {
        sum += i;
    }
    Assert.assertEqual(11100, sum);
}