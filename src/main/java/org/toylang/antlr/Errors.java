package org.toylang.antlr;

import java.util.LinkedList;

public class Errors {

    private static final LinkedList<String> ERRORS = new LinkedList<>();

    public static void put(String error) {
        ERRORS.add(error);
    }

    public static int getErrorCount() {
        return ERRORS.size();
    }

    public static void printErrors() {
        if (getErrorCount() > 0) {
            for (String error : ERRORS) {
                System.err.println("Error: " + error);
            }
            System.err.println();
        }
    }
}
