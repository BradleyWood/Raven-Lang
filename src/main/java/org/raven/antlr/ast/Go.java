package org.raven.antlr.ast;

import java.util.Objects;

public class Go extends Statement {

    private Call goFun;

    /**
     * Initializes a Go statement
     * @param goFun The function to call
     */
    public Go(Call goFun) {
        this.goFun = goFun;
    }

    public Call getGoFun() {
        return goFun;
    }

    public void setGoFun(Call goFun) {
        this.goFun = goFun;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitGo(this);
    }

    @Override
    public boolean equals(Object o) {
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
