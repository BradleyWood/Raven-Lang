package org.toylang.antlr.ast;

import java.util.Arrays;

public class Call extends Expression {

    private Expression precedingExpr = null;
    private QualifiedName name;
    private final Expression[] params;
    private boolean pop = false;

    public Call(QualifiedName name, Expression... params) {
        this.name = name;
        this.params = params;
    }

    public Call(Expression procedingExpr, QualifiedName name, Expression... params) {
        this.precedingExpr = procedingExpr;
        this.name = name;
        this.params = params;
    }

    public Expression getPrecedingExpr() {
        return precedingExpr;
    }

    public void setPrecedingExpr(Expression precedingExpr) {
        this.precedingExpr = precedingExpr;
    }

    public QualifiedName getName() {
        return name;
    }

    public void setQualifiedName(QualifiedName name) {
        this.name = name;
    }

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

    public void setPop(boolean pop) {
        this.pop = pop;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        //super.accept(visitor);
        visitor.visitFunCall(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(params).forEach(builder::append);
        return "{" + precedingExpr + " . " + name.toString() + "(" + builder.toString() + ");}";
    }
}
