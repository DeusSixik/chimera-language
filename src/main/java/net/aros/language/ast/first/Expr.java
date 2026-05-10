package net.aros.language.ast.first;

import net.aros.language.ast.Either;
import net.aros.language.ast.ScopeSpecifier;
import net.aros.language.ast.SourcePos;
import net.aros.language.ast.Modifier;
import net.aros.language.ast.ops.BinaryOp;
import net.aros.language.ast.ops.UnaryOp;

import java.util.List;
import java.util.Optional;

public sealed interface Expr extends Node {
    record LambdaExpr(List<ParameterExpr> parameters, Optional<String> returnType, Either<Stmt.BlockStmt, Expr> body, SourcePos pos) implements Expr {}
    record AssignExpr(AssignmentTarget target, Expr initializer, SourcePos pos) implements Expr {}
    record LiteralExpr(Object value, SourcePos pos) implements Expr {}
    record BinaryExpr(Expr left, BinaryOp op, Expr right, SourcePos pos) implements Expr {}
    record UnaryExpr(UnaryOp op, Expr expr, SourcePos pos) implements Expr {}
    record CallExpr(Expr callee, List<ArgumentExpr> args, SourcePos pos) implements Expr {}
    record MemberAccessExpr(Expr object, String member, SourcePos pos) implements Expr {}
    record VarExpr(ScopeSpecifier scopeSpecifier, String name, SourcePos pos) implements Expr {}
    record ParameterExpr(String name, Optional<String> type, Optional<Expr> defaultValue, SourcePos pos) implements Expr {}
    record ArgumentExpr(Optional<String> name, Expr value, SourcePos pos) implements Expr {}

    record AssignmentTarget(
            List<Modifier> modifiers,
            Expr target,
            Optional<String> type
    ) {
    }
}
