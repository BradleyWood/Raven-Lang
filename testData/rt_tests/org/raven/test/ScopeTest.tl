package org.raven.test

import org.junit.Assert

// test scoping over different branches

fun scopeTest1() {
    {
        var i = 0
        Assert.assertEquals(0, i)
    }
    var i = 100
    Assert.assertEquals(100, i)
}

fun scopeTest2() {
    if (true) {
        var i = 0
        Assert.assertEquals(0, i)
    } else {
        var i = 170
    }
    var i = 10
    Assert.assertEquals(10, i)
}

fun scopeTest3() {
    for (i range 0 to 10) {}

    var i = 20
    Assert.assertEquals(20, i)
}

fun scopeTest4() {
    var j = 0
    while (j < 10) {
        var i = 100
        j += 1
    }
    var i = 50
    Assert.assertEquals(50, i)
}

fun scopeTest5() {
    var i = 100
    // no redeclaration should occur
    for (i range 10 to 20) {}
    Assert.assertEquals(20, i)
}

fun scopeTest6() {
    var i = 1000
    {
        Assert.assertEquals(1000, i)
        {
            i = 2000
            Assert.assertEquals(2000, i)
        }
    }
    Assert.assertEquals(2000, i)
}
