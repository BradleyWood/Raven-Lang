package org.toylang.antlr.visitor;

import org.toylang.compiler.Errors;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;


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
            if (stmt instanceof Call) {
                ((Call) stmt).setPop(true);
            } else if (stmt instanceof QualifiedName) {
                Errors.put("Not a statement: " + stmt.toString());
            }
        } else if (ctx.goStatement() != null) {
            QualifiedName name = new QualifiedName(ctx.goStatement().funCall().IDENTIFIER().getText());
            Expression[] expressions = new Expression[0];
            if (ctx.goStatement().funCall().paramList() != null)
                expressions = new Expression[ctx.goStatement().funCall().paramList().param().size()];
            for (int i = 0; i < expressions.length; i++) {
                expressions[i] = ctx.goStatement().funCall().paramList().param(i).accept(ExpressionVisitor.INSTANCE);
            }
            Call go = new Call(name, expressions);
            go.setLineNumber(ctx.goStatement().funCall().start.getLine());
            stmt = new Go(go);
        } else if (ctx.SEMI() != null) {
            // empty statement
            stmt = new Statement();
        } else {
            System.err.println("Unimplemented parser rule for statement: " + ctx.getText());
        }
        //System.out.println("Statement: "+stmt);
        return stmt;
    }

    public static final StatementVisitor INSTANCE = new StatementVisitor();
}
