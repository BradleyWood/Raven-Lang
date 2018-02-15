package org.toylang.test;

import org.toylang.test.Assert;

fun boolTest1() {
    Assert.assertEqual(true, true);
}

fun boolTest2() {
    Assert.assertEqual(false, false);
}

fun boolTest3() {
    Assert.assertNotEqual(true, false);
    Assert.assertNotEqual(false, true);
}

fun boolNotTest() {
    Assert.assertEqual(!true, false);
    Assert.assertEqual(!false, true);
    Assert.assertEqual(!!true, true);
    Assert.assertEqual(!!false, false);
}
