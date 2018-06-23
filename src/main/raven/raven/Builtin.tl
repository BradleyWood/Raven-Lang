package raven;

import java.lang.System;
import java.lang.Integer;
import java.lang.Double;
import java.util.Collections;
import java.util.Scanner;
import java.lang.Thread;
import java.lang.Class;

import org.raven.core.wrappers.TError;
import org.raven.core.wrappers.TList;

fun isError(obj) {
    return type(obj) == TError.TYPE;
}

fun error(obj) {
    return TError(str(obj));
}

fun println(obj) {
    System.out.println(str(obj));
}

fun println() {
    System.out.println();
}

fun print(obj) {
    System.out.print(obj);
}

fun str(obj) {
    return obj.toString();
}

fun sort(list) {
    Collections.sort(list);
    return list;
}

fun len(obj) {
    return obj.size();
}

fun int(obj) {
    var intVal = obj.toInt();
    if (intVal == null) {
        return bigInt(obj);
    }
    return intVal;
}

fun bigInt(obj) {
    return obj.toBigInt();
}

fun real(obj) {
    return obj.toDouble();
}

fun type(obj) {
    return obj.getType();
}

fun readLine() {
    return Scanner(System.in).nextLine();
}

fun hash(obj) {
    return obj.hashCode();
}

fun exit(code) {
    System.exit(code);
}

fun reverse(lst) {
    if (type(lst) != TList.TYPE) {
        return error("Illegal arguments of type: " + type(lst) + " expected: " + TList.TYPE);
    }
    var n = len(lst);
    var tmp;
    for (i range 0 to n/2) {
        tmp = lst[i];
        lst[i] = lst[n - i - 1];
        lst[n - i - 1] = tmp;
    }
    return lst;
}

fun sum(lst) {
    if(len(lst) < 1)
        return null;
    var sum = lst[0];
    var n = len(lst);
    for (i range 1 to n) {
        sum += lst[i];
    }
    return sum;
}
