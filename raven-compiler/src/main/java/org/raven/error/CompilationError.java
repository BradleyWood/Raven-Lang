package org.raven.error;

import org.raven.antlr.ast.Statement;

import java.io.File;
import java.io.PrintStream;

public class CompilationError extends Error {

    private final Statement statement;
    private final File file;
    private final String klass;

    public CompilationError(final String file, final String klass, final Statement statement, final String message) {
        this(new File(file), klass, statement, message);
    }

    public CompilationError(final File file, final String klass, final Statement statement, final String message) {
        super(message);
        this.statement = statement;
        this.klass = klass;
        this.file = file;
    }

    @Override
    public void printError(final PrintStream printStream) {
        printStream.println("Error at "+ file.getName() + ": " + klass + " on line: " + statement.getLineNumber());
        System.err.println("\"" + statement.getText() + "\"");
        System.err.println(getMessage());
    }
}
