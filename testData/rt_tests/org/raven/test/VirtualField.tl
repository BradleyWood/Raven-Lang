package org.raven.test;

import org.junit.Assert;
import testclasses.FieldTestClass;

fun testVirtualField() {
    var obj = FieldTestClass();
    Assert.assertEquals("world", obj.virtualString);
    Assert.assertEquals(500, obj.virtualWrappedInt);
}

// test primitive coercion in virtual fields
fun testVirtualFieldCoercion() {
    var obj = FieldTestClass();
    Assert.assertEquals(50, obj.virtualPrimitiveInt);
}
