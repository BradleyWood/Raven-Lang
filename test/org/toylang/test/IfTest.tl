package org.toylang.test;

import org.toylang.test.Assert;

fun ifTest1() {
    if(false) {
        Assert.fail("false is not equal to true");
    }
}

fun ifTest2() {
    if(false) {
        Assert.fail("false is not equal to true");
    } else {
        return;
    }
    Assert.fail("else block did not run");
}

fun ifTest3() {
    var i = 3;

    if(i == 1) {
        Assert.fail();
    } else if(i == 2) {
        Assert.fail();
    } else if(i == 3) {
        return;
    } else if(i == 4) {
        Assert.fail();
    } else {
        Assert.fail();
    }
    Assert.fail("no branch executed");
}

fun ifTest4() {
    var i = 4;
    if(i == 1) {
        Assert.fail();
    } else {
        return;
    }
    Assert.fail("no branch executed");
}

