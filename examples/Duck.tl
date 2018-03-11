
public class Raven {

    public fun fly() {
        println("Raven Flying");
    }
}

public class Airplane {

    public fun fly() {
        println("Airplane Flying");
    }
}

public class Dog {

    public fun woof() {
        println("Woof!");
    }
}

public fun liftOff(flyable) {
    flyable.fly();
}

fun main() {
    liftOff(Raven());
    liftOff(Airplane());
    liftOff(Dog()); // Dog has no attribute fly
}

// OUTPUT
//
// Raven Flying
// Airplane Flying
// java.lang.NoSuchMethodException: fly
//         at Duck.liftOff(D:\IdeaProjects\Toylang\examples\.\Duck.tl:24)
//         at Duck.main(D:\IdeaProjects\Toylang\examples\.\Duck.tl:30)
//         at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//         at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
//         at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
//         at java.base/java.lang.reflect.Method.invoke(Unknown Source)
//         at org.raven.util.Utility.compileAndRun(Utility.java:57)
//         at org.raven.core.Application.main(Application.java:76)
