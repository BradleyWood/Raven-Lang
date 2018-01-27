package org.toylang.antlr.ast;

import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 */
public class ExpressionGroup extends Expression {

    private LinkedList<Statement> statements = new LinkedList<>();

    public ExpressionGroup(Statement... statements) {
        this.statements.addAll(Arrays.asList(statements));
    }

    @Override
    public void accept(TreeVisitor visitor) {
        statements.forEach(statement -> statement.accept(visitor));
    }
}
