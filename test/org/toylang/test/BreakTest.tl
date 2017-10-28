package org.toylang.test;

import org.toylang.test.Assert;

fun breakTest() {
    var j = 0;
    for i range 0 to 100 {
        if(i == 50)
            break;
        j += 1;
    }
    Assert.assertEqual(j, 50);
}

fun breakTest2() {
    var sum = 0;
    for i range 0 to 100 {
        if i == 25
            break;
        for j range 0 to 100 {
            if j == 50
                break;
            sum += 1;
        }
        sum += 1;
    }
    Assert.assertEqual(1275, sum);
}