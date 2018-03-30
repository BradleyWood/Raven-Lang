package org.raven.test;

import java.io.ByteArrayOutputStream;
import java.lang.String;

import org.junit.Assert;
import raven.csv;

fun testWriter() {
    var baos = ByteArrayOutputStream();
    var writer = csv.getWriter(baos);

    writer.printRecord([1,2,3,4,5]);
    writer.printRecord([5,4,3,2,1]);
    writer.close();

    var str = String(baos.toByteArray());
    var expected = "1,2,3,4,5" + System.lineSeparator() + "5,4,3,2,1" + System.lineSeparator();

    Assert.assertEquals(expected, str);
}

fun testWriter2() {
    var baos = ByteArrayOutputStream();
    var writer = csv.getWriter(baos);

    writer.write(5);
    writer.write(6);
    writer.write(7);
    writer.write(8);
    writer.printRecord();
    writer.printRecord([5,4,3,2,1]);
    writer.close();

    var str = String(baos.toByteArray());
    var expected = "5,6,7,8" + System.lineSeparator() + "5,4,3,2,1" + System.lineSeparator();

    Assert.assertEquals(expected, str);
}