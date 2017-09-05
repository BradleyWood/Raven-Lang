package org.toylang.antlr;

public enum Operator {

    ASSIGNMENT("="),
    PLUS_EQUALS("+="),
    MINUS_EQUALS("-="),
    TIMES_EQUALS("*="),
    DIV_EQUALS("/="),
    MOD_EQUALS("%="),

    ADD("+"),
    SUB("-"),
    MULT("*"),
    DIV("/"),
    EXP("**"),
    MOD("%"),


    NOT("!"),
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    EQ("=="),
    NE("!="),
    AND("&&"),
    OR("||"),

    ;

    public final String op;

    Operator(String op) {
        this.op = op;
    }
    @Override
    public String toString() {
        return op;
    }
}
