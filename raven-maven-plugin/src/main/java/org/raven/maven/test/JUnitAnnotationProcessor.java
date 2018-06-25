package org.raven.maven.test;

import org.raven.antlr.Modifier;
import org.raven.antlr.Node;
import org.raven.antlr.RavenTree;
import org.raven.antlr.ast.*;
import org.raven.compiler.AnnotationProcessor;

public class JUnitAnnotationProcessor implements AnnotationProcessor {

    @Override
    public void process(final Annotation annotation) {
        if (isJunitTest(annotation.getParent(), annotation.getName()) && annotation.getParent() instanceof Fun) {
            final Fun fun = (Fun) annotation.getParent();
            if (fun.getParams().length == 0) {
                fun.removeModifier(Modifier.STATIC);
                fun.forceDescriptor("()V", false);
            }
        }
    }

    private boolean isJunitTest(final Node node, final String annotationName) {
        if (annotationName.equals("org.junit.Test")) {
            return true;
        } else if (!annotationName.equals("Test")) {
            return false;
        }

        Node n = node;
        while (n != null) {
            if (n instanceof ClassDef) {
                final RavenTree tree = ((ClassDef) n).getSourceTree();
                for (final QualifiedName imp : tree.getImports()) {
                    if (imp.toString().equals("org.junit.Test")) {
                        return true;
                    }
                }
            }
            n = n.getParent();
        }

        return false;
    }
}
