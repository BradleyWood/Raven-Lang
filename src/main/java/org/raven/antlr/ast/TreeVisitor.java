package org.raven.antlr.ast;


public interface TreeVisitor {

    void visitIf(If ifStatement);

    void visitFor(For forStatement);

    void visitWhile(While whileStatement);

    void visitReturn(Return ret);

    void visitFun(Fun fun);

    void visitFunCall(Call call);

    void visitBlock(Block block);

    void visitExpressionGroup(ExpressionGroup group);

    void visitVarDecl(VarDecl decl);

    void visitImport(Import importStatement);

    void visitBinOp(BinOp op);

    void visitLiteral(Literal literal);

    void visitName(QualifiedName name);

    void visitListDef(ListDef def);

    void visitListIdx(ListIndex idx);

    void visitClassDef(ClassDef def);

    void visitDictDef(DictDef def);

    void visitAnnotation(Annotation annotation);

    void visitAnnotationDef(AnnoDef def);

    void visitGo(Go go);

    void visitContinue();

    void visitBreak();
}
