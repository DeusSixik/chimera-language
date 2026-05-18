package net.aros.language.parsing;

import net.aros.language.LangParser;
import net.aros.language.LangParserBaseVisitor;
import net.aros.language.ast.Either;
import net.aros.language.ast.SourcePos;
import net.aros.language.ast.Modifier;
import net.aros.language.ast.first.Expr;
import net.aros.language.ast.first.Node;
import net.aros.language.ast.first.Program;
import net.aros.language.ast.first.Stmt;
import net.aros.language.ast.ops.AssignmentOp;
import net.aros.language.ast.ops.BinaryOp;
import net.aros.language.ast.ops.UnaryOp;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.IntFunction;

import static com.ibm.icu.impl.Utility.unescape;

public class Antlr2LangVisitor extends LangParserBaseVisitor<Node> {
    @Override
    public Node visitProgram(LangParser.ProgramContext ctx) {
        return new Program(ctx.stmt().stream().map(s -> (Stmt) visit(s)).toList());
    }

    @Override
    public Node visitStmt(LangParser.StmtContext ctx) {
        if (ctx.ifStmt() != null) return visit(ctx.ifStmt());
        if (ctx.doWhileStmt() != null) return visit(ctx.doWhileStmt());
        if (ctx.whileStmt() != null) return visit(ctx.whileStmt());
        if (ctx.forStmt() != null) return visit(ctx.forStmt());
        if (ctx.exprStmt() != null) return visit(ctx.exprStmt());
        if (ctx.fnStmt() != null) return visit(ctx.fnStmt());
        if (ctx.returnStmt() != null) return visit(ctx.returnStmt());
        throw new IllegalArgumentException("Unknown stmt");
    }

    @Override
    public Node visitFnStmt(LangParser.FnStmtContext ctx) {
        SourcePos pos = pos(ctx);
        return new Stmt.ExprStmt(new Expr.AssignExpr(
                ctx.modifier().stream().map(this::mod).toList(),
                new Expr.VarExpr(ctx.Identifier().getText(), pos),
                Optional.of(new Expr.TypeExpr.FunctionType(
                        ctx.parameters().parameter().stream().map(p -> ((Expr.ParameterExpr) visit(p)).type().orElse(new Expr.TypeExpr.IdentifierType("any", pos(p)))).toList(),
                        ctx.type() == null ? new Expr.TypeExpr.IdentifierType("any", pos(ctx.Colon().getSymbol())) : (Expr.TypeExpr) visit(ctx.type()),
                        pos(ctx)
                )),
                new Expr.LambdaExpr(
                        ctx.parameters().parameter().stream().map(p -> (Expr.ParameterExpr) visit(p)).toList(),
                        visitNullable(ctx.type(), Expr.TypeExpr.class),
                        ctx.block() == null ? Either.right((Expr) visit(ctx.expr())) : Either.left((Stmt.BlockStmt) visit(ctx.block())),
                        pos
                ),
                pos
        ), pos);
    }

    @Override
    public Node visitParameter(LangParser.ParameterContext ctx) {
        return new Expr.ParameterExpr(ctx.Identifier().getText(), visitNullable(ctx.type(), Expr.TypeExpr.class), visitNullable(ctx.expr(), Expr.class), pos(ctx));
    }

    @Override
    public Node visitReturnStmt(LangParser.ReturnStmtContext ctx) {
        return new Stmt.ReturnStmt(visitNullable(ctx.expr(), Expr.class), pos(ctx));
    }

    @Override
    public Node visitExprStmt(LangParser.ExprStmtContext ctx) {
        return new Stmt.ExprStmt((Expr) visit(ctx.expr()), pos(ctx));
    }

    @Override
    public Node visitIfStmt(LangParser.IfStmtContext ctx) {
        if (ctx.parenIfStmt() != null) return visit(ctx.parenIfStmt());
        if (ctx.parenlessIfStmt() != null) return visit(ctx.parenlessIfStmt());

        throw new IllegalArgumentException("Unknown if stmt");
    }

