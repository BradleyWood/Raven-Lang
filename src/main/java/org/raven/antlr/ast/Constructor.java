package org.raven.antlr.ast;

import org.objectweb.asm.Type;
import org.raven.antlr.Modifier;
import org.raven.antlr.Operator;
import org.raven.core.wrappers.TObject;

import java.util.*;

public class Constructor extends ModifiableStatement {

    public static Constructor DEFAULT = new Constructor(new Modifier[]{Modifier.PUBLIC}, null);

    private Block initBlock = new Block();
    private Block body;

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
        super(modifiers);
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
        super(Modifier.PUBLIC);
        this.body = null;
        LinkedList<VarDecl> p = new LinkedList<>();
        for (Expression param : params) {
            if (param instanceof QualifiedName) {
                p.add(new VarDecl((QualifiedName) param, null));
            }
        }
        this.params = p.toArray(new VarDecl[p.size()]);
        this.superParams = params;
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
        if (getSuperParams() != null) {
            for (Expression ignored : getSuperParams()) {
                stringBuilder.append(Type.getType(TObject.class).getDescriptor());
            }
        }
        return stringBuilder.append(")V").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Constructor that = (Constructor) o;
        return Objects.equals(initBlock, that.initBlock) &&
                Objects.equals(body, that.body) &&
                getModifiers().equals(that.getModifiers()) &&
                Arrays.equals(params, that.params) &&
                Arrays.equals(superParams, that.superParams) &&
                Objects.equals(getAnnotations(), that.getAnnotations());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(initBlock, body);
        result = 31 * result + Objects.hashCode(getModifiers());
        result = 31 * result + Arrays.hashCode(params);
        result = 31 * result + Arrays.hashCode(superParams);
        return result;
    }
}
