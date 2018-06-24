package org.raven.antlr.ast;

/**
 * A break statement used in loops etc
 */
public class Break extends Statement {

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitBreak();
    }

    @Override
    public int hashCode() {
        return 420;
    }
}
