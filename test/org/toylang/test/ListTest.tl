package org.toylang.test;

import org.toylang.test.Assert;

fun sizeTest() {
    var list = [1, 2, 3, 4, 5];
    Assert.assertEqual(5, len(list));
    list = [];
    Assert.assertEqual(0, len(list));
}

fun appendTest() {
    var list = [];
    Assert.assertEqual(0, len(list));
    list += 5;
    list += 10;
    Assert.assertEqual(2, len(list));

    list.add(15);
    Assert.assertEqual(3, len(list));

    list = list + 20;
    Assert.assertEqual(4, len(list));
}

fun getTest() {
    var lst = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0];
    for i range 0 to len(lst) {
        Assert.assertEqual(i, lst[len(lst) - i - 1]);
    }
}

fun setTest() {
    var lst = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0];
    Assert.assertEqual(9, lst[0]);
    lst[0] = 10;
    Assert.assertEqual(10, lst[0]);
}
