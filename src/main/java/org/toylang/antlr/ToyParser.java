package org.toylang.antlr;


import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.toylang.antlr.visitor.ToyFileVisitor;
import org.toylang.antlr.ToyLangLexer;
import org.toylang.antlr.ToyLangParser;


import java.io.File;
import java.io.IOException;

public class ToyParser {

    private final String file;

    public ToyParser(final String file) {
        this.file = file;
    }

    public ToyTree parse() throws IOException {
        ToyLangLexer lexer = new ToyLangLexer(CharStreams.fromFileName(file));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ToyLangParser parser = new ToyLangParser(tokenStream);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(new ToyErrorListener(file));
        ToyFileVisitor fileVisitor = new ToyFileVisitor();
        ToyTree tree = fileVisitor.visit(parser.toyFile());
        tree.setSourceFile(file);
        String name = new File(file).getName();
        tree.setName(name.replaceAll(".tl", ""));
        return tree;
    }
}
