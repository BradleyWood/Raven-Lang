package org.raven.antlr.ast;

import java.util.Objects;

public class TryCatchFinally extends Statement {

    private final Block body;
    private final Block handler;
    private final Block finallyBlock;

    public TryCatchFinally(Block body, Block handler, Block finallyBlock) {
        this.body = body;
        this.handler = handler;
        this.finallyBlock = finallyBlock;
    }

    public Block getBody() {
        return body;
    }

    public Block getHandler() {
        return handler;
    }

    public Block getFinallyBlock() {
        return finallyBlock;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitTryCatchFinally(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TryCatchFinally that = (TryCatchFinally) o;
        return Objects.equals(body, that.body) &&
                Objects.equals(handler, that.handler) &&
                Objects.equals(finallyBlock, that.finallyBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), body, handler, finallyBlock);
    }
}
