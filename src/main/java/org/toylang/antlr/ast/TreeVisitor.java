package org.toylang.antlr.ast;


public interface TreeVisitor {

    void visitIf(If ifStatement);
    void visitWhile(While whileStatement);
    void visitReturn(Return ret);
    void visitFun(Fun fun);
    void visitFunCall(Call call);
    void visitBlock(Block block);
    void visitVarDecl(VarDecl decl);
    void visitImport(Import importStatement);

    void visitBinOp(BinOp op);
    void visitUnaryOp(UnaryOp op);
    void visitLiteral(Literal literal);
    void visitName(QualifiedName name);
    void visitListDef(ListDef def);
    void visitListIdx(ListIndex idx);
    void visitClassDef(ClassDef def);
    void visitDictDef(DictDef def);
    void visitAnnotation(Annotation annotation);
    void visitGo(Go go);
}