    @Override
    public Node visitParenIfStmt(LangParser.ParenIfStmtContext ctx) {
        return new Stmt.IfStmt((Expr) visit(ctx.expr()), (Stmt) visit(ctx.blockOrStmt(0)), visitNullable(ctx.blockOrStmt(1), Stmt.class), pos(ctx));
    }

    @Override
    public Node visitParenlessIfStmt(LangParser.ParenlessIfStmtContext ctx) {
        return new Stmt.IfStmt((Expr) visit(ctx.expr()), (Stmt) visit(ctx.blockOrStmt(0)), visitNullable(ctx.blockOrStmt(1), Stmt.class), pos(ctx));
    }

    @Override
    public Node visitForStmt(LangParser.ForStmtContext ctx) {
        if (ctx.parenForStmt() != null) return visit(ctx.parenForStmt());
        if (ctx.parenlessForStmt() != null) return visit(ctx.parenlessForStmt());

        throw new IllegalArgumentException("Unknown for stmt");
    }

    @Override
    public Node visitParenForStmt(LangParser.ParenForStmtContext ctx) {
        return new Stmt.ForStmt(
                ctx.Identifier().stream().map(TerminalNode::getText).toList(),
                (Expr) visit(ctx.expr()),
                (Stmt) visit(ctx.blockOrStmt()),
                pos(ctx)
        );
    }

    @Override
    public Node visitParenlessForStmt(LangParser.ParenlessForStmtContext ctx) {
        return new Stmt.ForStmt(
                ctx.Identifier().stream().map(TerminalNode::getText).toList(),
                (Expr) visit(ctx.expr()),
                (Stmt) visit(ctx.blockOrStmt()),
                pos(ctx)
        );
    }

    @Override
    public Node visitDoWhileStmt(LangParser.DoWhileStmtContext ctx) {
        if (ctx.parenDoWhileStmt() != null) return visit(ctx.parenDoWhileStmt());
        if (ctx.parenlessDoWhileStmt() != null) return visit(ctx.parenlessDoWhileStmt());

        throw new IllegalArgumentException("Unknown while stmt");
    }

    @Override
    public Node visitParenDoWhileStmt(LangParser.ParenDoWhileStmtContext ctx) {
        return new Stmt.DoWhileStmt((Stmt.BlockStmt) visit(ctx.block()), (Expr) visit(ctx.expr()), pos(ctx));
    }

    @Override
    public Node visitParenlessDoWhileStmt(LangParser.ParenlessDoWhileStmtContext ctx) {
        return new Stmt.DoWhileStmt((Stmt.BlockStmt) visit(ctx.block()), (Expr) visit(ctx.expr()), pos(ctx));
    }

    @Override
    public Node visitWhileStmt(LangParser.WhileStmtContext ctx) {
        if (ctx.parenWhileStmt() != null) return visit(ctx.parenWhileStmt());
        if (ctx.parenlessWhileStmt() != null) return visit(ctx.parenlessWhileStmt());

        throw new IllegalArgumentException("Unknown while stmt");
    }

    @Override
    public Node visitParenWhileStmt(LangParser.ParenWhileStmtContext ctx) {
        return new Stmt.WhileStmt((Expr) visit(ctx.expr()), (Stmt.BlockStmt) visit(ctx.block()), pos(ctx));
    }

    @Override
    public Node visitParenlessWhileStmt(LangParser.ParenlessWhileStmtContext ctx) {
        return new Stmt.WhileStmt((Expr) visit(ctx.expr()), (Stmt.BlockStmt) visit(ctx.block()), pos(ctx));
    }

    @Override
    public Node visitBlock(LangParser.BlockContext ctx) {
        return new Stmt.BlockStmt(ctx.stmt().stream().map(s -> (Stmt) visit(s)).toList(), pos(ctx));
    }

