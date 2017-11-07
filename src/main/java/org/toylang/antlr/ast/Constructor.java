package org.toylang.antlr.ast;

import org.objectweb.asm.Type;
import org.toylang.antlr.Modifier;
import org.toylang.antlr.Operator;
import org.toylang.core.wrappers.TNull;
import org.toylang.core.wrappers.TObject;

import java.util.LinkedList;

public class Constructor extends Statement {

    public static Constructor DEFAULT = new Constructor(new Modifier[]{Modifier.PUBLIC}, null);

    private Block initBlock = new Block();
    private Block body;

    private final Modifier[] modifiers;
    private final VarDecl[] params;
    private final Expression[] superParams;

    /**
     * Long hand constructor definition with no-param super constructor
     *
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

    public void initializeVar(VarDecl decl) {
        BinOp bop = new BinOp(decl.getName(), Operator.ASSIGNMENT, decl.getInitialValue());
        initBlock.append(bop);
    }

    /**
     * Short hand non-default constructor definition
     *
     * @param params
     */
    public Constructor(Expression... params) {
        this.modifiers = new Modifier[]{Modifier.PUBLIC};
        this.body = null;
        LinkedList<VarDecl> p = new LinkedList<>();
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof QualifiedName) {
                p.add(new VarDecl((QualifiedName) params[i], null, null));
            }
        }
        this.params = p.toArray(new VarDecl[p.size()]);
        this.superParams = params;
    }

    public Modifier[] getModifiers() {
        return modifiers;
    }

    public Block getInitBlock() {
        return initBlock;
    }

    public Block getBody() {
        return body;
    }

    public void setBody(Block body) {
        this.body = body;
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

    public String getDesc() {
        StringBuilder stringBuilder = new StringBuilder("(");
        for (VarDecl ignored : getParams()) {
            stringBuilder.append(Type.getType(TObject.class).getDescriptor());
        }
        return stringBuilder.append(")V").toString();
    }

    public String getSuperConstructorDesc() {
        StringBuilder stringBuilder = new StringBuilder("(");
        for (Expression ignored : getSuperParams()) {
            stringBuilder.append(Type.getType(TObject.class).getDescriptor());
        }
        return stringBuilder.append(")V").toString();
    }
}
