package scripts.test;

import javax.swing.JOptionPane;
import java.lang.System;
import org.toylang.compiler.AsmTest;
import org.toylang.core.Timer;


fun gg() {
    return "asdasd";
}

fun main() {
    //JOptionPane.showMessageDialog(null, "Hello World.");
    println("gg");
    //System.out.println(System.out.toString().toString());
    System.out.println("brool");

    var brool = System.out;

    var t = AsmTest();

    var dict = { "one" : 1, "two":2 };
    var list = [1, 2, 3, 4];
    println("gggg");
    println("ggg");
    println(dict);
    println(list[2]);
    println(dict["one"]);
    dict.put("lul", "broooool");
    println(dict["one"]);
    println(dict["two"]);
    println(dict["lul"]);

}