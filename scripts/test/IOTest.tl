package scripts.test;

import toylang.lang.IO;

fun main() {
    println("Enter a file path");
    var gg = IO.readFile(readLine());
    println(gg);
}
