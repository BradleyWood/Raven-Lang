package org.toylang.compiler;

import java.util.LinkedList;

public class Warning {

    private static LinkedList<String> WARNINGS = new LinkedList<>();

    public int getWarningCount() {
        return WARNINGS.size();
    }

    public void put(String warning) {
        WARNINGS.add(warning);
    }

    public void reset() {
        WARNINGS.clear();
    }

    public void printWarnings() {
        System.err.println("Compilation Completed with " + getWarningCount() + " warnings.");
        WARNINGS.forEach(System.err::println);
    }
}
