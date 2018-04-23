package org.raven.antlr.ast;

import org.objectweb.asm.Type;
import org.raven.antlr.Modifier;
import org.raven.antlr.Operator;
import org.raven.core.wrappers.TObject;

import java.util.*;

public class Constructor extends ModifiableStatement {

    public static final Constructor DEFAULT = new Constructor(new Modifier[]{Modifier.PUBLIC}, null);

    private Block initBlock = new Block();
    private Block body;

    private final VarDecl[] params;
    private final Expression[] superParams;

    /**
     * Long hand constructor definition with no-param super constructor
     *
     * @param modifiers access modifiers for this constructor
     * @param body      The block including all user defined statements
     * @param params    The list of parameters this constructor may accept
     */
    public Constructor(final Modifier[] modifiers, final Block body, final VarDecl... params) {
        super(modifiers);
        this.body = body;
        this.params = params;
        this.superParams = null;
    }

    /**
     * Some instance variables may be defined and initialized outside the constructor. However,
     * it is the job of the constructor to initialize these fields when the class is instantiated.
     *
     * @param decl The variable declaration that we need to initialize
     */
    public void initializeVar(final VarDecl decl) {
        BinOp bop = new BinOp(decl.getName(), Operator.ASSIGNMENT, decl.getInitialValue());
        initBlock.append(bop);
    }

    /**
     * Short hand non-default constructor definition
     * <p>
     * Shorthand constructors are provided in the class definition
     * and only specify the parameters that will be passed to super()
     *
     * <code>class Point3D(x, y, z) extends Point2D(x,y) {}</code>
     *
     * @param params
     */
    public Constructor(final Expression... params) {
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

    /**
     * @return The block containing each statement in the constructor
     */
    public Block getBody() {
        return body;
    }

    /**
     * Set the new body for this constructor
     *
     * @param body The new constructor body
     */
    public void setBody(final Block body) {
        this.body = body;
    }

    /**
     * @return The parameters passed to this constructor
     */
    public VarDecl[] getParams() {
        return params;
    }

    /**
     * Each constructor in jvm must call super() or this(). This method provides the
     * arguments that will be used in the super call. These arguments come either from
     * and explicit super() call or from the shorthand constructor notation. If no
     * super() call is provided in the constructor then this method returns null
     *
     * @return Parameters to be used in the super call or null if none are provided
     */
    public Expression[] getSuperParams() {
        return superParams;
    }

    /**
     * Checks if the constructor was defined using shorthand notation.
     *
     * <code>class Point3D(x, y, z) extends Point2D(x,y) {}</code>
     *
     * @return Whether the constructor was defined with shorthand notation
     */
    public boolean isShorthand() {
        return superParams != null;
    }

    /**
     * Calculates the method descriptor for this constructor
     *
     * @return The method descriptor of this constructor
     */
    public String getDesc() {
        StringBuilder stringBuilder = new StringBuilder("(");
        for (VarDecl ignored : getParams()) {
            stringBuilder.append(Type.getType(TObject.class).getDescriptor());
        }
        return stringBuilder.append(")V").toString();
    }

    @Override
    public boolean equals(final Object o) {
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
