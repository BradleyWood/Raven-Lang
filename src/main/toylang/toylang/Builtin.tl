package toylang;

import java.lang.System;
import java.lang.Integer;
import java.lang.Double;
import java.util.Collections;
import java.util.Scanner;
import java.lang.Thread;
import java.lang.Class;

import org.toylang.core.wrappers.TError;
import org.toylang.core.wrappers.TList;

public fun isError(obj) {
    return type(obj) == TError.TYPE;
}

public fun error(obj) {
    return TError(str(obj));
}

public fun println(obj) {
    System.out.println(str(obj));
}

public fun println() {
    System.out.println();
}

public fun print(obj) {
    System.out.print(obj);
}

public fun str(obj) {
    return obj.toString();
}

public fun sort(list) {
    Collections.sort(list);
    return list;
}

public fun len(obj) {
    return obj.size();
}

public fun int(obj) {
    var intVal = obj.toInt();
    if (intVal == null) {
        return bigInt(obj);
    }
    return intVal;
}

public fun bigInt(obj) {
    return obj.toBigInt();
}

public fun real(obj) {
    return obj.toDouble();
}

public fun type(obj) {
    return obj.getType();
}

public fun readLine() {
    return Scanner(System.in).nextLine();
}

public fun hash(obj) {
    return obj.hashCode();
}

public fun exit(code) {
    System.exit(code);
}

public fun reverse(lst) {
    if (type(lst) != TList.TYPE) {
        return error("Illegal arguments of type: " + type(lst) + " expected: " + TList.TYPE);
    }
    var n = len(lst);
    var tmp;
    for i range 0 to n/2 {
        tmp = lst[i];
        lst[i] = lst[n - i - 1];
        lst[n - i - 1] = tmp;
    }
    return lst;
}

public fun sum(lst) {
    if(len(lst) < 1)
        return null;
    var sum = lst[0];
    var n = len(lst);
    for i range 1 to n {
        sum += lst[i];
    }
    return sum;
}
