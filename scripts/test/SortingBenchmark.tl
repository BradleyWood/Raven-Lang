package scripts.test;

import java.util.Random;
import org.toylang.core.Timer;

var rand = Random();


fun main() {
    benchmark(5000);
    benchmark(50000);
    benchmark(500000);
    benchmark(5000000);
    benchmark(100000000);
}

fun benchmark(len) {
    var list = makeList(len);
    var timer = Timer();
    sort(list);
    println("Elapsed "+(timer.elapsed()) + " ms, n = "+len);
}

fun makeList(len) {
    var list = [];
    var i = 0;
    while(i < len) {
        list = list + rand.nextInt();
        i += 1;
    }
    return list;
}