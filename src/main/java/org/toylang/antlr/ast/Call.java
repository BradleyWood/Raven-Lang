package org.toylang.antlr.ast;

import java.util.Arrays;

/**
 * An expression representing a function call
 */
public class Call extends Expression {

    private Expression precedingExpr = null;
    private QualifiedName name;
    private final Expression[] params;
    private boolean pop = false;

    /**
     * Initializes a call, generally used for local static functions
     * @param name The name of the function to call
     * @param params The function parameters as expressions
     */
    public Call(QualifiedName name, Expression... params) {
        this.name = name;
        this.params = params;
    }

    /**
     * Initializes a call, usually represents calls on objects or other imported classes
     * @param precedingExpr The expression should represent an object or imported name
     * @param name The name of the function to call
     * @param params The function parameters as expressions
     */
    public Call(Expression precedingExpr, QualifiedName name, Expression... params) {
        this.precedingExpr = precedingExpr;
        this.name = name;
        this.params = params;
    }

    /**
     * The expression that precedes this function call
     * @return The expression
     */
    public Expression getPrecedingExpr() {
        return precedingExpr;
    }

    /**
     * Modify the expression preceding this function call
     * @param precedingExpr The new expression
     */
    public void setPrecedingExpr(Expression precedingExpr) {
        this.precedingExpr = precedingExpr;
    }

    /**
     * The the name of this function call
     * @return The fully qualified name
     */
    public QualifiedName getName() {
        return name;
    }

    /**
     * Set the name of this function call
     * @param name The qualified name
     */
    public void setQualifiedName(QualifiedName name) {
        this.name = name;
    }

    /**
     * Get the parameters for this function call
     * @return The list of parameters
     */
    public Expression[] getParams() {
        return params;
    }

    /**
     * Sometimes the return value is not used and it must be popped off the stack
     *
     * @return True if the return value should be popped off the stack
     */
    public boolean pop() {
        return pop;
    }

    /**
     * If the result of this expression is not used, we may need to pop the value off the stack
     * @param pop Whether to pop the return value off the stack
     */
    public void setPop(boolean pop) {
        this.pop = pop;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitFunCall(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(params).forEach(builder::append);
        return "{" + precedingExpr + " . " + name.toString() + "(" + builder.toString() + ");}";
    }
}
