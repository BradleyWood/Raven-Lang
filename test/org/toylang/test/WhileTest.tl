package org.toylang.test;

import org.toylang.test.Assert;

fun whileTest1() {
    var i = 0;
    while(i < 10) i+=1;

    Assert.assertEqual(i, 10);
}

fun whileTest2() {
    var i = 0;
    while(i < 10) {
        i += 1;
    }
    Assert.assertEqual(i, 10);
}

fun whileTest3() {
    var str = "";
    while(str != "aaaa") {
        str += "a";
    }
    Assert.assertEqual(str, "aaaa");
}