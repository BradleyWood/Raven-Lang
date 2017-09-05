package scripts.test;

import java.lang.System;
import java.lang.Thread;
import org.toylang.core.Timer;


fun factorial(num) {
    if(num <= 1) {
        return num;
    }
    return num * factorial(num - 1);
}
fun fib(n) {
    if(n == 0)
        return 0;
    if(n == 1)
        return 1;
    return fib(n - 1) + fib(n-2);
}
fun main() {
    var timer = Timer();
    count();
    println("Time = "+timer.elapsed() + " ms.");
    System.gc();
    Thread.sleep(25000);
    println("done");
}
fun count() {
    var i = 0;
    var max = 2000000000;
    while(i < max) {
        i += 1;
    }
}