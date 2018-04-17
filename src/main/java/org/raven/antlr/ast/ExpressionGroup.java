package org.raven.antlr.ast;

import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 */
public class ExpressionGroup extends Expression {

    private LinkedList<Statement> statements = new LinkedList<>();

    public ExpressionGroup(final Statement... statements) {
        this.statements.addAll(Arrays.asList(statements));
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        statements.forEach(statement -> statement.accept(visitor));
    }
}
