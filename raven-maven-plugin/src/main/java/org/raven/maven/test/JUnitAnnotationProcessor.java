package org.raven.maven.test;

import org.raven.antlr.Modifier;
import org.raven.antlr.Node;
import org.raven.antlr.RavenTree;
import org.raven.antlr.ast.*;
import org.raven.compiler.AnnotationProcessor;
import org.raven.compiler.JvmMethodAnnotationProcessor;

public class JUnitAnnotationProcessor implements AnnotationProcessor {

    private final JvmMethodAnnotationProcessor processor = new JvmMethodAnnotationProcessor();

    @Override
    public void process(final Annotation annotation) {
        if (isJunitTest(annotation.getParent(), annotation.getName()) && annotation.getParent() instanceof Fun) {
            Fun fun = (Fun) annotation.getParent();
            if (fun.getParams().length == 0) {
                fun.removeModifier(Modifier.STATIC);
                fun.forceDescriptor("()V", false);
            }
        }
    }

    public boolean isJunitTest(final Node node, final String annotationName) {
        if (annotationName.equals("org.junit.Test")) {
            return true;
        } else if (!annotationName.equals("Test")) {
            return false;
        }

        Node n = node;
        while (n != null) {
            if (n instanceof ClassDef) {
                RavenTree tree = ((ClassDef) n).getSourceTree();
                for (QualifiedName imp : tree.getImports()) {
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
