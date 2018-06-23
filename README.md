# Raven-Lang

This is an experimental programming language built for the JVM platform. The main
goal of this project is to create a fast jvm language that lacks the verbosity
of java while maintaining interoperability. Antlr is used to perform parsing
and the ASM bytecode manipulation framework is used for bytecode generation.
The runtime environment performs all type checking and coercions as well
as dynamic linking.


### [Web Demo](http://bradleywood.me/tryraven.html)

View multiple examples or try to create your own program using
the web demo. The web demo also comes with a REPL (read-eval-print-loop)
utility.

## Building and Running


```
mvn clean install
```

To run a test script

```
java -jar target/raven-1.0-SNAPSHOT-jar-with-dependencies.jar -r <path to script>.tl
```

### Command line options

To run in REPL (read-eval-print-loop) mode use the command line option "-repl"
and to execute a script with "-r path_to_script"

## Examples

This example serves to illustrate java interoperability. 

``` Java
import javax.swing.JOptionPane;

fun main() {
    JOptionPane.showMessageDialog(null, "Hello World.")
}
```

[View more examples](https://github.com/BradleyWood/TlDemo)

## Tests

Each test from the "testData" folder will always be run during the test phase
of the build. If a test file fails to compile it will be marked as a test failure.

### REPL Tests

Tests for the REPL (read-eval-print-loop) utility are located in the "testData/repl/"
folder and will run as long as the file has a ".repl" extension. A REPL test defines
the expected output for the given input. Lines containing the specified input are 
prefixed with ">". Lines containing only a "*" will denote that we will allow any
output for that given line. All other lines are assumed to be the expected output.

#### Example
```java
> 5 + 21
26
> 100
*
```

### Normal Tests

Regular unit tests are stored under the "testData/rt_tests" folder with the ".tl"
file extension. These tests are written directly in the Raven language. Inorder
for a function to be registered as a test it must have the word "test" (case insensitive) 
in the function name. Tests that throw uncaught exceptions are said to fail.


#### Example

```java
import org.junit.Assert;
import testclasses.FieldTestClass;

fun testVirtualField() {
    var obj = FieldTestClass("world", 500)
    Assert.assertEquals("world", obj.virtualString)
    Assert.assertEquals(500, obj.virtualWrappedInt)
}
```

This example will test access to virtual fields of a class written in Java