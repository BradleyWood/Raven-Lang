package org.toylang.compiler;

import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.Statement;

public interface AnnotationProcessor {

    void process(ToyTree file, Statement stmt);

}
