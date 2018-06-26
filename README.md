# Raven-Lang

## What is this?

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

## Build

```
git clone https://github.com/BradleyWood/Raven-Lang.git
```

```
mvn install
```

## Project Layout

- [raven-core](raven-core/src/main/java/org/raven/core) - the core runtime environment

- [raven-compiler](raven-compiler/src/main/java/org/raven) - code compilation

- [raven-maven-plugin](raven-maven-plugin/src/main/java/org/raven/maven) - maven compatibility - compilation and test compilation

- [raven-stdlib](raven-stdlib/src/main/raven/raven) - raven stdlib

- [raven-cli](raven-cli/src/main/java/org/raven) - cli

- [raven-example](example) - example maven projects


## Examples

This example serves to illustrate java interoperability. 

``` Java
import javax.swing.JOptionPane

fun main() {
    JOptionPane.showMessageDialog(null, "Hello World.")
}
```

[View more examples](https://github.com/BradleyWood/TlDemo)


## Tests

Many Raven tests are written in Raven and are compiled using the
[raven-maven-plugin](raven-maven-plugin/src/main/java/org/raven/maven).
To achieve Junit compatibility we add an annotation processor for the
@Test annotation to ensure that test methods are compiled as non-static
void methods.

#### Example

```java
import org.junit.Assert
import org.junit.Test
import testclasses.FieldTestClass

@Test
fun testAddition() {
    var a = 100
    var b = 200
    Assert.assertEquals(300, a + b)
}
```
