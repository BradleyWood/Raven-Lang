package org.toylang.test;

import org.junit.Assert;

public class Ok(X, Y) {
    var Z = 100;

    public fun getZ() {
        return Z;
    }
    public fun setZ(z) {
        Z = z;
    }
}

fun testOk() {
    var gg = Ok(10, 20);
    Assert.assertEquals(10, gg.getX());
    Assert.assertEquals(20, gg.getY());
    Assert.assertEquals(100, gg.getZ());
}

public class SubClass extends Ok(100, 100) {}

fun testSubClass() {
    var gg = SubClass();
    Assert.assertEquals(100, gg.getX());
    Assert.assertEquals(100, gg.getY());
    Assert.assertEquals(100, gg.getZ());
    gg.setZ(200);
    Assert.assertEquals(200, gg.getZ());
    gg.setY(300);
    Assert.assertEquals(300, gg.getY());
    gg.setX(400);
    Assert.assertEquals(400, gg.getX());
}

public class SuperClass(x) {}


public fun aFun() {
    return 10;
}

var aTestVar = 100;
var anotherTestVar = "test";

public class FieldAccessSameFile {
    public constructor() {
        Assert.assertEquals(100, ClassTest.aTestVar);
        Assert.assertEquals("test", ClassTest.anotherTestVar);
        ClassTest.aTestVar = 500;
        ClassTest.anotherTestVar = true;
        Assert.assertEquals(500, ClassTest.aTestVar);
        Assert.assertEquals(true, ClassTest.anotherTestVar);
    }
}

fun testFieldAccess() {
    FieldAccessSameFile();
}
