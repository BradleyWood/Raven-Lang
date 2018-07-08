package org.raven.antlr.ast;

import java.util.Objects;

public class Case {

    private final Expression caseExpr;
    private final Block block;

    public Case(final Expression caseExpr, final Block block) {
        this.caseExpr = caseExpr;
        this.block = block;
    }

    public Expression getCaseExpr() {
        return caseExpr;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case aCase = (Case) o;
        return Objects.equals(caseExpr, aCase.caseExpr) &&
                Objects.equals(block, aCase.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseExpr, block);
    }

    @Override
    public String toString() {
        return "Case{" +
                "caseExpr=" + caseExpr +
                ", block=" + block +
                '}';
    }
}
