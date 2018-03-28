package org.raven.test;

import org.junit.Assert;
import java.lang.NullPointerException;

fun testRaise() {
    try {
        raise NullPointerException();
    } catch e {
        return;
    }
    Assert.fail("Exception not caught");
}