    @Override
    public Node visitLambda(LangParser.LambdaContext ctx) {
        return new Expr.LambdaExpr(
                ctx.parameters().parameter().stream().map(p -> (Expr.ParameterExpr) visit(p)).toList(),
                visitNullable(ctx.type(), Expr.TypeExpr.class),
                ctx.block() == null ? Either.right((Expr) visit(ctx.expr())) : Either.left((Stmt.BlockStmt) visit(ctx.block())), pos(ctx)
        );
    }

    @Override
    public Node visitAssignment(LangParser.AssignmentContext ctx) {
        if (ctx.logicalOr() != null) return visit(ctx.logicalOr());

        Expr target = buildPostfixExpr((Expr) visit(ctx.primary()), ctx.postfix());
        Expr initializer = (Expr) visit(ctx.assignment());
        BinaryOp op = AssignmentOp.opByValue(ctx.assignmentOperator().getText());
        if (op != null) {
            initializer = new Expr.BinaryExpr(target, op, initializer, pos(ctx));
        }

        return new Expr.AssignExpr(
                ctx.modifier().stream().map(this::mod).toList(),
                target,
                visitNullable(ctx.type(), Expr.TypeExpr.class),
                initializer,
                pos(ctx)
        );
    }

    private Expr buildPostfixExpr(Expr expr, List<LangParser.PostfixContext> postfixes) {
        for (LangParser.PostfixContext postfix : postfixes) {
            if (postfix.arguments() != null)
                expr = new Expr.CallExpr(expr, postfix.arguments().argument().stream().map(arg -> (Expr.ArgumentExpr) visit(arg)).toList(), pos(postfix));
            else
                expr = new Expr.MemberAccessExpr(expr, postfix.Identifier().getText(), pos(postfix));
        }
        return expr;
    }

    private Modifier mod(LangParser.ModifierContext mod) {
        if (mod.Const() != null) return Modifier.CONST;
        if (mod.At() != null) return Modifier.STATIC;
        throw new IllegalArgumentException("Unknown mod");
    }

    @Override
    public Node visitNamedArgument(LangParser.NamedArgumentContext ctx) {
        return new Expr.ArgumentExpr(Optional.of(ctx.Identifier().getText()), (Expr) visit(ctx.expr()), pos(ctx));
    }

    @Override
    public Node visitPositionalArgument(LangParser.PositionalArgumentContext ctx) {
        return new Expr.ArgumentExpr(Optional.empty(), (Expr) visit(ctx.expr()), pos(ctx));
    }

    @Override
    public Node visitLogicalOr(LangParser.LogicalOrContext ctx) {
        return leftAssociative(ctx, ctx.logicalXor().size(), ctx::logicalXor);
    }

    @Override
    public Node visitLogicalXor(LangParser.LogicalXorContext ctx) {
        return leftAssociative(ctx, ctx.logicalAnd().size(), ctx::logicalAnd);
    }

    @Override
    public Node visitLogicalAnd(LangParser.LogicalAndContext ctx) {
        return leftAssociative(ctx, ctx.bitwiseOr().size(), ctx::bitwiseOr);
    }

    @Override
    public Node visitBitwiseOr(LangParser.BitwiseOrContext ctx) {
        return leftAssociative(ctx, ctx.bitwiseXor().size(), ctx::bitwiseXor);
    }

    @Override
    public Node visitBitwiseXor(LangParser.BitwiseXorContext ctx) {
        return leftAssociative(ctx, ctx.bitwiseAnd().size(), ctx::bitwiseAnd);
    }

    @Override
    public Node visitBitwiseAnd(LangParser.BitwiseAndContext ctx) {
        return leftAssociative(ctx, ctx.equality().size(), ctx::equality);
    }

    @Override
    public Node visitEquality(LangParser.EqualityContext ctx) {
        return leftAssociative(ctx, ctx.comparison().size(), ctx::comparison);
    }

