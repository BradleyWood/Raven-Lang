package org.raven.antlr;


import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.raven.antlr.visitor.RavenFileVisitor;


import java.io.File;
import java.io.IOException;

public class RParser {

    private final String file;

    public RParser(final String file) {
        this.file = file;
    }

    public RavenTree parse() throws IOException {
        RavenLexer lexer = new RavenLexer(CharStreams.fromFileName(file));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        RavenParser parser = new RavenParser(tokenStream);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(new RavenErrorListener(file));
        RavenFileVisitor fileVisitor = new RavenFileVisitor();

        RavenParser.RavenFileContext ctx = parser.ravenFile();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            return null;
        }

        RavenTree tree = fileVisitor.visit(ctx);
        tree.setSourceFile(file);
        String name = new File(file).getName();
        tree.setName(name.replaceAll(".tl", ""));
        return tree;
    }
}
