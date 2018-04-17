package org.raven.js;


import org.raven.antlr.RParser;
import org.raven.antlr.RavenTree;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TString;

import java.io.*;
import java.util.LinkedList;

/**
 * Javascript trans-compiler
 */
public class JSCompiler implements TreeVisitor {

    private int spaces = 4;

    private final RavenTree tree;

    private int indentationLevel = 0;

    private final LinkedList<Line> lines = new LinkedList<>();
    private final StringBuilder output = new StringBuilder();
    private StringBuilder line = new StringBuilder();

    private boolean compiled = false;

    public JSCompiler(final RavenTree tree) {
        this.tree = tree;
    }

    /**
     * Get the tree associated with this compilation
     *
     * @return The tree representing the program
     */
    public final RavenTree getTree() {
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
    public void setSpaces(final int spaces) {
        this.spaces = spaces;
    }

    public String getOutput() {
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

    public boolean save(final String file) {
        return save(new File(file));
    }

    public boolean save(final File file) {
        if (!file.getAbsoluteFile().getParentFile().exists()) {
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

    public void writeOutput(final OutputStream output) throws IOException {
        output.write(getOutput().getBytes());
    }

    public void compile() {
        tree.accept(this);
        compiled = true;
    }

    private void putSpaces(final int count) {
        for (int i = 0; i < count; i++) {
            line.append(' ');
        }
    }

    private void newLine(final Statement stmt) {
        // check whether the new line needs a semi colon
        if (!(stmt instanceof For) && !(stmt instanceof While) && !(stmt instanceof If)
                && !(stmt instanceof Fun)) {
            newLine(true);
        } else {
            newLine(false);
        }
    }

    private void newLine(final boolean b) {
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
    public void visitIf(final If ifStatement) {
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
            if (ifStatement.getElse() instanceof Block)
                line.append('{');
            newLine(ifStatement);
            beginIndent();
            ifStatement.getElse().accept(this);
            endIndent();
            if (ifStatement.getElse() instanceof Block)
                line.append('}');
        }
        newLine(ifStatement);
    }

    @Override
    public void visitFor(final For forStatement) {

    }

    @Override
    public void visitWhile(final While whileStatement) {
        line.append("while");
        putSpaces(1);
        line.append('(');
        whileStatement.getCondition().accept(this);
        line.append(')');
        putSpaces(1);
        line.append('{');
        newLine(whileStatement);
        whileStatement.getBody().accept(this);
        line.append('}');
    }

    @Override
    public void visitReturn(final Return ret) {
        line.append("return");
        if (ret.getValue() != null) {
            line.append(' ');
            ret.getValue().accept(this);
        }
    }

    @Override
    public void visitFun(final Fun fun) {
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
        line.append('}');
        newLine(fun);
        newLine(fun);
    }

    @Override
    public void visitFunCall(final Call call) {
        if (call.getPrecedingExpr() != null) {
            call.getPrecedingExpr().accept(this);
            line.append('.');
        }
        call.getName().accept(this);
        line.append('(');
        for (int i = 0; i < call.getParams().length; i++) {
            call.getParams()[i].accept(this);
            if (i + 1 < call.getParams().length) {
                line.append(',');
                putSpaces(1);
            }
        }
        line.append(')');

    }

    @Override
    public void visitBlock(final Block block) {
        block.getStatements().forEach(stmt -> {
            stmt.accept(this);
            newLine(stmt);
        });
    }

    @Override
    public void visitExpressionGroup(final ExpressionGroup group) {

    }

    @Override
    public void visitVarDecl(final VarDecl decl) {
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
    public void visitImport(final Import importStatement) {

    }

    @Override
    public void visitBinOp(final BinOp op) {
        op.getLeft().accept(this);
        putSpaces(1);
        line.append(op.getOp().op);
        putSpaces(1);
        op.getRight().accept(this);
    }

    @Override
    public void visitLiteral(final Literal literal) {
        if (literal.getValue() instanceof TString) {
            line.append("\"");
        }
        line.append(literal.getValue().toString());
        if (literal.getValue() instanceof TString) {
            line.append("\"");
        }
    }

    @Override
    public void visitName(final QualifiedName name) {
        line.append(name.toString());
    }

    @Override
    public void visitListDef(final ListDef def) {

    }

    @Override
    public void visitListIdx(final ListIndex idx) {

    }

    @Override
    public void visitClassDef(final ClassDef def) {
        throw new RuntimeException("Classes not allowed");
    }

    @Override
    public void visitDictDef(final DictDef def) {

    }

    @Override
    public void visitAnnotation(final Annotation annotation) {

    }

    @Override
    public void visitAnnotationDef(final AnnoDef def) {

    }

    @Override
    public void visitTryCatchFinally(final TryCatchFinally tcf) {

    }

    @Override
    public void visitRaise(final Raise raise) {

    }

    @Override
    public void visitGo(final Go go) {
        throw new RuntimeException("Go not allowed");
    }

    @Override
    public void visitContinue() {
        line.append("continue");
    }

    @Override
    public void visitBreak() {
        line.append("break");
    }

    private static class Line {
        String line;
        int level;

        public Line(final String line, final int level) {
            this.line = line;
            this.level = level;
        }
    }
    public static void main(final String[] args) throws IOException {
        RParser tp = new RParser("test/org/raven/test/IfTest.tl");
        RavenTree tree = tp.parse();
        JSCompiler compiler = new JSCompiler(tree);
        compiler.compile();
        compiler.save("gg.js");
    }
}
