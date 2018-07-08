package org.raven.antlr.ast;

public class When extends Expression {

    private final Expression condition;
    private final Case[] cases;
    private final Block elseBlock;

    public When(final Expression condition, final Case[] cases, final Block elseBlock) {
        this.condition = condition;
        this.cases = cases;
        this.elseBlock = elseBlock;
    }

    public Expression getCondition() {
        return condition;
    }

    public Case[] getCases() {
        return cases;
    }

    public Block getElseCase() {
        return elseBlock;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitWhen(this);
    }
}
