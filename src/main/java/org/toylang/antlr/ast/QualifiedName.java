package org.toylang.antlr.ast;

public class QualifiedName extends Expression {

    private String[] names;

    public QualifiedName(String... names) {
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }

    @Override
    public String toString() {
        if (names == null || names.length == 0)
            return null;
        StringBuilder qualifiedName = new StringBuilder();
        for (String name : names) {
            qualifiedName.append(name).append(".");
        }
        return qualifiedName.substring(0, qualifiedName.length() - 1);
    }

    public QualifiedName add(String name) {
        String[] sa = new String[names.length + 1];
        for (int i = 0; i < names.length; i++) {
            sa[i] = names[i];
        }
        sa[names.length] = name;
        return new QualifiedName(sa);
    }

    public void update(QualifiedName name) {
        this.names = name.getNames();
    }

    public QualifiedName add(QualifiedName name) {
        String[] sa = new String[names.length + name.getNames().length];
        int i;
        for (i = 0; i < names.length; i++) {
            sa[i] = names[i];
        }
        for (String s : name.getNames()) {
            sa[i] = s;
            i++;
        }
        return new QualifiedName(sa);
    }

    @Override
    public void accept(TreeVisitor visitor) {
        //System.out.println("Visited qn: "+toString());
        visitor.visitName(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualifiedName that = (QualifiedName) o;
        return toString().toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}