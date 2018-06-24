package org.raven.antlr;

public enum Operator {

    ASSIGNMENT("=", "ASS"),
    PLUS_EQUALS("+=", ""),
    MINUS_EQUALS("-=", ""),
    TIMES_EQUALS("*=", ""),
    DIV_EQUALS("/=", ""),
    MOD_EQUALS("%=", ""),

    ADD("+", "add"),
    SUB("-", "sub"),
    MULT("*", "mul"),
    DIV("/", "div"),
    EXP("**", "pow"),
    MOD("%", "mod"),


    NOT("!", "not"),
    INC("++", "inc"),
    DEC("--", "dec"),
    GT(">", "GT"),
    LT("<", "LT"),
    GTE(">=", "GTE"),
    LTE("<=", "LTE"),
    EQ("==", "EQ"),
    NE("!=", "NE"),
    AND("&&", "and"),
    OR("||", "or"),
    BT_AND("&", ""),
    BT_OR("|", ""),
    BT_XOR("^", ""),
    BT_LS("<<", ""),
    BT_RS(">>", ""),
    BT_ARS(">>>", ""),
    BT_NOT("~", "");

    public final String op;
    public final String name;

    Operator(final String op, final String name) {
        this.op = op;
        this.name = name;
    }

    @Override
    public String toString() {
        return op;
    }
}
