package org.raven.compiler;

import java.util.LinkedList;

public class Warning {

    private static LinkedList<String> WARNINGS = new LinkedList<>();

    public static int getWarningCount() {
        return WARNINGS.size();
    }

    public static void put(String warning) {
        WARNINGS.add(warning);
    }

    public static void reset() {
        WARNINGS.clear();
    }

    public static void printWarnings() {
        System.err.println("Compilation Completed with " + getWarningCount() + " warnings.");
        WARNINGS.forEach(System.err::println);
    }
}
