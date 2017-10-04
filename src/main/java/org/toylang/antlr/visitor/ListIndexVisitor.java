package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.ListIndex;
import org.toylang.antlr.ast.QualifiedName;

public class ListIndexVisitor extends ToyLangBaseVisitor<ListIndex> {

    private ListIndexVisitor() {

    }

    @Override
    public ListIndex visitListIdx(ToyLangParser.ListIdxContext ctx) {
        QualifiedName lst = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
        Expression[] ia = new Expression[ctx.expression().size()];
        for (int i = 0; i < ia.length; i++) {
            ia[i] = ctx.expression(i).accept(ExpressionVisitor.INSTANCE);
        }
        return new ListIndex(lst, ia);
    }

    public static final ListIndexVisitor INSTANCE = new ListIndexVisitor();
}