    @Override
    public Node visitComparison(LangParser.ComparisonContext ctx) {
        return leftAssociative(ctx, ctx.shift().size(), ctx::shift);
    }

    @Override
    public Node visitShift(LangParser.ShiftContext ctx) {
        return leftAssociative(ctx, ctx.term().size(), ctx::term);
    }

    @Override
    public Node visitTerm(LangParser.TermContext ctx) {
        return leftAssociative(ctx, ctx.factor().size(), ctx::factor);
    }

    @Override
    public Node visitFactor(LangParser.FactorContext ctx) {
        return leftAssociative(ctx, ctx.unary().size(), ctx::unary);
    }

    @Override
    public Node visitUnary(LangParser.UnaryContext ctx) {
        if (ctx.call() != null) return visit(ctx.call());
        var op = UnaryOp.byValue(ctx.getChild(0).getText());
        Expr right = (Expr) visit(ctx.unary());
        return new Expr.UnaryExpr(op.orElseThrow(), right, pos(ctx));
    }

    @Override
    public Node visitCall(LangParser.CallContext ctx) {
        return buildPostfixExpr((Expr) visit(ctx.primary()), ctx.postfix());
    }

    @Override
    public Node visitListLiteral(LangParser.ListLiteralContext ctx) {
        return new Expr.LiteralExpr(ctx.expr().stream().map(this::visit).toList(), pos(ctx));
    }

    @Override
    public Node visitMapLiteral(LangParser.MapLiteralContext ctx) {
        Map<Node, Node> map = new HashMap<>();
        for (int i = 0; i < ctx.Colon().size(); i++) {
            map.put(visit(ctx.expr(i)), visit(ctx.expr(i + 1)));
        }
        return new Expr.LiteralExpr(Map.copyOf(map), pos(ctx));
    }

    @Override
    public Node visitPrimary(LangParser.PrimaryContext ctx) {
        if (ctx.IntLiteral() != null) return new Expr.LiteralExpr(new BigInteger(ctx.IntLiteral().getText()), pos(ctx));
        if (ctx.FloatLiteral() != null)
            return new Expr.LiteralExpr(new BigDecimal(ctx.FloatLiteral().getText()), pos(ctx));
        if (ctx.StringLiteral() != null) {
            String raw = ctx.getText();
            return new Expr.LiteralExpr(unescape(raw.substring(1, raw.length() - 1)), pos(ctx));
        }
        if (ctx.listLiteral() != null) return visit(ctx.listLiteral());
        if (ctx.mapLiteral() != null) return visit(ctx.mapLiteral());
        if (ctx.True() != null) return new Expr.LiteralExpr(true, pos(ctx));
        if (ctx.False() != null) return new Expr.LiteralExpr(false, pos(ctx));
        if (ctx.Null() != null) return new Expr.LiteralExpr(null, pos(ctx));
        if (ctx.Identifier() != null) return new Expr.VarExpr(
                ctx.Identifier().getText(),
                pos(ctx)
        );
        if (ctx.expr() != null) return visit(ctx.expr());

        throw new IllegalArgumentException("Unknown primary");
    }

    @Override
    public Node visitArgument(LangParser.ArgumentContext ctx) {
        if (ctx.positionalArgument() != null) return visit(ctx.positionalArgument());
        if (ctx.namedArgument() != null) return visit(ctx.namedArgument());
        throw new IllegalArgumentException("Unknown argument");
    }

    @Override
    public Node visitExpr(LangParser.ExprContext ctx) {
        if (ctx.assignment() != null) return visit(ctx.assignment());
        if (ctx.lambda() != null) return visit(ctx.lambda());

        throw new IllegalArgumentException("Unknown expr");
    }

    @Override
    public Node visitBlockOrStmt(LangParser.BlockOrStmtContext ctx) {
        if (ctx.block() != null) return visit(ctx.block());
        return visit(ctx.stmt());
    }

    private static SourcePos pos(ParserRuleContext ctx) {
        return pos(ctx.getStart());
    }

