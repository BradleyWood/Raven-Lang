package org.toylang.js;


import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.*;

import java.io.*;
import java.util.LinkedList;

/**
 * Javascript trans-compiler
 */
public class JSCompiler implements TreeVisitor {

    private int spaces = 4;

    private final ToyTree tree;

    private int indentationLevel = 0;

    private final LinkedList<Line> lines = new LinkedList<>();
    private final StringBuilder output = new StringBuilder();
    private StringBuilder line = new StringBuilder();

    private boolean compiled = false;

    public JSCompiler(final ToyTree tree) {
        this.tree = tree;
    }

    /**
     * Get the tree associated with this compilation
     *
     * @return The tree representing the program
     */
    public final ToyTree getTree() {
        return tree;
    }

    /**
     * Get the number of spaces used for indentation
     *
     * @return The number of spaces
     */
    public int getSpaces() {
        return spaces;
    }

    /**
     * Sets the number of spaces used for indentation
     *
     * @param spaces Number of spaces to use
     */
    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }

    public String getOutput() throws IOException {
        if (!compiled) {
            compile();
        }
        if (output.length() > 0) {
            return output.toString();
        }

        for (Line line : lines) {
            if (line.line.length() == 0) {
                output.append('\n');
                continue;
            }
            for (int i = 0; i < spaces * line.level; i++) {
                output.append(' ');
            }
            output.append(line.line);
            output.append('\n');
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

    private void putSpaces(int count) {
        for (int i = 0; i < count; i++) {
            line.append(' ');
        }
    }

    private void newLine(Statement stmt) {
        // check whether the new line needs a semi colon
        if (!(stmt instanceof For) && !(stmt instanceof While) && !(stmt instanceof If)
                && !(stmt instanceof Fun)) {
            newLine(true);
        } else {
            newLine(false);
        }
    }

    private void newLine(boolean b) {
        if (b) {
            line.append(';');
        }
        lines.add(new Line(line.toString(), indentationLevel));
        line = new StringBuilder();
    }

    private void beginIndent() {
        indentationLevel++;
    }

    private void endIndent() {
        indentationLevel--;
    }

    @Override
    public void visitIf(If ifStatement) {
        line.append("if");
        putSpaces(1);
        line.append('(');
        ifStatement.getCondition().accept(this);
        line.append(')');
        putSpaces(1);
        line.append('{');
        newLine(ifStatement);
        beginIndent();
        ifStatement.getBody().accept(this);
        endIndent();
        line.append('}');
        if (ifStatement.getElse() != null) {
            putSpaces(1);
            line.append("else");
            putSpaces(1);
            line.append("{");
            ifStatement.getElse().accept(this);
            line.append("}");
        }
        newLine(ifStatement);
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
        line.append("function");
        putSpaces(1);
        fun.getName().accept(this);
        line.append('(');

        for (int i = 0; i < fun.getParams().length; i++) {
            line.append(fun.getParams()[i]);
            if (i + 1 < fun.getParams().length) {
                line.append(',');
                putSpaces(1);
            }
        }

        line.append(')');
        putSpaces(1);
        line.append('{');
        newLine(fun);
        beginIndent();
        fun.getBody().accept(this);
        endIndent();

        newLine(fun);
    }

    @Override
    public void visitFunCall(Call call) {

    }

    @Override
    public void visitBlock(Block block) {
        block.getStatements().forEach(stmt -> {
            stmt.accept(this);
            newLine(stmt);
        });
    }

    @Override
    public void visitVarDecl(VarDecl decl) {
        line.append("var");
        putSpaces(1);
        decl.getName().accept(this);
        if (decl.getInitialValue() != null) {
            putSpaces(1);
            line.append('=');
            putSpaces(1);
            decl.getInitialValue().accept(this);
        }
    }

    @Override
    public void visitImport(Import importStatement) {

    }

    @Override
    public void visitBinOp(BinOp op) {
        op.getLeft().accept(this);
        putSpaces(1);
        line.append(op.getOp().op);
        putSpaces(1);
        op.getRight().accept(this);
    }

    @Override
    public void visitUnaryOp(UnaryOp op) {

    }

    @Override
    public void visitLiteral(Literal literal) {
        line.append(literal.getValue().toString());
    }

    @Override
    public void visitName(QualifiedName name) {
        line.append(name.toString());
    }

    @Override
    public void visitListDef(ListDef def) {

    }

    @Override
    public void visitListIdx(ListIndex idx) {

    }

    @Override
    public void visitClassDef(ClassDef def) {
        throw new RuntimeException("Classes not allowed");
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

    private static class Line {
        String line;
        int level;

        public Line(String line, int level) {
            this.line = line;
            this.level = level;
        }
    }
}
