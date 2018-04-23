package org.raven.test;

import org.junit.Assert;

import testclasses.Person;
import testclasses.Container;

class SpecialPerson(a, b) extends Person(a, b) {
}

fun testInheritedMethods() {
    // test super() selection
    var me = SpecialPerson("Brad", 420);
    Assert.assertEquals("Brad", me.getName());
    Assert.assertEquals(420, me.getAge());

    me = SpecialPerson(420, "Brad");

    Assert.assertEquals("Brad", me.getName());
    Assert.assertEquals(420, me.getAge());
}

class ContainerSubClass(lst) extends Container(lst) {

    fun getSize() {
        // list is inherited field
        return list.size();
    }
}

fun testListCoercion() {
    var container = ContainerSubClass([100, 200, 300, 400]);
    Assert.assertEquals(100, container.get(0));
    Assert.assertEquals(200, container.get(1));
    Assert.assertEquals(300, container.get(2));
    Assert.assertEquals(4, container.getSize());
}
