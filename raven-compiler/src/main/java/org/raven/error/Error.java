package org.raven.error;

import java.io.PrintStream;

public class Error {

    private final String message;

    public Error(final String message) {
        this.message = message;
    }

    public void printError() {
        printError(System.err);
    }

    public void printError(final PrintStream stream) {
        stream.println("Error: " + message);
    }

    public String getMessage() {
        return message;
    }
}
