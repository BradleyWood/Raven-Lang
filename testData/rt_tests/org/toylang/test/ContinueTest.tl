package org.toylang.test;

import org.junit.Assert;

fun continueTest() {
    var gg = 0;
    for i range 0 to 10 {
        if(i == 5) {
            continue;
        }
        gg += 1;
    }
    Assert.assertEquals(9, gg);
}

fun continueTest2() {
    var gg = 0;
    for i range 0 to 10 {
        continue;
        gg += 1;
    }
    Assert.assertEquals(0, gg);
}

fun continueTest3() {
    for i range 0 to 100 {
        for j range 0 to 100 {
            if (i == j)
                continue;
            if (i == j)
                Assert.fail("Continue failed");
        }
        continue;
        Assert.fail("Continue failed");
    }
}

fun continueTest4() {
    var i = 0;
    var j = 0;
    while (i < 10) {
        i += 1;
        if(i == 5)
            continue;
        j += 1;
    }
    Assert.assertEquals(9, j);
}