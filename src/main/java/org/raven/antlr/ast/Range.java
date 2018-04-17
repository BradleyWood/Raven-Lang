package org.raven.antlr.ast;

import java.util.Objects;

/**
 * These ranges are especially helpful for loops,
 * and make if extremely simple to define the start and
 * end points of a loop
 */
public class Range extends Expression {

    private final Expression start;
    private final Expression end;

    /**
     * Initialize the range expression
     * @param start The start of the range
     * @param end The end of the range
     */
    public Range(final Expression start, final Expression end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Get the starting expression for this range
     * @return
     */
    public Expression getStart() {
        return start;
    }

    /**
     * Get the ending expression for this range
     * @return
     */
    public Expression getEnd() {
        return end;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return Objects.equals(start, range.start) &&
                Objects.equals(end, range.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
