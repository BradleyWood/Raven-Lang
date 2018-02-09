package org.toylang.antlr.visitor;

import org.toylang.antlr.Operator;
import org.toylang.compiler.Errors;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;
import org.toylang.core.Application;
import org.toylang.util.Settings;


public class StatementVisitor extends ToyLangBaseVisitor<Statement> {


    private StatementVisitor() {
    }


    @Override
    public Statement visitStatement(ToyLangParser.StatementContext ctx) {
        Statement stmt = null;
        if (ctx.block() != null) {
            stmt = ctx.block().accept(BlockVisitor.INSTANCE);
        } else if (ctx.constructor() != null) {
            stmt = ctx.constructor().accept(ConstructorVisitor.INSTANCE);
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
            boolean printableExpression = true;
            if (stmt instanceof BinOp) {
                printableExpression = ((BinOp) stmt).getOp() != Operator.ASSIGNMENT;
            }
            if (Settings.getBoolean("REPL") && printableExpression) {
                stmt = new Call(new QualifiedName("println"), (Expression) stmt);
            } else if (stmt instanceof Call) {
                ((Call) stmt).setPop(true);
            } else if (stmt instanceof QualifiedName) {
                Errors.put("Not a statement: " + stmt.toString());
            }
        } else if (ctx.goStatement() != null) {
            stmt = ctx.goStatement().accept(GoVisitor.INSTANCE);
        } else if (ctx.SEMI() != null) {
            // empty statement
            stmt = new Statement();
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
