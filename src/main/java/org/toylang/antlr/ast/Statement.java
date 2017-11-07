package org.toylang.antlr.ast;

import org.toylang.antlr.AST;

import java.util.ArrayList;
import java.util.List;

public class Statement extends AST {

    private final ArrayList<Annotation> annotations = new ArrayList<>();

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }

    @Override
    public void accept(TreeVisitor visitor) {
        //throw new RuntimeException("Invalid Node");
    }
}
