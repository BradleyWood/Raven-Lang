package org.toylang.test;

import org.toylang.test.Assert;

fun sizeTest() {
    var dict = { "Name" : "Brad", "Color" : "Orange", "Tired" : true };
    Assert.assertEqual(3, len(dict));
}

fun getTest() {
    var dict = { "Name" : "Brad" };
    Assert.assertEqual("Brad", dict["Name"]);
}

fun putTest() {
    var dict = { "Name" : "Brad" };
    Assert.assertEqual(1, len(dict));

    dict["School"] = "UOIT";
    Assert.assertEqual("UOIT", dict["School"]);
    Assert.assertEqual(2, len(dict));

    Assert.assertEqual("Brad", dict["Name"]);
    dict["Name"] = dict["Name"] + " Wood";
    Assert.assertEqual("Brad Wood", dict["Name"]);
}
