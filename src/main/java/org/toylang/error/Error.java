package org.toylang.error;

import java.io.PrintStream;

public class Error {

    private final String message;

    public Error(String message) {
        this.message = message;
    }

    public void printError() {
        printError(System.err);
    }

    public void printError(PrintStream stream) {
        stream.println("Error: " + message);
    }

    public String getMessage() {
        return message;
    }
}
