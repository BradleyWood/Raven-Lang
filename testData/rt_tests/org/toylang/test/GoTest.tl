package org.toylang.test;

import org.toylang.test.Assert;
import java.lang.Thread;

var val = 0;

fun goTest1() {
    go toHell(5, 45);
    Thread.sleep(50);
    Assert.assertEqual(50, val);
}

fun toHell(with, params) {
    val = with + params;
}

