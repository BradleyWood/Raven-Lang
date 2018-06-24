package org.raven.test

import org.junit.Assert
import testclasses.NestedField

fun testNestedFieldGetters() {
    Assert.assertEquals("a", NestedField.instance.value)
    Assert.assertEquals("b", NestedField.instance.nestedField.value)
    Assert.assertEquals("c", NestedField.instance.nestedField.nestedField.value)
    Assert.assertEquals("c", NestedField.instance.nestedField.nestedField.getValue())
}

fun testNestedFieldSetters() {
    NestedField.instance.value = "c"
    NestedField.instance.nestedField.value = "b"
    NestedField.instance.nestedField.nestedField.value = "a"

    Assert.assertEquals("c", NestedField.instance.value)
    Assert.assertEquals("b", NestedField.instance.nestedField.value)
    Assert.assertEquals("a", NestedField.instance.nestedField.nestedField.value)

    NestedField.instance.value = "a"
    NestedField.instance.nestedField.value = "b"
    NestedField.instance.nestedField.nestedField.value = "c"

    Assert.assertEquals("a", NestedField.instance.value)
    Assert.assertEquals("b", NestedField.instance.nestedField.value)
    Assert.assertEquals("c", NestedField.instance.nestedField.nestedField.value)
}

var field = NestedField.instance

fun testGetNestedFieldLocalVar() {
    var localVar = NestedField.instance
    Assert.assertEquals("a", localVar.value)
    Assert.assertEquals("b", localVar.nestedField.value)
    Assert.assertEquals("c", localVar.nestedField.nestedField.value)

    Assert.assertEquals("c", localVar.nestedField.nestedField.getValue())
    Assert.assertEquals("c", field.nestedField.nestedField.getValue())

    Assert.assertEquals("a", field.value)
    Assert.assertEquals("b", field.nestedField.value)
    Assert.assertEquals("c", field.nestedField.nestedField.value)
}


fun testSetNestedFieldLocalVar() {
    var localVar = NestedField.instance

    // test assignment of nested field through the local variable
    localVar.value = "c"
    localVar.nestedField.value = "b"
    localVar.nestedField.nestedField.value = "a"

    Assert.assertEquals("c", localVar.value)
    Assert.assertEquals("b", localVar.nestedField.value)
    Assert.assertEquals("a", localVar.nestedField.nestedField.value)

    // test assignment of nested field through the local field
    field.value = "1"
    field.nestedField.value = "2"
    field.nestedField.nestedField.value = "3"

    Assert.assertEquals("1", localVar.value)
    Assert.assertEquals("2", localVar.nestedField.value)
    Assert.assertEquals("3", localVar.nestedField.nestedField.value)

    // set values back to the original
    field.value = "a"
    field.nestedField.value = "b"
    field.nestedField.nestedField.value = "c"

    Assert.assertEquals("a", localVar.value)
    Assert.assertEquals("b", localVar.nestedField.value)
    Assert.assertEquals("c", localVar.nestedField.nestedField.value)
}
