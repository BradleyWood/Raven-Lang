package org.toylang.test;

import org.toylang.test.Assert;

f(x) = x * x * 50 + 5000;

fun f2(x) {
    return x * x * 50 + 5000;
}

g(x) = f(f(f(x)));

fun g2(x) {
    return f(f(f(x)));
}

fun testFunction1() {
    Assert.assertEqual(f(-10), f2(-10));
    Assert.assertEqual(f(0), f2(0));
    Assert.assertEqual(5000, f2(0));
    Assert.assertEqual(f(10), f2(10));
}

fun testFunction2() {
    Assert.assertEqual(g(-10), g2(-10));
    Assert.assertEqual(g(0), g2(0));
    Assert.assertEqual(g(10), g2(10));
}
