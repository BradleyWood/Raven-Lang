package org.raven.antlr;

import org.raven.antlr.ast.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Node {

    private Node parent;

    public abstract void accept(TreeVisitor tree);

    public Node getParent() {
        return parent;
    }

    public void setParent(final Node parent) {
        this.parent = parent;
    }

    public <T extends Node> T getParentByType(final Class<T> type) {
        Node parent = getParent();
        while (parent != null) {
            if (type.isAssignableFrom(parent.getClass()))
                return (T) parent;

            parent = parent.getParent();
        }

        return null;
    }

    public <T extends Node> boolean hasChildOfType(final Class<T> type) {
        return !getChildrenByType(type).isEmpty();
    }

    public <T extends Node> List<T> getChildrenByType(final Class<T> type) {
        final ChildVisitor visitor = new ChildVisitor();
        accept(visitor);
        return visitor.children.stream().filter(c -> type.isAssignableFrom(c.getClass())).map(a -> (T) a)
                .collect(Collectors.toList());
    }

    private class ChildVisitor implements TreeVisitor {

        private final LinkedList<Node> children = new LinkedList<>();

        @Override
        public void visitIf(final If stmt) {
            children.add(stmt);

            stmt.getCondition().accept(this);
            stmt.getBody().accept(this);

            if (stmt.getElse() != null) {
                stmt.getElse().accept(this);
            }
        }

        @Override
        public void visitFor(final For stmt) {
            children.add(stmt);

            stmt.getInit().accept(this);
            stmt.getCondition().accept(this);
            stmt.getAfter().accept(this);
            stmt.getBody().accept(this);
        }

        @Override
        public void visitWhile(final While stmt) {
            children.add(stmt);

            stmt.getCondition().accept(this);
            stmt.getBody().accept(this);
        }

        @Override
        public void visitReturn(final Return stmt) {
            children.add(stmt);

            if (stmt.getValue() != null) {
                stmt.getValue().accept(this);
            }
        }

        @Override
        public void visitConstructor(final Constructor stmt) {
            children.add(stmt);
            if (stmt.getInitBlock() != null) {
                stmt.getInitBlock().accept(this);
            }
            if (stmt.getBody() != null) {
                stmt.getBody().accept(this);
            }
        }

        @Override
        public void visitFun(final Fun stmt) {
            children.add(stmt);

            stmt.getName().accept(this);
            for (final VarDecl varDecl : stmt.getParams()) {
                varDecl.accept(this);
            }
            stmt.getBody().accept(this);

            for (final Annotation annotation : stmt.getAnnotations()) {
                annotation.accept(this);
            }
        }

        @Override
        public void visitFunCall(final Call stmt) {
            children.add(stmt);

            stmt.getName().accept(this);
            if (stmt.getPrecedingExpr() != null) {
                stmt.getPrecedingExpr().accept(this);
            }

            if (stmt.getParams() != null) {
                for (final Expression expression : stmt.getParams()) {
                    expression.accept(this);
                }
            }
        }

        @Override
        public void visitBlock(final Block stmt) {
            children.add(stmt);

            for (final Statement statement : stmt.getStatements()) {
                statement.accept(this);
            }
        }

        @Override
        public void visitExpressionGroup(final ExpressionGroup stmt) {
            children.add(stmt);
        }

        @Override
        public void visitVarDecl(final VarDecl stmt) {
            children.add(stmt);
            stmt.getName().accept(this);
            if (stmt.getInitialValue() != null) {
                stmt.getInitialValue().accept(this);
            }

            for (final Annotation annotation : stmt.getAnnotations()) {
                annotation.accept(this);
            }
        }

        @Override
        public void visitImport(final Import stmt) {
            children.add(stmt);
            stmt.getName().accept(this);
        }

        @Override
        public void visitBinOp(final BinOp stmt) {
            children.add(stmt);
            if (stmt.getLeft() != null) {
                stmt.getLeft().accept(this);
            }
            if (stmt.getRight() != null) {
                stmt.getRight().accept(this);
            }
        }

        @Override
        public void visitLiteral(final Literal stmt) {
            children.add(stmt);
        }

        @Override
        public void visitName(final QualifiedName stmt) {
            children.add(stmt);
        }

        @Override
        public void visitListDef(final ListDef stmt) {
            children.add(stmt);

            for (final Expression expression : stmt.getExpressions()) {
                expression.accept(this);
            }
        }

        @Override
        public void visitListIdx(final ListIndex stmt) {
            children.add(stmt);

            if (stmt.getPrecedingExpr() != null) {
                stmt.getPrecedingExpr().accept(this);
            }

            for (final Expression expression : stmt.getIndex()) {
                expression.accept(this);
            }
        }

        @Override
        public void visitClassDef(final ClassDef stmt) {
            children.add(stmt);

            if (stmt.getSuper() != null) {
                stmt.getSuper().accept(this);
            }

            if (stmt.getInterfaces() != null) {
                for (final QualifiedName qualifiedName : stmt.getInterfaces()) {
                    qualifiedName.accept(this);
                }
            }

            for (final Constructor constructor : stmt.getConstructors()) {
                constructor.accept(this);
            }

            for (final Statement statement : stmt.getStatements()) {
                statement.accept(this);
            }

            stmt.getPackage().accept(this);

            for (final Annotation annotation : stmt.getAnnotations()) {
                annotation.accept(this);
            }
        }

        @Override
        public void visitDictDef(final DictDef stmt) {
            children.add(stmt);
            for (final Expression expression : stmt.getKeys()) {
                expression.accept(this);
            }
            for (final Expression expression : stmt.getValues()) {
                expression.accept(this);
            }
        }

        @Override
        public void visitAnnotation(final Annotation stmt) {
            children.add(stmt);
            for (final QualifiedName qualifiedName : stmt.getKeys()) {
                qualifiedName.accept(this);
            }
            for (final Literal literal : stmt.getValues()) {
                literal.accept(this);
            }
        }

        @Override
        public void visitAnnotationDef(final AnnoDef stmt) {
            children.add(stmt);
            stmt.getName().accept(this);
            for (final QualifiedName qualifiedName : stmt.getParamNames()) {
                qualifiedName.accept(this);
            }
        }

        @Override
        public void visitTryCatchFinally(final TryCatchFinally stmt) {
            children.add(stmt);
            if (stmt.getBody() != null) {
                stmt.getBody().accept(this);
            }
            if (stmt.getHandler() != null) {
                stmt.getHandler().accept(this);
            }
            if (stmt.getFinallyBlock() != null) {
                stmt.getFinallyBlock().accept(this);
            }
            if (stmt.getExceptionName() != null) {
                stmt.getExceptionName().accept(this);
            }
        }

        @Override
        public void visitTernaryOp(final TernaryOp stmt) {
            children.add(stmt);
            stmt.getCondition().accept(this);
            stmt.getLhs().accept(this);
            stmt.getRhs().accept(this);
        }

        @Override
        public void visitWhen(final When stmt) {
            children.add(stmt);
            stmt.getCondition().accept(this);
            for (final Case aCase : stmt.getCases()) {
                aCase.getCaseExpr().accept(this);
                aCase.getBlock().accept(this);
            }
            stmt.getElseCase().accept(this);
        }

        @Override
        public void visitRaise(final Raise stmt) {
            children.add(stmt);
            stmt.getExpression().accept(this);
        }

        @Override
        public void visitDefer(final Defer stmt) {
            children.add(stmt);
            stmt.getCall().accept(this);
        }

        @Override
        public void visitGo(final Go stmt) {
            children.add(stmt);
            stmt.getGoFun().accept(this);
        }

        @Override
        public void visitContinue() {
            children.add(new Continue());
        }

        @Override
        public void visitBreak() {
            children.add(new Break());
        }
    }
}
