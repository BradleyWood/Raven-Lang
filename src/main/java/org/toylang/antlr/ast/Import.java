package org.toylang.antlr.ast;

public class Import extends Statement {

    private final QualifiedName name;

    public Import(QualifiedName name) {
        this.name = name;
    }

    public QualifiedName getName() {
        return name;
    }
    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitImport(this);
    }
    @Override
    public String toString() {
        return "import " + name.toString() + ";";
    }
}
