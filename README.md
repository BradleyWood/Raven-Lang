# JVMLang

## Intro

I have been working on a dynamically typed programming language and compiler
for the JVM platform. The project is in early stages but I have successfully
compiled and tested several programs.

The main goal is create a fast jvm language that lacks the verbosity
of java while maintaining full interoperability.

## Building and Running

```
mvn compile assembly:single
```

Run once from the root directory to build the builtin (.tl) files

```
java -jar target/toylang-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Then re-assemble the jar

```
mvn assembly:single
```

To run a test script

```
java -jar toylang-1.0-SNAPSHOT-jar-with-dependencies.jar <path to script>.tl
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
BigReal: to-do <br>
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

<br>
Below is the bytecode output of the compiler. Each class is initialized with an array
of constants since the wrapper types cannot go in the constant pool. This will prevent
commonly used constants such as numbers and strings from reinitialized every time
they are needed. In this case null and the string "Hello World." are placed within the constants and
only initialized once.

Inorder to show the message dialog we must call the method invoke from ToyObject.
This method will use reflection to determine the correct method to invoke and convert
the parameters to java types. When calling invoke we will pass a few parameters on the stack
including the class that declared the method, the name of the method, and the parameters
in the form of a list.


```
public class scripts/test/HelloWorld {

  // compiled from: HelloWorld.tl

  // access flags 0x1A
  private final static [Lorg/toylang/core/ToyObject; __CONSTANTS__

  // access flags 0x9
  public static main([Ljava/lang/String;)V
   L0
    LINENUMBER 6 L0
    LDC Ljavax/swing/JOptionPane;.class
    LDC "showMessageDialog"
    NEW org/toylang/core/ToyList
    DUP
    INVOKESPECIAL org/toylang/core/ToyList.<init> ()V
    GETSTATIC scripts/test/HelloWorld.__CONSTANTS__ : [Lorg/toylang/core/ToyObject;
    LDC 0
    AALOAD
    INVOKEVIRTUAL org/toylang/core/ToyObject.add (Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;
    GETSTATIC scripts/test/HelloWorld.__CONSTANTS__ : [Lorg/toylang/core/ToyObject;
    LDC 1
    AALOAD
    INVOKEVIRTUAL org/toylang/core/ToyObject.add (Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;
    INVOKESTATIC org/toylang/core/ToyObject.invoke (Ljava/lang/Class;Ljava/lang/String;Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;
    POP
    RETURN
    MAXSTACK = 5
    MAXLOCALS = 1

  // access flags 0x8
  static <clinit>()V
    LDC 2
    ANEWARRAY org/toylang/core/ToyObject
    DUP
    LDC 0
    NEW org/toylang/core/ToyNull
    DUP
    INVOKESPECIAL org/toylang/core/ToyNull.<init> ()V
    AASTORE
    DUP
    LDC 1
    NEW org/toylang/core/ToyString
    DUP
    LDC "Hello World."
    INVOKESPECIAL org/toylang/core/ToyString.<init> (Ljava/lang/String;)V
    AASTORE
    PUTSTATIC scripts/test/HelloWorld.__CONSTANTS__ : [Lorg/toylang/core/ToyObject;
    RETURN
    MAXSTACK = 6
    MAXLOCALS = 0

  // access flags 0x1
  public <init>()V
    ALOAD 0
    INVOKESPECIAL java/lang/Object.<init> ()V
    RETURN
    MAXSTACK = 1
    MAXLOCALS = 1
}
```

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
 11. <b>sum(list) : Adds the contents of a list</br> <br>
 12. <b>reverse(list) : Reverse the order of a list</br> <br>
 13. <b>hash(var) : Returns the hash of the var </br>

### In Progress

- Classes
    - Automatically generate constructors, getters and setters
    - Inherit from java classes

### Todo

- Automatically generate methods that accept and convert java types
to facilitate calls from java.