package org.raven.compiler;

import org.raven.antlr.ast.Annotation;

public interface AnnotationProcessor {

    void process(Annotation annotation);

}
