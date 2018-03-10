package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Literal;
import org.raven.error.Errors;
import org.raven.core.wrappers.*;

public class LiteralVisitor extends RavenBaseVisitor<Expression> {

    private LiteralVisitor() {
    }

    @Override
    public Expression visitLiteral(RavenParser.LiteralContext ctx) {
        Expression expr = null;
        if (ctx.number() != null) {
            if (ctx.number().getText().contains(".") || ctx.number().getText().toLowerCase().contains("e")) {
                expr = new Literal(new TReal(Double.parseDouble(ctx.number().getText())));
            } else {
                try {
                    expr = new Literal(new TInt(Integer.parseInt(ctx.number().getText())));
                } catch (NumberFormatException e) {
                    expr = new Literal(new TBigInt(ctx.number().getText()));
                }
            }
        } else if (ctx.stringLiteral() != null) {
            String str = ctx.stringLiteral().getText();
            expr = new Literal(new TString(str.substring(1, str.length() - 1)));
        } else if (ctx.getText().equals("null")) {
            expr = new Literal(TNull.NULL); // use reference to null instead
        } else if (ctx.getText().equals("true")) {
            expr = new Literal(TBoolean.TRUE); // use reference
        } else if (ctx.getText().equals("false")) {
            expr = new Literal(TBoolean.FALSE); // use reference
        }
        if (expr == null) {
            Errors.put("Unsupported literal: " + ctx.getText());
        }
        return expr;
    }

    public static LiteralVisitor INSTANCE = new LiteralVisitor();
}
