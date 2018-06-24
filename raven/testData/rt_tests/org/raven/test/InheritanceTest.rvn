package org.raven.test

import org.junit.Assert

import testclasses.Person
import testclasses.Container
import testclasses.FieldTestClass

class SpecialPerson(a, b) extends Person(a, b) {
}

class HackedPerson(a, b) extends Person(a, b) {

    fun getName() {
        return "who"
    }

    fun getAge() {
        return -1
    }
}

class VirtualFieldInheritance extends FieldTestClass {

    fun getVirtualField() {
        return virtualPrimitiveInt
    }
}

class StaticFieldInheritance extends FieldTestClass {

    fun getStaticField() {
        return staticInt
    }

    fun delegate() {
        return aStaticMethod()
    }
}

fun testStaticMethodInheritance() {
    var obj = StaticFieldInheritance()
    Assert.assertEquals(-1, obj.aStaticMethod())
    Assert.assertEquals(-1, obj.delegate())
}

fun testStaticFieldInheritance() {
    var obj = StaticFieldInheritance()
    Assert.assertEquals(101, obj.staticInt)
    Assert.assertEquals(101, obj.getStaticField())
}

fun testVirtualFieldInheritance() {
    var obj = VirtualFieldInheritance()
    Assert.assertEquals(50, obj.virtualPrimitiveInt)
    Assert.assertEquals(50, obj.getVirtualField())
}

fun testOverride() {
    var me = HackedPerson("Brad", 420)
    Assert.assertEquals("who", me.getName())
    Assert.assertEquals(-1, me.getAge())
}

fun testInheritedMethods() {
    // test super() selection
    var me = SpecialPerson("Brad", 420)
    Assert.assertEquals("Brad", me.getName())
    Assert.assertEquals(420, me.getAge())

    me = SpecialPerson(420, "Brad")

    Assert.assertEquals("Brad", me.getName())
    Assert.assertEquals(420, me.getAge())
}

class ContainerSubClass(lst) extends Container(lst) {

    fun getSize() {
        // list is inherited field
        return list.size()
    }
}

fun testListCoercion() {
    var container = ContainerSubClass([100, 200, 300, 400])
    Assert.assertEquals(100, container.get(0))
    Assert.assertEquals(200, container.get(1))
    Assert.assertEquals(300, container.get(2))
    Assert.assertEquals(4, container.getSize())
}
