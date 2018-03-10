package org.raven.antlr.visitor;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.QualifiedName;

import java.util.ArrayList;
import java.util.List;

public class QualifiedNameVisitor extends RavenBaseVisitor<QualifiedName> {

    private QualifiedNameVisitor() {
    }

    @Override
    public QualifiedName visitQualifiedName(RavenParser.QualifiedNameContext ctx) {
        List<String> list = new ArrayList<>();

        if (ctx.THIS() != null) {
            list.add("this");
        }
        
        if (ctx.SUPER() != null) {
            list.add("super");
        }

        for (TerminalNode terminalNode : ctx.IDENTIFIER()) {
            list.add(terminalNode.getText());
        }
        return new QualifiedName(list.toArray(new String[list.size()]));
    }

    public static QualifiedNameVisitor INSTANCE = new QualifiedNameVisitor();
}