    private static SourcePos pos(Token symbol) {
        return new SourcePos(symbol.getLine(), symbol.getCharPositionInLine());
    }

    private Expr leftAssociative(ParserRuleContext ctx, int size, IntFunction<ParseTree> exprGetter) {
        Expr expr = (Expr) visit(exprGetter.apply(0));
        for (int i = 1; i < size; i++) {
            var op = BinaryOp.byValue(ctx.getChild(2 * i - 1).getText()).orElseThrow();
            Expr right = (Expr) visit(exprGetter.apply(i));
            expr = new Expr.BinaryExpr(expr, op, right, pos(ctx));
        }
        return expr;
    }

    private <T extends Node> Optional<T> visitNullable(ParseTree parseTree, Class<T> type) {
        if (parseTree == null) return Optional.empty();
        return Optional.of(type.cast(visit(parseTree)));
    }

    // TYPES

    @Override
    public Node visitType(LangParser.TypeContext ctx) {
        return visit(ctx.unionType());
    }

    @Override
    public Node visitUnionType(LangParser.UnionTypeContext ctx) {
        if (ctx.intersectionType().size() == 1)
            return visit(ctx.intersectionType(0));
        return new Expr.TypeExpr.UnionTypeExpr(ctx.intersectionType().stream().map(intersection -> (Expr.TypeExpr) visit(intersection)).toList(), pos(ctx));
    }

    @Override
    public Node visitIntersectionType(LangParser.IntersectionTypeContext ctx) {
        if (ctx.postfixType().size() == 1)
            return visit(ctx.postfixType(0));
        return new Expr.TypeExpr.IntersectionTypeExpr(ctx.postfixType().stream().map(intersection -> (Expr.TypeExpr) visit(intersection)).toList(), pos(ctx));
    }

    @Override
    public Node visitPostfixType(LangParser.PostfixTypeContext ctx) {
        Expr.TypeExpr type = (Expr.TypeExpr) visit(ctx.primaryType());
        return ctx.QuestionMark() != null ? new Expr.TypeExpr.NullableTypeExpr(type, pos(ctx)) : type;
    }

    @Override
    public Node visitPrimaryType(LangParser.PrimaryTypeContext ctx) {
        if (ctx.Identifier() != null) return new Expr.TypeExpr.IdentifierType(ctx.Identifier().getText(), pos(ctx));
        if (ctx.tupleType() != null) return visit(ctx.tupleType());
        if (ctx.listType() != null) return visit(ctx.listType());
        if (ctx.mapType() != null) return visit(ctx.mapType());
        if (ctx.functionType() != null) return visit(ctx.functionType());
        return visit(ctx.type());
    }

    @Override
    public Node visitTupleType(LangParser.TupleTypeContext ctx) {
        return new Expr.TypeExpr.TupleTypeExpr(ctx.type().stream().map(t -> (Expr.TypeExpr) visit(t)).toList(), pos(ctx));
    }

    @Override
    public Node visitListType(LangParser.ListTypeContext ctx) {
        return new Expr.TypeExpr.ListTypeExpr((Expr.TypeExpr) visit(ctx.type()), pos(ctx));
    }

    @Override
    public Node visitMapType(LangParser.MapTypeContext ctx) {
        return new Expr.TypeExpr.MapTypeExpr((Expr.TypeExpr) visit(ctx.type(0)), (Expr.TypeExpr) visit(ctx.type(1)), pos(ctx));
    }

    @Override
    public Node visitFunctionType(LangParser.FunctionTypeContext ctx) {
        int size = ctx.type().size();
        List<Expr.TypeExpr> types = ctx.type().stream().map(t -> (Expr.TypeExpr) visit(t)).toList();
        return new Expr.TypeExpr.FunctionType(
                size == 1 ? List.of() : types.subList(0, size - 2),
                types.getLast(),
                pos(ctx)
        );
    }
}
