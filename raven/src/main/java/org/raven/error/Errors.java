package org.raven.error;

import java.util.LinkedList;

public class Errors {

    private static final LinkedList<Error> ERRORS = new LinkedList<>();

    public static void put(final String error) {
        ERRORS.add(new Error(error));
    }

    public static void put(final Error error) {
        ERRORS.add(error);
    }

    public static int getErrorCount() {
        return ERRORS.size();
    }

    public static void printErrors() {
        if (getErrorCount() > 0) {
            for (Error error : ERRORS) {
                error.printError();
            }
            System.err.println();
        }
    }

    public static LinkedList<Error> getErrors() {
        return ERRORS;
    }

    public static void reset() {
        ERRORS.clear();
    }
}
