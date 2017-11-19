package org.toylang.antlr;

import org.toylang.antlr.ast.*;
import org.toylang.compiler.SymbolMap;

public class Mapper implements TreeVisitor {

    private ToyTree tree;

    public Mapper(ToyTree tree) {
        this.tree = tree;
    }

    public void map() {
        tree.accept(this);
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
        String name = tree.getName() + "." + fun.getName();
        if (tree.getPackage() != null) {
            name = tree.getPackage().add(tree.getName()).add(fun.getName()).toString();
        }
        if (fun.getModifiers() != null) {
            for (Modifier modifier : fun.getModifiers()) {
                if (modifier == Modifier.PUBLIC) {
                    SymbolMap.FUN_MAP.put(name, fun);
                    return;
                }
            }
        }
        SymbolMap.addLocalFun(name, fun);
    }

    @Override
    public void visitFunCall(Call call) {
    }

    @Override
    public void visitBlock(Block block) {
    }

    @Override
    public void visitVarDecl(VarDecl decl) {
        if (decl.getModifiers() != null) {
            for (Modifier modifier : decl.getModifiers()) {
                if (modifier.equals(Modifier.PUBLIC)) {
                    SymbolMap.VARIABLE_MAP.put(tree.getPackage().add(tree.getName()).add(decl.getName()).toString(), decl);
                    break;
                }
            }
        }
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
        if (def.getModifiers() != null) {
            for (Modifier modifier : def.getModifiers()) {
                if (modifier.equals(Modifier.PUBLIC)) {
                    SymbolMap.CLASS_MAP.put(tree.getPackage().add(def.getName()).toString(), def);
                }
            }
        }
    }

    @Override
    public void visitDictDef(DictDef def) {

    }

    @Override
    public void visitAnnotation(Annotation annotation) {

    }

    @Override
    public void visitGo(Go go) {

    }

    @Override
    public void visitContinue() {

    }

    @Override
    public void visitBreak() {

    }
}
