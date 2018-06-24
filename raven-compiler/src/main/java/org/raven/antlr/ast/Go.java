package org.raven.antlr.ast;

import java.util.Objects;

public class Go extends Expression {

    private Call goFun;

    /**
     * Initializes a Go statement
     * @param goFun The function to call
     */
    public Go(final Call goFun) {
        this.goFun = goFun;
    }

    public Call getGoFun() {
        return goFun;
    }

    public void setGoFun(final Call goFun) {
        this.goFun = goFun;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitGo(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Go go = (Go) o;
        return Objects.equals(goFun, go.goFun);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goFun);
    }
}
