package org.toylang.js;


import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.*;

import java.io.*;

/**
 * Javascript trans-compiler
 */
public class JSCompiler implements TreeVisitor {

    private int spaces = 4;

    private final ToyTree tree;

    private int indentationLevel = 0;

    private final StringBuilder output = new StringBuilder();

    private boolean compiled = false;

    public JSCompiler(final ToyTree tree) {
        this.tree = tree;
    }

    /**
     * Get the tree associated with this compilation
     * @return The tree representing the program
     */
    public final ToyTree getTree() {
        return tree;
    }

    /**
     * Get the number of spaces used for indentation
     * @return The number of spaces
     */
    public int getSpaces() {
        return spaces;
    }

    /**
     * Sets the number of spaces used for indentation
     * @param spaces Number of spaces to use
     */
    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }

    public String getOutput() {
        if (!compiled) {
            compile();
        }
        return output.toString();
    }

    public boolean save(String file) {
        return save(new File(file));
    }

    public boolean save(File file) {
        if (!file.getParentFile().exists()) {
            boolean success = file.getParentFile().mkdirs();
            if (!success) {
                return false;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            writeOutput(fos);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void writeOutput(OutputStream output) throws IOException {
        output.write(getOutput().getBytes());
    }

    public void compile() {

        compiled = true;
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
