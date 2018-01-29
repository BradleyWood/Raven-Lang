package org.toylang.antlr.ast;

import java.util.Arrays;
import java.util.Objects;

/**
 * An expression representing a function call
 */
public class Call extends Expression {

    private Expression precedingExpr = null;
    private QualifiedName name;
    private final Expression[] params;

    /**
     * Initializes a call, generally used for local static functions
     *
     * @param name   The name of the function to call
     * @param params The function parameters as expressions
     */
    public Call(QualifiedName name, Expression... params) {
        this.name = name;
        this.params = params;
    }

    /**
     * Initializes a call, usually represents calls on objects or other imported classes
     *
     * @param precedingExpr The expression should represent an object or imported name
     * @param name          The name of the function to call
     * @param params        The function parameters as expressions
     */
    public Call(Expression precedingExpr, QualifiedName name, Expression... params) {
        this.precedingExpr = precedingExpr;
        this.name = name;
        this.params = params;
    }

    /**
     * The expression that precedes this function call
     *
     * @return The expression
     */
    public Expression getPrecedingExpr() {
        return precedingExpr;
    }

    /**
     * Modify the expression preceding this function call
     *
     * @param precedingExpr The new expression
     */
    public void setPrecedingExpr(Expression precedingExpr) {
        this.precedingExpr = precedingExpr;
    }

    /**
     * The the name of this function call
     *
     * @return The fully qualified name
     */
    public QualifiedName getName() {
        return name;
    }

    /**
     * Set the name of this function call
     *
     * @param name The qualified name
     */
    public void setQualifiedName(QualifiedName name) {
        this.name = name;
    }

    /**
     * Get the parameters for this function call
     *
     * @return The list of parameters
     */
    public Expression[] getParams() {
        return params;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Call call = (Call) o;
        return Objects.equals(precedingExpr, call.precedingExpr) &&
                Objects.equals(name, call.name) &&
                Arrays.equals(params, call.params);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(super.hashCode(), precedingExpr, name);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }
}
