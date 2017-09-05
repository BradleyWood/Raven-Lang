package org.toylang.antlr.ast;

import java.util.ArrayList;
import java.util.List;

public class Block extends Statement {

    private final List<Statement> statements = new ArrayList<>();

    public Block(Statement... statements) {
        for (Statement statement : statements) {
            append(statement);
        }
    }
    public void append(Statement statement) {
        statements.add(statement);
    }
    public List<Statement> getStatements() {
        return statements;
    }
    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitBlock(this);
        //statements.forEach(stmt -> stmt.accept(visitor));
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        statements.forEach(stmt -> builder.append(stmt.toString()).append(System.lineSeparator()));
        return "{" + System.lineSeparator() + builder.toString() + "}";
    }
}
