package net.aros.language.ast.first;

import net.aros.language.ast.SourcePos;
import net.aros.language.ast.ops.BinaryOp;
import net.aros.language.ast.ops.UnaryOp;

import java.util.List;

public sealed interface Expr extends Node {
    record LambdaExpr(List<String> args, Stmt.BlockStmt block, SourcePos pos) implements Expr {}
    record AssignExpr(String variable, String typeName, Expr initializer, SourcePos pos) implements Expr {}
    record LiteralExpr(Object value, SourcePos pos) implements Expr {}
    record BinaryExpr(Expr left, BinaryOp op, Expr right, SourcePos pos) implements Expr {}
    record UnaryExpr(UnaryOp op, Expr expr, SourcePos pos) implements Expr {}
    record CallExpr(Expr callee, List<Expr> args, SourcePos pos) implements Expr {}
    record VarExpr(String name, SourcePos pos) implements Expr {}
}
