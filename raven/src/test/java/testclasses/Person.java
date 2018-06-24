package testclasses;

import java.util.ArrayList;
import java.util.List;

public class Person {

    private List<Person> children = new ArrayList<>();
    private final String name;
    private final int age;

    public Person(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    public Person(final int age, final String name) {
        this(name, age);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void addChild(final Person person) {
        this.children.add(person);
    }

    public List<Person> getChildren() {
        return children;
    }
}
