package org.toylang.test;

import org.junit.Assert;

fun listGetter() {
    return [0, 1, 2, 3, 4, 5, 6, 7, 8];
}

fun listIdxTest() {
    Assert.assertEquals(2, listGetter()[2]);
    Assert.assertEquals(8, listGetter()[8]);
    Assert.assertEquals(0, listGetter()[0]);
    var xd = ListTest.listGetter()[5];
    Assert.assertEquals(5, xd);
}

fun sizeTest() {
    var list = [1, 2, 3, 4, 5];
    Assert.assertEquals(5, len(list));
    list = [];
    Assert.assertEquals(0, len(list));
}

fun listEQTest() {
    var list = [1, 2, 3, 4, 5];
    Assert.assertEquals(list, list);
    Assert.assertTrue(list == list);
    Assert.assertTrue([] == []);
    Assert.assertTrue([1, 2] == [1, 2]);
}

fun listNETest() {
    var list = [1, 2, 3, 4, 5];
    Assert.assertTrue(list != [5, 4, 3, 2, 1]);
    Assert.assertTrue(list != []);
}

fun appendTest() {
    var list = [];
    Assert.assertEquals(0, len(list));
    list += 5;
    list += 10;
    Assert.assertEquals(2, len(list));

    list.add(15);
    Assert.assertEquals(3, len(list));

    list = list + 20;
    Assert.assertEquals(4, len(list));
}

fun getTest() {
    var lst = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0];
    for i range 0 to len(lst) {
        Assert.assertEquals(i, lst[len(lst) - i - 1]);
    }
}

fun setTest() {
    var lst = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0];
    Assert.assertEquals(9, lst[0]);
    lst[0] = 10;
    Assert.assertEquals(10, lst[0]);
}

fun list2dTest() {
    var lst = [
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]
    ];
    Assert.assertEquals(3, len(lst));
}

fun list2dTest2() {
    var lst = [
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]
    ];
    Assert.assertEquals(5, lst[1][1]);
    lst[1][1] = 100;
    Assert.assertEquals(100, lst[1][1]);
}

fun list2dTest3() {
    var lst = [
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]
    ];
    Assert.assertEquals(6, sum(lst[0]));
    Assert.assertEquals(15, sum(lst[1]));
    Assert.assertEquals(24, sum(lst[2]));
}

fun sliceTest() {
    var lst = [2, 4, 6, 8, 10, 12, 14, 16];
    Assert.assertEquals(lst, lst[:]);
    Assert.assertEquals([6, 8, 10, 12, 14, 16], lst[2:]);
    Assert.assertEquals([2, 4], lst[:2]);
    Assert.assertEquals([4, 6], lst[1:3]);
}
