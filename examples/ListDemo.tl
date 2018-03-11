import java.util.Random;

var rand = Random();

fun main() {
    println();
    println("Welcome to the list demo demo!");
    println("For each line, enter a number. Type 'done' when you are done");

    var numbers = [];

    while (true) {
        var line = readLine();
        if line == "done"
            break;
        numbers += int(line);
    }

    println("Before... " + numbers);
    println("Sorting...");
    println("After... " + sort(numbers));
    println("The sum is " + sum(numbers));
}
