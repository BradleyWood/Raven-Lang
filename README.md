# Raven-Lang

This is an experimental programming language built for the JVM platform. The main
goal of this project is to create a fast jvm language that lacks the verbosity
of java while maintaining interoperability. Antlr is used to perform parsing
and ASM bytecode manipulation framework is used for bytecode generation.
The runtime environment performs all type checking and coercions as well
as dynamic linking.


[Click here to try](http://bradleywood.me/projects/jvmlang.html)

## Building and Running


```
mvn clean install assembly:single
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

## Tests

