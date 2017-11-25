package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Call;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.Literal;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.core.wrappers.TInt;

public class SliceVisitor extends ToyLangBaseVisitor<Expression> {

    private SliceVisitor() {
    }

    @Override
    public Expression visitSlice(ToyLangParser.SliceContext ctx) {
        QualifiedName varName = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
        QualifiedName funName = new QualifiedName("subList");
        Expression start = new Literal(new TInt(0));
        Expression end = new Call((Expression) varName, new QualifiedName("size"));
        if (ctx.lhs != null) {
            start = ctx.lhs.accept(ExpressionVisitor.INSTANCE);
        }
        if (ctx.rhs != null) {
            end = ctx.rhs.accept(ExpressionVisitor.INSTANCE);
        }
        return new Call((Expression) varName, funName, start, end);
    }

    public static SliceVisitor INSTANCE = new SliceVisitor();
}
