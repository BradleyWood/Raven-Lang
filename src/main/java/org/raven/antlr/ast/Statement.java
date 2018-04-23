package org.raven.antlr.ast;

import org.raven.antlr.AST;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Statement extends AST {

    private final ArrayList<Annotation> annotations = new ArrayList<>();

    protected String text;

    protected int lineNumber = -1;

    /**
     * Returns any annotations that this statement may have.
     *
     * @return The List of Annotations
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * Set the string of tokens that comprise this statement
     *
     * @param text The string of tokens
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * @return the string of tokens that comprise this statement
     */
    public String getText() {
        return text;
    }

    /**
     * Add a new annotation to this statement
     *
     * @param annotation The annotation to add
     */
    public void addAnnotation(final Annotation annotation) {
        annotations.add(annotation);
    }

    /**
     *
     * @return The line number that this statement was written on
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     *
     * @param lineNumber The line number
     */
    public void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statement statement = (Statement) o;
        return Objects.equals(annotations, statement.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotations, text, lineNumber);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getText() + "]";
    }
}
