# JVMLang

## Intro

I have been working on a dynamically typed programming language and compiler
for the JVM platform. The project is in early stages but I have successfully
compiled and tested several programs.

The main goal is create a fast jvm language that lacks the verbosity
of java while maintaining full interoperability.

## Building and Running
<br>
```
mvn clean install assembly:single
```
<br>
To run a test script

```
java -jar toylang-1.0-SNAPSHOT-jar-with-dependencies.jar -r <path to script>.tl
```
<br>
### Command line options

<br>
Run with security manager
```
-secure
```

#### Mutually exclusive options

Run in REPL (read-eval-print-loop) mode
```
-repl
```

Run a script
```
-r <path to script>
```

Check files for correctness
```
-s <path to script>
```

Build an executable jar
```
-b <path to script>
```

#### Wrapper types

Types are wrapped to enforce type conversion. Below is a list of wrapper types.

<b>
Int <br>
Real<br>
Bool<br>
Null<br>
List<br>
Dictionary<br>
BigInt<br>
</b>


#### An example program to demonstrate java interop 
<br>

``` Java
import javax.swing.JOptionPane;

fun main() {
    JOptionPane.showMessageDialog(null, "Hello World.");
}
```

[View more examples](https://github.com/BradleyWood/TlDemo)


### Bultins
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

### In Progress

- Classes
    - Automatically generate constructors, getters and setters
    - Inherit from java classes

- Annotations and annotation processors
    - Generate adaptor methods through the @JvmMethod annotation
        - Annotation takes name, parameter types, and return type
        - Allows for java code to call our functions

### Todo

- More Unit tests