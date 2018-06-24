package org.raven.test

import org.junit.Assert

fun breakTest() {
    var j = 0
    for (i range 0 to 100) {
        if(i == 50)
            break
        j += 1
    }
    Assert.assertEquals(j, 50)
}

fun breakTest2() {
    var sum = 0
    for (i range 0 to 100) {
        if (i == 25)
            break
        for (j range 0 to 100) {
            if (j == 50)
                break
            sum += 1
        }
        sum += 1
    }
    Assert.assertEquals(1275, sum)
}

fun breakTest3() {
    var i = 0
    while (i < 10) {
        i += 1
        if (i == 5) {
            break
        }
    }
    Assert.assertEquals(5, i)
}
