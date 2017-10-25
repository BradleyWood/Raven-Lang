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

fun dict2dTest() {
    var dict = {
        "one" : { 0 : "o", 1 : "n", 2 : "e" },
        "two" : { 0 : "t", 1 : "w", 2 : "o" },
        "three" : { 0 : "t", 1 : "h", 2 : "r", 3 : "e", 4 : "e" }
    };
    Assert.assertEqual("t", dict["two"][0]);
    Assert.assertEqual("w", dict["two"][1]);
    Assert.assertEqual("o", dict["two"][2]);
}

fun dict2dTest2() {
    var people = {
        "Brad" : { "id" : 1000594115, "School" : "UOIT" },
        "Stephen" : { "id" : 1000912365, "School" : "UofT" }
    };
    Assert.assertEqual("UOIT", people["Brad"]["School"]);
    Assert.assertEqual("UofT", people["Stephen"]["School"]);
}