package org.toylang.js;


import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.*;

/**
 * Javascript trans-compiler
 */
public class JSCompiler implements TreeVisitor {

    private final ToyTree tree;

    public JSCompiler(final ToyTree tree) {
        this.tree = tree;
    }

    @Override
    public void visitIf(If ifStatement) {

    }

    @Override
    public void visitFor(For forStatement) {

    }

    @Override
    public void visitWhile(While whileStatement) {

    }

    @Override
    public void visitReturn(Return ret) {

    }

    @Override
    public void visitFun(Fun fun) {

    }

    @Override
    public void visitFunCall(Call call) {

    }

    @Override
    public void visitBlock(Block block) {

    }

    @Override
    public void visitVarDecl(VarDecl decl) {

    }

    @Override
    public void visitImport(Import importStatement) {

    }

    @Override
    public void visitBinOp(BinOp op) {

    }

    @Override
    public void visitUnaryOp(UnaryOp op) {

    }

    @Override
    public void visitLiteral(Literal literal) {

    }

    @Override
    public void visitName(QualifiedName name) {

    }

    @Override
    public void visitListDef(ListDef def) {

    }

    @Override
    public void visitListIdx(ListIndex idx) {

    }

    @Override
    public void visitClassDef(ClassDef def) {

    }

    @Override
    public void visitDictDef(DictDef def) {

    }

    @Override
    public void visitAnnotation(Annotation annotation) {

    }

    @Override
    public void visitAnnotationDef(AnnoDef def) {

    }

    @Override
    public void visitGo(Go go) {
        throw new RuntimeException("Go not allowed");
    }

    @Override
    public void visitContinue() {

    }

    @Override
    public void visitBreak() {

    }
}
