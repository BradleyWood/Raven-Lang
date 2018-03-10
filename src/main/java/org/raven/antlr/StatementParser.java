package org.raven.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.raven.antlr.ast.Statement;
import org.raven.antlr.visitor.ToyFileVisitor;

import java.util.List;

public class StatementParser {

    public static List<Statement> parseStatements(final String line) {
        RavenLexer lexer = new RavenLexer(CharStreams.fromString(line));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        org.raven.antlr.RavenParser parser = new org.raven.antlr.RavenParser(tokenStream);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(new ToyErrorListener("<stdin>"));

        ToyFileVisitor tfv = new ToyFileVisitor();
        return tfv.visitToyFile(parser.toyFile()).getStatements();
    }

}
