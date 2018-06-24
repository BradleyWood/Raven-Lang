package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.*;

public class StatementVisitor extends RavenBaseVisitor<Statement> {

    private StatementVisitor() {
    }


    @Override
    public Statement visitStatement(final RavenParser.StatementContext ctx) {
        Statement stmt = null;
        if (ctx.block() != null) {
            stmt = ctx.block().accept(BlockVisitor.INSTANCE);
        } else if (ctx.tryCatchFinally() != null) {
            stmt = ctx.tryCatchFinally().accept(TryCatchFinallyVisitor.INSTANCE);
        } else if (ctx.constructor() != null) {
            stmt = ctx.constructor().accept(ConstructorVisitor.INSTANCE);
        } else if (ctx.raiseStatement() != null) {
            stmt = ctx.raiseStatement().accept(RaiseVisitor.INSTANCE);
        } else if (ctx.CONTINUE() != null) {
            stmt = new Continue();
        } else if (ctx.BREAK() != null) {
            stmt = new Break();
        } else if (ctx.classDef() != null) {
            stmt = ctx.classDef().accept(ClassDefVisitor.INSTANCE);
        } else if (ctx.varDeclaration() != null) {
            stmt = ctx.varDeclaration().accept(VarDeclVisitor.INSTANCE);
        } else if (ctx.methodDeclaration() != null) {
            stmt = ctx.methodDeclaration().accept(MethodDeclVisitor.INSTANCE);
        } else if (ctx.ifStatement() != null) {
            stmt = ctx.ifStatement().accept(IfVisitor.INSTANCE);
        } else if (ctx.whileStatement() != null) {
            stmt = ctx.whileStatement().accept(WhileVisitor.INSTANCE);
        } else if (ctx.forStatement() != null) {
            stmt = ctx.forStatement().accept(ForVisitor.INSTANCE);
        } else if (ctx.returnStatement() != null) {
            stmt = ctx.returnStatement().accept(ReturnVisitor.INSTANCE);
        } else if (ctx.expression() != null) {
            stmt = ctx.expression().accept(ExpressionVisitor.INSTANCE);
            ((Expression) stmt).setPop(true);
        } else {
            //System.err.println("Unimplemented parser rule for statement: " + ctx.getText());
        }

        if (stmt != null) {
            stmt.setLineNumber(ctx.getStart().getLine());
            stmt.setText(ctx.getText());
        }

        return stmt;
    }

    public static final StatementVisitor INSTANCE = new StatementVisitor();
}
