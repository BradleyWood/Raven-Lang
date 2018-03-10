package org.raven.antlr.ast;

import java.util.Objects;

public class Import extends Statement {

    private final QualifiedName name;

    /**
     * Initializes an import statement
     * @param name The fully qualified name
     */
    public Import(QualifiedName name) {
        this.name = name;
    }

    /**
     * Get the imported resource
     * @return The fully qualified name
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Import anImport = (Import) o;
        return Objects.equals(name, anImport.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
