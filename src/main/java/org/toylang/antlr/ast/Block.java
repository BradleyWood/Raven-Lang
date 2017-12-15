package org.toylang.antlr.ast;

import java.util.LinkedList;
import java.util.List;

/**
 * A block of code that contains a set of statements
 * Should have its own scope
 */
public class Block extends Statement {

    private final LinkedList<Statement> statements = new LinkedList<>();

    /**
     * Initializes a block of code
     *
     * @param statements The list of statements in the block
     */
    public Block(Statement... statements) {
        for (Statement statement : statements) {
            append(statement);
        }
    }

    /**
     * Append a statement to the end of this block
     * @param statement The statement or expression
     */
    public void append(Statement statement) {
        statements.add(statement);
    }

    /**
     * Add a statement to the beginning of this block
     * @param statement The statement or expression
     */
    public void addBefore(Statement statement) {
        statements.addFirst(statement);
    }

    /**
     * Get the list of statements in this block
     * @return A list of statements
     */
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitBlock(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        statements.forEach(stmt -> builder.append(stmt.toString()).append(System.lineSeparator()));
        return "{" + System.lineSeparator() + builder.toString() + "}";
    }
}
