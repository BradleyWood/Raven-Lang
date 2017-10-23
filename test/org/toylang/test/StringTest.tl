
package org.toylang.test;

import org.toylang.test.Assert;

fun strTest1() {
    Assert.assertEqual("abc", "abc");
}

fun strTest2() {
    Assert.assertNotEqual("abcd", "abc");
}

fun strTest3() {
    Assert.assertEqual("abc", str("abc"));
}

fun strTest4() {
    Assert.assertEqual("21", str(21));
}

fun strTest5() {
    Assert.assertEqual("true", str(true));
}

fun strTest6() {
    Assert.assertEqual("null", str(null));
}

fun strTest7() {
    Assert.assertEqual("3.14", str(3.14));
}

fun strAddTest() {
    var result = "abcdef";
    Assert.assertEqual(result, "abc" + "def");
}

fun strAddTest2() {
    var result = "abc123";
    Assert.assertEqual(result, "abc" + 123);
}

fun strAddTest3() {
    var result = "123abc";
    Assert.assertEqual(result, 123 + "abc");
}
