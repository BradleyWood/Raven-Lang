package org.toylang.antlr.ast;

public class Range {

    private final Expression start;
    private final Expression end;

    public Range(Expression start, Expression end) {
        this.start = start;
        this.end = end;
    }

    public Expression getStart() {
        return start;
    }

    public Expression getEnd() {
        return end;
    }
}
