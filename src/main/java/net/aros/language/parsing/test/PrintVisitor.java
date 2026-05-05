package net.aros.language.parsing.test;

import net.aros.language.ast.first.Expr;
import net.aros.language.ast.first.LangVisitor;
import net.aros.language.ast.first.Program;
import net.aros.language.ast.first.Stmt;

public class PrintVisitor implements LangVisitor<Void> {
    private final PosStringBuilder builder = new PosStringBuilder();

    @Override
    public Void visitProgram(Program program) {
        program.stmts().forEach(this::visit);
        return null;
    }

    @Override
    public Void visitExprStmt(Stmt.ExprStmt stmt) {
        visit(stmt.expr());
        builder.append("\n");
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.IfStmt stmt) {
        builder.append("if ");
        visit(stmt.cond());
        builder.append(" ");
        visit(stmt.thenStmt());
        builder.append("\n");
        stmt.elseStmt().ifPresent(elseStmt -> {
            builder.append("else ");
            visit(elseStmt);
        });
        return null;
    }

    @Override
    public Void visitBlockStmt(Stmt.BlockStmt stmt) {
        builder.append("{\n");
        stmt.stmts().forEach(this::visit);
        builder.append("}");
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.WhileStmt stmt) {
        builder.append("while");
        return null;
    }

    @Override
    public Void visitDoWhileStmt(Stmt.DoWhileStmt stmt) {
        builder.append("doWhile");
        return null;
    }

    @Override
    public Void visitForStmt(Stmt.ForStmt stmt) {
        builder.append("for " + String.join(", ", stmt.variables()) + " in ");
        visit(stmt.iterator());
        builder.append(" ");
        visit(stmt.body());
        return null;
    }

    @Override
    public Void visitFnStmt(Stmt.FnStmt stmt) {
        builder.append("fn " + stmt.name() + "(" + String.join(", ", stmt.parameters().stream().map(e -> "%s: %s = %s".formatted(
                e.name(), e.type().isEmpty() ? "null" : e.type().get(), e.defaultValue().isEmpty() ? "null" : "<expr>"
        )).toList()) + ") ");
        visit(stmt.body().isLeft() ? stmt.body().left().get() : stmt.body().right().get());

        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.ReturnStmt stmt) {
        builder.append("return ");
        stmt.expr().ifPresent(this::visit);
        builder.append(";\n");
        return null;
    }

    @Override
    public Void visitLambdaExpr(Expr.LambdaExpr expr) {
        builder.append("fn(" + String.join(", ", expr.parameters().stream().map(e -> "%s: %s = %s".formatted(
                e.name(), e.type().isEmpty() ? "null" : e.type().get(), e.defaultValue().isEmpty() ? "null" : "<expr>"
        )).toList()) + ") ");
        visit(expr.body().isLeft() ? expr.body().left().get() : expr.body().right().get());
        return null;
    }

    @Override
    public Void visitAssignExpr(Expr.AssignExpr expr) {
        builder.append(expr.variable() + ": " + (expr.typeName().isEmpty() ? "?" : expr.typeName().get()) + " = ");
        visit(expr.initializer());

        return null;
    }

    @Override
    public Void visitLiteralExpr(Expr.LiteralExpr expr) {
        builder.append(String.valueOf(expr.value()));

        return null;
    }

    @Override
    public Void visitBinaryExpr(Expr.BinaryExpr expr) {
        visit(expr.left());
        builder.append(expr.op().getValues().getFirst());
        visit(expr.right());

        return null;
    }

    @Override
    public Void visitUnaryExpr(Expr.UnaryExpr expr) {
        builder.append(expr.op().getValues().getFirst());
        visit(expr.expr());

        return null;
    }

    @Override
    public Void visitCallExpr(Expr.CallExpr expr) {
        visit(expr.callee());
        builder.append("(");
        for (Expr e : expr.args()) {
            visit(e);
            builder.append(",");
        }
        builder.append(")");

        return null;
    }

    @Override
    public Void visitVarExpr(Expr.VarExpr expr) {
        builder.append("`" + expr.name() + "`");

        return null;
    }

    @Override
    public Void visitArgumentExpr(Expr.ArgumentExpr expr) {
        builder.append(expr.name() + " = ");
        expr.value().ifPresent(this::visit);
        return null;
    }

    @Override
    public Void visitParameterExpr(Expr.ParameterExpr expr) {
        builder.append("param");
        return null;
    }

    public static class PosStringBuilder {
        private final StringBuilder builder = new StringBuilder();

        public PosStringBuilder append(Object object) {
            builder.append(object);
            return this;
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
