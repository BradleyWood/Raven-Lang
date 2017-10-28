package org.toylang.test;

import org.toylang.test.Assert;

fun continueTest() {
    var gg = 0;
    for i range 0 to 10 {
        if(i == 5) {
            continue;
        }
        gg += 1;
    }
    Assert.assertEqual(9, gg);
}

fun continueTest2() {
    var gg = 0;
    for i range 0 to 10 {
        continue;
        gg += 1;
    }
    Assert.assertEqual(0, gg);
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