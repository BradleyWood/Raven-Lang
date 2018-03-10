package org.raven.test;

import org.junit.Assert;

fun sizeTest() {
    var dict = { "Name" : "Brad", "Color" : "Orange", "Tired" : true };
    Assert.assertEquals(3, len(dict));
}

fun getTest() {
    var dict = { "Name" : "Brad" };
    Assert.assertEquals("Brad", dict["Name"]);
}

fun putTest() {
    var dict = { "Name" : "Brad" };
    Assert.assertEquals(1, len(dict));

    dict["School"] = "UOIT";
    Assert.assertEquals("UOIT", dict["School"]);
    Assert.assertEquals(2, len(dict));

    Assert.assertEquals("Brad", dict["Name"]);
    dict["Name"] = dict["Name"] + " Wood";
    Assert.assertEquals("Brad Wood", dict["Name"]);
}

fun dict2dTest() {
    var dict = {
        "one" : { 0 : "o", 1 : "n", 2 : "e" },
        "two" : { 0 : "t", 1 : "w", 2 : "o" },
        "three" : { 0 : "t", 1 : "h", 2 : "r", 3 : "e", 4 : "e" }
    };
    Assert.assertEquals("t", dict["two"][0]);
    Assert.assertEquals("w", dict["two"][1]);
    Assert.assertEquals("o", dict["two"][2]);
}

fun dict2dTest2() {
    var people = {
        "Brad" : { "id" : 1000594115, "School" : "UOIT" },
        "Stephen" : { "id" : 1000912365, "School" : "UofT" }
    };
    Assert.assertEquals("UOIT", people["Brad"]["School"]);
    Assert.assertEquals("UofT", people["Stephen"]["School"]);
}