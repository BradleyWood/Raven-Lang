# Raven-Lang

This is an experimental programming language built for the JVM platform. The main
goal of this project is to create a fast jvm language that lacks the verbosity
of java while maintaining interoperability.

[Click here to try](http://bradleywood.me/projects/jvmlang.html)

## Building and Running

```
mvn install assembly:single
```

To run a test script


```
java -jar target/raven-1.0-SNAPSHOT-jar-with-dependencies.jar -r <path to script>.tl
```

### Command line options

Run in REPL (read-eval-print-loop) mode
```
-repl
```

Run a script
```
-r <path to script>
```

## Examples

``` Java
import javax.swing.JOptionPane;

fun main() {
    JOptionPane.showMessageDialog(null, "Hello World.");
}
```

[View more examples](https://github.com/BradleyWood/TlDemo)


### Builtins
<b>The following builtin functions are accessible globally<b><br><br>

 1. <b>print(var) : Prints a variable to the console</b><br>
 2. <b>println(var) : Prints a variable to the console followed by a new line</b><br>
 3. <b>println() : Prints a new line</b><br>
 4. <b>sort(list) : Sorts a list of items (assuming the elemts are comparable)</b><br>
 5. <b>vars() : A dictionary of accessible variables</b> <br>
 6. <b>readLine() : Reads a line of text from the standard input stream</b> <br>
 7. <b>type(var) : Returns the type of a variable</b> <br>
 8. <b>str(var) : Turns a var into its string representation</b> <br>
 9. <b>len(var) : Returns the size for a string, list, or dictionary</b> <br>
 9. <b>int(var) : Converts a variable to an integer if possible</b> <br>
 10. <b>real(var) : Converts a variable to a real number if possible</b> <br>
 11. <b>sum(list) : Adds the contents of a list</b> <br>
 12. <b>reverse(list) : Reverse the order of a list</b> <br>
 13. <b>hash(var) : Returns the hash of the var </b> <br>
 14. <b>exit(status) : terminate virtual machine </b> <br>


### Todo

- More Unit tests

- Import * from package

- Static import

- Import as
