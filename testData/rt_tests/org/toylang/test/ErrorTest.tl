package org.toylang.test;

import org.junit.Assert;

fun testEq() {
    var a = error("an error message");
    var b = error("an error message");
    Assert.assertEquals(a, b);
    Assert.assertTrue(a == b);
}

fun testGetMessage() {
    var a = error("an error message");
    Assert.assertEquals("an error message", a.getMessage());
}
