package net.aros.language.ast.first;

import net.aros.language.ast.SourcePos;

import java.util.List;
import java.util.Optional;

public sealed interface Stmt extends Node {
    record ExprStmt(Expr expr, SourcePos pos) implements Stmt {}
    record IfStmt(Expr cond, Stmt thenStmt, Optional<Stmt> elseStmt, SourcePos pos) implements Stmt {}
    record BlockStmt(List<Stmt> stmts, SourcePos pos) implements Stmt {}
    record WhileStmt(Expr cond, BlockStmt thenBlock, SourcePos pos) implements Stmt {}
    record DoWhileStmt(BlockStmt doBlock, Expr cond, SourcePos pos) implements Stmt {}
    record ForStmt(List<String> variables, Expr iterator, Stmt body, SourcePos pos) implements Stmt {}
    record ReturnStmt(Optional<Expr> expr, SourcePos pos) implements Stmt {}
}
