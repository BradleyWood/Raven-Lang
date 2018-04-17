package org.raven.antlr.ast;

import org.objectweb.asm.Type;
import org.raven.antlr.Modifier;
import org.raven.core.wrappers.TObject;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Fun extends ModifiableStatement {

    private QualifiedName name;
    private final Block body;
    private final List<Modifier> modifiers;
    private final String[] exceptions;
    private VarDecl[] params;
    private String javaDesc = null;

    public Fun(final QualifiedName name, final Block body, final Modifier[] modifiers, final String[] exceptions, final VarDecl... params) {
        super(modifiers);
        this.name = name;
        this.body = body;
        this.modifiers = new LinkedList<>();
        this.exceptions = exceptions;
        this.params = params;

        if (this.params == null)
            this.params = new VarDecl[0];
    }

    public Fun(final QualifiedName name, final Block body, final List<Modifier> modifiers, final String[] exceptions, final VarDecl... params) {
        this.name = name;
        this.body = body;
        this.modifiers = new LinkedList<>(modifiers);
        this.exceptions = exceptions;
        this.params = params;


        if (this.params == null)
            this.params = new VarDecl[0];
    }

    public void forceDescriptor(final String desc) {
        this.javaDesc = desc;
    }

    public QualifiedName getName() {
        return name;
    }

    public Block getBody() {
        return body;
    }

    public VarDecl[] getParams() {
        return params;
    }

    public void setName(final String name) {
        this.name = new QualifiedName(name);
    }

    public boolean isJavaMethod() {
        return javaDesc != null;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitFun(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(params).forEach(param -> builder.append(param).append(", "));
        return "fun " + name + "(" + builder.toString().substring(0, builder.length()) + ") " + body;
    }

    public int modifiers() {
        int mod = 0;
        if (getModifiers() == null)
            return mod;
        for (Modifier modifier : getModifiers()) {
            mod += modifier.getModifier();
        }
        return mod;
    }

    public String getDesc() {
        if (javaDesc != null)
            return javaDesc;

        if (getName().toString().equals("main")) {
            return "([Ljava/lang/String;)V";
        } else if (getName().toString().equals("<clinit>")) {
            return "()V";
        }

        Type[] paramTypes = new Type[params.length];
        Arrays.fill(paramTypes, Type.getType(TObject.class));
        return Type.getMethodDescriptor(Type.getType(TObject.class), paramTypes);
    }

    public String[] getExceptions() {
        return exceptions;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fun fun = (Fun) o;
        return Objects.equals(name, fun.name) &&
                Objects.equals(body, fun.body) &&
                Objects.equals(modifiers, fun.modifiers) &&
                Arrays.equals(exceptions, fun.exceptions) &&
                Arrays.equals(params, fun.params) &&
                Objects.equals(getAnnotations(), fun.getAnnotations());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, body);
        result = 31 * result + Objects.hashCode(modifiers);
        result = 31 * result + Arrays.hashCode(exceptions);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }

    public boolean hasModifier(final Modifier modifier) {
        for (Modifier m : getModifiers()) {
            if (m.equals(modifier))
                return true;
        }
        return false;
    }

    public static Fun valueOf(final Method method) {
        List<Modifier> modifierList = new LinkedList<>();
        int modifiers = method.getModifiers();
        if (java.lang.reflect.Modifier.isStatic(modifiers)) {
            modifierList.add(Modifier.STATIC);
        }
        if (java.lang.reflect.Modifier.isPublic(modifiers)) {
            modifierList.add(Modifier.PUBLIC);
        }
        if (java.lang.reflect.Modifier.isPrivate(modifiers)) {
            modifierList.add(Modifier.PRIVATE);
        }
        String[] exceptions = new String[method.getExceptionTypes().length];

        for (int i = 0; i < method.getExceptionTypes().length; i++) {
            exceptions[i] = method.getExceptionTypes()[i].getName().replace(".", "/");
        }

        VarDecl[] params = new VarDecl[method.getParameterCount()];

        Fun fun = new Fun(QualifiedName.valueOf(method.getName()), new Block(),
                modifierList.toArray(new Modifier[modifierList.size()]), exceptions, params);
        int i = 0;
        boolean isJava = false;
        for (Class<?> paramType : method.getParameterTypes()) {
            if (!paramType.equals(TObject.class)) {
                isJava = true;
                break;
            }
            params[i++] = new VarDecl(QualifiedName.valueOf(String.valueOf(i)), null);
        }

        if (!method.getReturnType().equals(TObject.class)) {
            isJava = true;
        }

        if (isJava)
            fun.forceDescriptor(Type.getMethodDescriptor(method));

        return fun;
    }
}
