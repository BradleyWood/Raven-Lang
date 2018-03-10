package org.raven.compiler;

import org.raven.antlr.ToyTree;
import org.raven.antlr.ast.Statement;

public interface AnnotationProcessor {

    void process(ToyTree file, Statement stmt);

}
