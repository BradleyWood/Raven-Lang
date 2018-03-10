package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.Modifier;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TString;

import static org.raven.antlr.RuleTester.testStatement;

public class TestAnnotatedMethod {

    @Test
    public void testAnnotatedMethod() {
        Annotation annotation = new Annotation("JvmMethod",
                new QualifiedName[]{
                        new QualifiedName("name"), new QualifiedName("ret")
                },
                new Literal[]{
                        new Literal(new TString("javaName")), new Literal(new TString("int"))
                });
        Fun fun = new Fun(new QualifiedName("someMethod"), new Block(), new Modifier[0], new String[0]);
        fun.addAnnotation(annotation);

        String txt = "@JvmMethod(name=\"javaName\", ret=\"int\") \n"
                + "fun someMethod() {}";

        testStatement(MethodDeclVisitor.INSTANCE, txt, fun);
    }
}
