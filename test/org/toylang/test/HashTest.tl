package org.toylang.test;

import org.toylang.test.Assert;

fun hashTest1() {
    var str = "an error";
    var error = error(str);
    Assert.assertEqual(hash(str), hash(error));
    Assert.assertEqual(error.hashCode(), hash(error));
}
