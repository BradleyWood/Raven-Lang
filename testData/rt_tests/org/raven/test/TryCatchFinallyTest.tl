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

fun testNestedTryCatch() {
    try {
        try {
            Assert.fail();
        } catch e {
            return;
        }
        Assert.fail("Exception not caught");
    } catch e {
        Assert.fail("Exception should not be caught here");
    }
    Assert.fail("No branch executed");
}

fun testNestedTryCatchFinally() {
    try {
        try {
            Assert.fail("abc");
        } catch e {
            return;
        } finally {
        }
        Assert.fail("Exception not caught");
    } catch e {
        Assert.fail("Exception should not be caught here");
    } finally {
    }
    Assert.fail("No branch executed");
}