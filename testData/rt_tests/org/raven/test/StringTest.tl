package org.raven.test;

import org.junit.Assert;

fun strTest1() {
    Assert.assertEquals("abc", "abc");
}

fun strTest2() {
    Assert.assertNotEquals("abcd", "abc");
}

fun strTest3() {
    Assert.assertEquals("abc", str("abc"));
}

fun strTest4() {
    Assert.assertEquals("21", str(21));
}

fun strTest5() {
    Assert.assertEquals("true", str(true));
}

fun strTest6() {
    Assert.assertEquals("null", str(null));
}

fun strTest7() {
    Assert.assertEquals("3.14", str(3.14));
}

fun strAddTest() {
    var result = "abcdef";
    Assert.assertEquals(result, "abc" + "def");
}

fun strAddTest2() {
    var result = "abc123";
    Assert.assertEquals(result, "abc" + 123);
}

fun strAddTest3() {
    var result = "123abc";
    Assert.assertEquals(result, 123 + "abc");
}

fun strGTTest() {
    var str1 = "aba";
    var str2 = "abz";
    Assert.assertEquals(true, str1 > str2);
    Assert.assertEquals(false, str2 > str1);
}

fun strLTTest() {
    var str1 = "aba";
    var str2 = "abz";
    Assert.assertEquals(false, str1 < str2);
    Assert.assertEquals(true, str2 < str1);
}

fun strGTETest() {
    var str1 = "aba";
    var str2 = "abz";
    var str3 = "aba";
    Assert.assertEquals(true, str1 >= str2);
    Assert.assertEquals(true, str1 >= str3);
    Assert.assertEquals(false, str2 >= str3);
}

fun strLTETest() {
    var str1 = "aba";
    var str2 = "abz";
    var str3 = "aba";
    Assert.assertEquals(false, str1 <= str2);
    Assert.assertEquals(true, str1 <= str3);
    Assert.assertEquals(true, str2 <= str3);
}

fun strEQTest() {
    var str1 = "aba";
    var str2 = "abz";
    var str3 = "aba";
    Assert.assertEquals(true, str1 == str1);
    Assert.assertEquals(false, str1 == str2);
    Assert.assertEquals(true, str1 == str3);
    Assert.assertEquals(false, str2 == str3);
}

fun strNETest() {
    var str1 = "aba";
    var str2 = "abz";
    var str3 = "aba";
    Assert.assertEquals(false, str1 != str1);
    Assert.assertEquals(true, str1 != str2);
    Assert.assertEquals(false, str1 != str3);
    Assert.assertEquals(true, str2 != str3);
}

fun strContainsTest() {
    var str = "abcdefghi";
    Assert.assertTrue(str.contains("def"));
}

fun strSubstringTest() {
    var str = "abcdefghi";
    Assert.assertTrue(str.substring(1) == "bcdefghi");
    Assert.assertTrue(str.substring(1, str.size() - 1) == "bcdefgh");
}

fun strEndsWithTest() {
    var str = "abcdefghi";
    Assert.assertTrue(str.endsWith("ghi"));
    Assert.assertTrue(!str.endsWith("abc"));
}

fun strStarssWithTest() {
    var str = "abcdefghi";
    Assert.assertTrue(!str.startsWith("ghi"));
    Assert.assertTrue(str.startsWith("abc"));
}

fun strUppercaseTest() {
    var str = "abc";
    Assert.assertTrue(str.toUpperCase() == "ABC");
    Assert.assertTrue(str.toUpperCase() != "abc");
}

fun strLowercaseTest() {
    var str = "ABC";
    Assert.assertTrue(str.toLowerCase() != "ABC");
    Assert.assertTrue(str.toLowerCase() == "abc");
}

fun strReplaceTest() {
    var str = "ABCdefGHI";
    Assert.assertTrue(str.replace("def", "") == "ABCGHI");
    Assert.assertTrue(str.replace("def", "") != "def");
}

fun strIndexOfTest() {
    var str = "ABCdefGHI";
    Assert.assertEquals(3, str.indexOf("def"));
}

fun testEqualsIgnoreCase() {
    Assert.assertTrue("AbCdEF".equalsIgnoreCase("abcdef"));
}

fun testIsEmpty() {
    Assert.assertTrue("".isEmpty());
    Assert.assertTrue(!("abc".isEmpty()));
}
