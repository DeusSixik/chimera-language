parser grammar LangParser;

@header {
package net.aros.language;
}

options { tokenVocab=LangLexer; }

program
    : stmt* EOF
    ;

stmt
    : ifStmt
    | fnStmt
    | doWhileStmt
    | whileStmt
    | forStmt
    | returnStmt
    | exprStmt
    ;

exprStmt
    : assignableExpr Semicolon
    ;

fnStmt
    : Fn Identifier LParen parameters RParen (block | (Assign expr))
    ;

returnStmt
    : Return assignableExpr? Semicolon
    ;

ifStmt
    : parenIfStmt
    | parenlessIfStmt
    ;

parenIfStmt
    : If LParen assignableExpr RParen blockOrStmt (Else blockOrStmt)?
    ;

parenlessIfStmt
    : If assignableExpr blockOrStmt (Else blockOrStmt)?
    ;

blockOrStmt
    : block
    | stmt
    ;

forStmt
    : parenForStmt
    | parenlessForStmt
    ;

parenForStmt
    : For LParen (Identifier (Comma Identifier)*) In assignableExpr RParen blockOrStmt
    ;

parenlessForStmt
    : For (Identifier (Comma Identifier)*) In assignableExpr blockOrStmt
    ;

doWhileStmt
    : parenDoWhileStmt
    | parenlessDoWhileStmt
    ;

parenDoWhileStmt
    : Do block While LParen assignableExpr RParen Semicolon
    ;

parenlessDoWhileStmt
    : Do block While assignableExpr Semicolon
    ;

whileStmt
    : parenWhileStmt
    | parenlessWhileStmt
    ;

parenWhileStmt
    : While LParen assignableExpr RParen block
    ;

parenlessWhileStmt
    : While assignableExpr block
    ;

block
    : LBrace stmt* RBrace
    ;

assignableExpr
    : assignment
    | expr
    ;

expr
    : lambda
    | logicalOr
    ;

assignment
    : Identifier (Colon Identifier)? assignmentOperator assignableExpr
    ;

assignmentOperator
    : Assign
    | PlusAssign
    | MinusAssign
    | MultiplyAssign
    | DivideAssign
    | ModuloAssign
    | BitAndAssign
    | BitOrAssign
    | BitXorAssign
    | ShiftLeftAssign
    | ShiftRightAssign
    | ShiftRightUnsignedAssign
    | LogicAndAssign
    | LogicOrAssign
    | LogicXorAssign
    ;

lambda
    : Fn LParen parameters RParen (block | (Assign expr))
    ;

logicalOr
    : logicalXor (LogicOr logicalXor)*
    ;

logicalXor
    : logicalAnd (LogicXor logicalAnd)*
    ;

logicalAnd
    : bitwiseOr (LogicAnd bitwiseOr)*
    ;

bitwiseOr
    : bitwiseXor (BitOr bitwiseXor)*
    ;

bitwiseXor
    : bitwiseAnd (BitXor bitwiseAnd)*
    ;

bitwiseAnd
    : equality (BitAnd equality)*
    ;

equality
    : comparison ((Equals | NotEquals) comparison)*
    ;

comparison
    : shift ((Less | LessEqual | Greater | GreaterEqual) shift)*
    ;

shift
    : term ((ShiftLeft | ShiftRight | ShiftRightUnsigned) term)*
    ;

term
    : factor ((Plus | Minus) factor)*
    ;

factor
    : unary ((Multiply | Divide | Modulo) unary)*
    ;

unary
    : (LogicNot | Plus | Minus | BitNot) unary
    | call
    ;

call
    : primary (Dot Identifier)? (LParen arguments RParen)*
    ;

arguments
    : (argument (Comma argument)*)?
    ;

argument
    : (Identifier Assign)? expr
    ;

parameters
    : (parameter (Comma parameter)*)?
    ;

parameter
    : Identifier (Colon Identifier)? (Assign expr)?
    ;

primary
    : IntLiteral
    | FloatLiteral
    | StringLiteral
    | listLiteral
    | mapLiteral
    | True
    | False
    | Null
    | Identifier
    | LParen assignableExpr RParen
    ;

listLiteral
    : LBracket (assignableExpr (Comma assignableExpr)*)? RBracket
    ;

mapLiteral
    : LBrace ((assignableExpr Colon assignableExpr) (Comma (assignableExpr Colon assignableExpr))*)? RBrace
    ;