package net.aros.language.ast.first;

public interface LangVisitor<T> {
    T visitProgram(Program program);
    T visitExprStmt(Stmt.ExprStmt stmt);
    T visitIfStmt(Stmt.IfStmt stmt);
    T visitBlockStmt(Stmt.BlockStmt stmt);
    T visitWhileStmt(Stmt.WhileStmt stmt);
    T visitDoWhileStmt(Stmt.DoWhileStmt stmt);
    T visitForStmt(Stmt.ForStmt stmt);
//    T visitFnStmt(Stmt.FnStmt stmt);
    T visitReturnStmt(Stmt.ReturnStmt stmt);
    T visitLambdaExpr(Expr.LambdaExpr expr);
    T visitAssignExpr(Expr.AssignExpr expr);
    T visitLiteralExpr(Expr.LiteralExpr expr);
    T visitBinaryExpr(Expr.BinaryExpr expr);
    T visitUnaryExpr(Expr.UnaryExpr expr);
    T visitCallExpr(Expr.CallExpr expr);
    T visitMemberAccessExpr(Expr.MemberAccessExpr expr);
    T visitVarExpr(Expr.VarExpr expr);
    T visitParameterExpr(Expr.ParameterExpr expr);
    T visitArgumentExpr(Expr.ArgumentExpr expr);

    default T visit(Node node) {
        return switch (node) {
            case Expr.LambdaExpr expr -> visitLambdaExpr(expr);
            case Expr.AssignExpr expr -> visitAssignExpr(expr);
            case Expr.LiteralExpr expr -> visitLiteralExpr(expr);
            case Expr.BinaryExpr expr -> visitBinaryExpr(expr);
            case Expr.UnaryExpr expr -> visitUnaryExpr(expr);
            case Expr.CallExpr expr -> visitCallExpr(expr);
            case Expr.MemberAccessExpr expr -> visitMemberAccessExpr(expr);
            case Expr.VarExpr expr -> visitVarExpr(expr);
            case Expr.ParameterExpr expr -> visitParameterExpr(expr);
            case Expr.ArgumentExpr expr -> visitArgumentExpr(expr);
            case Stmt.ExprStmt stmt -> visitExprStmt(stmt);
            case Stmt.IfStmt stmt -> visitIfStmt(stmt);
            case Stmt.BlockStmt stmt -> visitBlockStmt(stmt);
            case Stmt.WhileStmt stmt -> visitWhileStmt(stmt);
            case Stmt.DoWhileStmt stmt -> visitDoWhileStmt(stmt);
            case Stmt.ForStmt stmt -> visitForStmt(stmt);
//            case Stmt.FnStmt stmt -> visitFnStmt(stmt);
            case Stmt.ReturnStmt stmt -> visitReturnStmt(stmt);
            case Program program -> visitProgram(program);
        };
    }
}
