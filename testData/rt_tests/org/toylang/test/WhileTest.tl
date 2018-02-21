package org.toylang.test;

import org.junit.Assert;

fun whileTest1() {
    var i = 0;
    while(i < 10) i+=1;

    Assert.assertEquals(i, 10);
}

fun whileTest2() {
    var i = 0;
    while(i < 10) {
        i += 1;
    }
    Assert.assertEquals(i, 10);
}

fun whileTest3() {
    var str = "";
    while(str != "aaaa") {
        str += "a";
    }
    Assert.assertEquals(str, "aaaa");
}

fun doWhileTest() {
    var counter = 0;
    do {
        counter += 1;
    } while (counter < 10);
    Assert.assertEquals(10, counter);
}

fun doWhileTest2() {
    var counter = 0;
        do {
            counter += 1;
        } while (false);
    Assert.assertEquals(1, counter);
}
