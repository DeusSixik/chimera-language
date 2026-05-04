package net.aros.language.ast.first;

import net.aros.language.ast.SourcePos;

import java.util.List;

public sealed interface Stmt extends Node {
    record ExprStmt(Expr expr, SourcePos pos) implements Stmt {}
    record IfStmt(Expr cond, BlockStmt thenBlock, BlockStmt elseBlock, SourcePos pos) implements Stmt {}
    record BlockStmt(List<Stmt> stmts, SourcePos pos) implements Stmt {}
    record WhileStmt(Expr cond, BlockStmt thenBlock, SourcePos pos) implements Stmt {}
    record DoWhileStmt(BlockStmt doBlock, Expr cond, SourcePos pos) implements Stmt {}
    record ForStmt(List<String> variables, Expr iterator, BlockStmt block, SourcePos pos) implements Stmt {}
    record FnStmt(String name, List<String> parameters, BlockStmt block, SourcePos pos) implements Stmt {}
}
