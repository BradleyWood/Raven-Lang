package org.toylang.test;

import org.toylang.test.Assert;

import toylang.Files;

fun readTest() {
    var stream = Files.openR("not-a-file.txt");
    Assert.assertTrue(isError(stream));
}

fun readTest2() {
    var stream = Files.openR("pom.xml");
    Assert.assertEqual(str(type(stream)), "java.io.FileInputStream");
}


fun writeTest() {
    var stream = Files.openR("pom.xml");
    Assert.assertEqual(str(type(stream)), "java.io.FileInputStream");
}

// todo;