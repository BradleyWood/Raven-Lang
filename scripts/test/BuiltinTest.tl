package scripts.test;


public var xd = 100;

public var brool = "gggg";

fun main() {
    println("Enter your name");
    var name = readLine();
    println("Hello "+name);

    var lst = ["abcdef", "zabc", "dbcdef", "xdddd", "abcze"];
    println("Lets sort these strings...");
    println(lst);
    sort(lst);
    println("Here is the result");
    println(lst);

    println();
    println();

    lst = [1000, 123, 54,1233,954, 191, 355, 641,4,7776, 11223,414417];
    println("Lets sort these numbers...");
    println(lst);
    sort(lst);
    println("Here is the result");
    println(lst);

    println();
    println();
    println(vars());
}
