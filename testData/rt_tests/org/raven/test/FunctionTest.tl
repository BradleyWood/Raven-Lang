package org.raven.test;

import org.junit.Assert;

f(x) = x * x * 50 + 5000;

fun f2(x) {
    return x * x * 50 + 5000;
}

g(x) = f(f(f(x)));

fun g2(x) {
    return f(f(f(x)));
}

fun testFunction1() {
    Assert.assertEquals(f(-10), f2(-10));
    Assert.assertEquals(f(0), f2(0));
    Assert.assertEquals(5000, f2(0));
    Assert.assertEquals(f(10), f2(10));
}

fun testFunction2() {
    Assert.assertEquals(g(-10), g2(-10));
    Assert.assertEquals(g(0), g2(0));
    Assert.assertEquals(g(10), g2(10));
}
