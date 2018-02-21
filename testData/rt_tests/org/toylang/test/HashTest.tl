package org.toylang.test;

import org.junit.Assert;

fun hashTest1() {
    var str = "an error";
    var error = error(str);
    Assert.assertEquals(hash(str), hash(error));
    Assert.assertEquals(error.hashCode(), hash(error));
}
