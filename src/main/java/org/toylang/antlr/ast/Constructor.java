package org.toylang.antlr.ast;

import org.toylang.antlr.Modifier;

public class Constructor extends Statement {

    public static Constructor DEFAULT = new Constructor(new Modifier[]{Modifier.PUBLIC}, null);

    private final Modifier[] modifiers;
    private final Block body;
    private final VarDecl[] params;
    private final Expression[] superParams;

    /**
     * Long hand constructor definition with no-param super constructor
     * @param modifiers
     * @param body
     * @param params
     */
    public Constructor(final Modifier[] modifiers, final Block body, final VarDecl... params) {
        this.modifiers = modifiers;
        this.body = body;
        this.params = params;
        this.superParams = null;
    }

    /**
     * Short hand non-default constructor definition
     * @param params
     */
    public Constructor(Expression... params) {
        this.modifiers = new Modifier[] {Modifier.PUBLIC};
        this.body = null;
        this.params = null;
        this.superParams = params;
    }

    public Modifier[] getModifiers() {
        return modifiers;
    }

    public Block getBody() {
        return body;
    }

    public VarDecl[] getParams() {
        return params;
    }

    public Expression[] getSuperParams() {
        return superParams;
    }

    public boolean isShorthand() {
        return superParams != null;
    }
}
