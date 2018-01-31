package org.toylang.antlr.ast;

import org.toylang.antlr.AST;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Statement extends AST {

    private final ArrayList<Annotation> annotations = new ArrayList<>();

    protected String text;

    protected int lineNumber = -1;

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public void accept(TreeVisitor visitor) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statement statement = (Statement) o;
        return Objects.equals(annotations, statement.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotations);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getText() + "]";
    }
}
