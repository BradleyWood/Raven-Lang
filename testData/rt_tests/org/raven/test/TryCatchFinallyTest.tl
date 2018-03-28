package org.raven.test;

import org.junit.Assert;

fun testTryCatch() {
    try {
        Assert.fail();
    } catch e {
        return;
    }
    Assert.fail("Exception not caught");
}

// test finally with exception thrown
fun testTryCatchFinally() {
    try {
        Assert.fail();
    } catch e {
    } finally {
        return;
    }
    Assert.fail("Finally not executed");
}

// test finally without exception thrown
fun testTryCatchFinally2() {
    try {
    } catch e {
    } finally {
        return;
    }
    Assert.fail("Finally not executed");
}
