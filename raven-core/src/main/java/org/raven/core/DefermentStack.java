package org.raven.core;

import org.raven.core.wrappers.TObject;

import java.util.Stack;

@Hidden
public class DefermentStack {

    @Hidden
    private final Stack<TObject> expression = new Stack<>();

    @Hidden
    private final Stack<Integer> deferments = new Stack<>();

    /**
     *
     * @return The id (type) of the next statement to be deferred
     */
    @Hidden
    public int nextDeferment() {
        if (deferments.isEmpty())
            return -1;
        return deferments.pop();
    }

    /**
     *
     * @param id The id of the statement to defer
     */
    @Hidden
    public void defer(final int id) {
        deferments.push(id);
    }

    /**
     * Add an expression used in the deferment to the stack
     *
     * @param param The expression
     */
    @Hidden
    public void push(final TObject param) {
        expression.push(param);
    }

    @Hidden
    public TObject pop() {
        return expression.pop();
    }
}
