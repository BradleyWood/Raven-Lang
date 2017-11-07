package org.toylang.antlr.visitor;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.QualifiedName;

import java.util.ArrayList;
import java.util.List;

public class QualifiedNameVisitor extends ToyLangBaseVisitor<QualifiedName> {

    private QualifiedNameVisitor() {
    }

    @Override
    public QualifiedName visitQualifiedName(ToyLangParser.QualifiedNameContext ctx) {
        List<String> list = new ArrayList<>();
        for (TerminalNode terminalNode : ctx.IDENTIFIER()) {
            list.add(terminalNode.getText());
        }
        return new QualifiedName(list.toArray(new String[list.size()]));
    }

    public static QualifiedNameVisitor INSTANCE = new QualifiedNameVisitor();
}
