package org.raven.compiler;

import org.raven.antlr.RavenTree;
import org.raven.antlr.ast.Statement;

public interface AnnotationProcessor {

    void process(RavenTree file, Statement stmt);

}
