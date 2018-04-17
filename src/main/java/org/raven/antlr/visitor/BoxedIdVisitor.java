package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.QualifiedName;

/**
 * For IDENTIFIERS that are wrapped in brackets '(' ID ')'
 */
public class BoxedIdVisitor extends RavenBaseVisitor<QualifiedName> {

    private BoxedIdVisitor() {
    }

    @Override
    public QualifiedName visitBoxedId(final RavenParser.BoxedIdContext ctx) {
        RavenParser.BoxedIdContext bid = ctx;
        while (bid.boxedId() != null) {
            bid = bid.boxedId();
        }
        return new QualifiedName(bid.IDENTIFIER().getText());
    }

    public static final BoxedIdVisitor INSTANCE = new BoxedIdVisitor();
}
