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

private var out = System.out;
private var err = System.err;
private var in = System.in;
private var scan = Scanner(in);


public fun isError(obj) {
    return type(obj) == TError.TYPE;
}

public fun error(obj) {
    return TError(str(obj));
}
public fun println(obj) {
    out.println(obj);
}

public fun println() {
    out.println();
}

public fun print(obj) {
    out.println(obj);
}

public fun str(obj) {
    return obj.toString();
}

public fun sort(list) {
    return Collections.sort(list);
}

public fun len(obj) {
    return obj.size();
}

public fun int(obj) {
    return obj.toInt();
}

public fun real(obj) {
    return obj.toDouble();
}

public fun type(obj) {
    return obj.getType();
}

public fun readLine() {
    return scan.nextLine();
}

public fun vars() {
    var stackTrace = Thread.currentThread().getStackTrace();
    var name = stackTrace.get(8);
    var clazz = Class.forName(name.getClassName());
    var fields = clazz.getDeclaredFields();

    var dict = {};
    var i = 0;
    while (i < len(fields)) {
        var field = fields[i];
        field.setAccessible(true);
        dict.put(field.getName(), field.get(null));
        i += 1;
    }
    return dict;
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
