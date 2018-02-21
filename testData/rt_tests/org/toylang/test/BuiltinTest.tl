package org.toylang.test;

import org.toylang.core.wrappers.TString;
import org.toylang.core.wrappers.TNull;
import org.toylang.core.wrappers.TInt;
import org.toylang.core.wrappers.TReal;
import org.toylang.core.wrappers.TList;
import org.toylang.core.wrappers.TDict;
import org.toylang.core.wrappers.TBoolean;
import org.toylang.core.wrappers.TType;
import org.junit.Assert;


fun intConversionTest() {
    var num = "42";
    Assert.assertEquals(int(num), 42);
    num = 42.0;
    Assert.assertEquals(int(num), 42);
    num = 42;
    Assert.assertEquals(int(num), 42);
}

fun realConversionTest() {
    var num = "42";
    Assert.assertEquals(real(num), 42.0, 0.00000001);
    num = "42.0";
    Assert.assertEquals(real(num), 42.0, 0.00000001);
    num = "1e6";
    Assert.assertEquals(real(num), real(1000000), 0.00000001);
    Assert.assertEquals(real(100.00000000), 100.00000000, 0.00000001);
}

fun sortTest() {
    var numbers = [120, 10, 441, 122, 21, 45, 184, 1233, 512, 1224, 864];
    sort(numbers);
    for i range 0 to len(numbers) - 1 {
        Assert.assertTrue(numbers[i] < numbers[i + 1]);
    }
}

fun lenTest() {
    var lst = [];
    Assert.assertEquals(len(lst), 0);
    lst = [1, 2, 3];
    Assert.assertEquals(len(lst), 3);

    var dict = {};
    Assert.assertEquals(len(dict), 0);
    dict = { 1 : "a", 2 : "b"};
    Assert.assertEquals(len(dict), 2);
}

fun typeTest() {
    var val = null;
    Assert.assertEquals(type(val), TNull.TYPE);
    val = "";
    Assert.assertEquals(type(val), TString.TYPE);
    val = 40;
    Assert.assertEquals(type(val), TInt.TYPE);
    val = 0.01;
    Assert.assertEquals(type(val), TReal.TYPE);
    val = [];
    Assert.assertEquals(type(val), TList.TYPE);
    val = {};
    Assert.assertEquals(type(val), TDict.TYPE);
    val = true;
    Assert.assertEquals(type(val), TBoolean.TYPE);
}

fun sumTest() {
    var lst = [10, 10, 100, 1000, 100000];
    Assert.assertEquals(sum(lst), 101120);
    lst = ["a", "b", "c", "d"];
    Assert.assertEquals(sum(lst), "abcd");
}

fun revsereTest() {
    var lst = [10, 10, 100, 1000, 100000];
    var reversed = [100000, 1000, 100, 10, 10];
    Assert.assertEquals(reverse(lst), reversed);
    Assert.assertTrue(isError(reverse("abc")));
}
