package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Call;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Literal;
import org.raven.antlr.ast.QualifiedName;
import org.raven.core.wrappers.TInt;

public class SliceVisitor extends RavenBaseVisitor<Expression> {

    private SliceVisitor() {
    }

    @Override
    public Expression visitSlice(RavenParser.SliceContext ctx) {
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
