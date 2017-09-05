package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Import;
import org.toylang.antlr.ast.QualifiedName;

public class ImportVisitor extends ToyLangBaseVisitor<Import> {

    private ImportVisitor(){}

    @Override
    public Import visitImportStatement(ToyLangParser.ImportStatementContext ctx) {
        QualifiedName name = null;
        if(ctx.qualifiedName() != null)
            name = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
        return new Import(name);
    }

    public static final ImportVisitor INSTANCE = new ImportVisitor();
}
