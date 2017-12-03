package org.toylang.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.toylang.antlr.ast.Statement;
import org.toylang.antlr.ToyLangLexer;
import org.toylang.antlr.visitor.ToyFileVisitor;

import java.util.List;

public class StatementParser {

    public static List<Statement> parseStatements(final String line) {
        ToyLangLexer lexer = new ToyLangLexer(CharStreams.fromString(line));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        org.toylang.antlr.ToyLangParser parser = new org.toylang.antlr.ToyLangParser(tokenStream);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(new ToyErrorListener("REPL"));

        ToyFileVisitor tfv = new ToyFileVisitor();
        return tfv.visitToyFile(parser.toyFile()).getStatements();
    }

}
