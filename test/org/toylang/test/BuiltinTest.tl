package org.toylang.test;

import org.toylang.core.wrappers.TString;
import org.toylang.core.wrappers.TNull;
import org.toylang.core.wrappers.TInt;
import org.toylang.core.wrappers.TReal;
import org.toylang.core.wrappers.TList;
import org.toylang.core.wrappers.TDict;
import org.toylang.core.wrappers.TBoolean;
import org.toylang.core.wrappers.TType;
import org.toylang.test.Assert;


fun intConversionTest() {
    var num = "42";
    Assert.assertEqual(int(num), 42);
    num = 42.0;
    Assert.assertEqual(int(num), 42);
    num = 42;
    Assert.assertEqual(int(num), 42);
}

fun realConversionTest() {
    var num = "42";
    Assert.assertEqual(real(num), 42.0);
    num = "42.0";
    Assert.assertEqual(real(num), 42.0);
    num = "1e6";
    Assert.assertEqual(real(num), real(1000000));
    Assert.assertEqual(real(100.00000000), 100.00000000);
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
    Assert.assertEqual(len(lst), 0);
    lst = [1, 2, 3];
    Assert.assertEqual(len(lst), 3);

    var dict = {};
    Assert.assertEqual(len(dict), 0);
    dict = { 1 : "a", 2 : "b"};
    Assert.assertEqual(len(dict), 2);
}

var testVar = "testval";

fun varsTest() {
    var varDict = vars();
    Assert.assertEqual(varDict.get("testVar"), "testval");
    Assert.assertEqual(len(varDict), 2);
}

fun typeTest() {
    var val = null;
    Assert.assertEqual(type(val), TNull.TYPE);
    val = "";
    Assert.assertEqual(type(val), TString.TYPE);
    val = 40;
    Assert.assertEqual(type(val), TInt.TYPE);
    val = 0.01;
    Assert.assertEqual(type(val), TReal.TYPE);
    val = [];
    Assert.assertEqual(type(val), TList.TYPE);
    val = {};
    Assert.assertEqual(type(val), TDict.TYPE);
    val = true;
    Assert.assertEqual(type(val), TBoolean.TYPE);
}

fun sumTest() {
    var lst = [10, 10, 100, 1000, 100000];
    Assert.assertEqual(sum(lst), 101120);
    lst = ["a", "b", "c", "d"];
    Assert.assertEqual(sum(lst), "abcd");
}

fun revsereTest() {
    var lst = [10, 10, 100, 1000, 100000];
    var reversed = [100000, 1000, 100, 10, 10];
    Assert.assertEqual(reverse(lst), reversed);
    Assert.assertTrue(isError(reverse("abc")));
}
