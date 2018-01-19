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

fun doWhileTest() {
    var counter = 0;
    do {
        counter += 1;
    } while (counter < 10);
    Assert.assertEqual(10, counter);
}

fun doWhileTest2() {
    var counter = 0;
        do {
            counter += 1;
        } while (false);
    Assert.assertEqual(1, counter);
}
