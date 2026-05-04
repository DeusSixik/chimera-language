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
    | exprStmt
    ;

exprStmt
    : expr
    ;

fnStmt
    : Fn Identifier LParen identifiersZeroOrMore RParen block
    ;

ifStmt
    : parenIfStmt
    | parenlessIfStmt
    ;

parenIfStmt
    : If LParen expr RParen block (Else block)?
    ;

parenlessIfStmt
    : If expr block (Else block)?
    ;

forStmt
    : parenForStmt
    | parenlessForStmt
    ;

parenForStmt
    : For LParen identifiersOneOrMore In expr RParen block
    ;

parenlessForStmt
    : For identifiersOneOrMore In expr block
    ;

doWhileStmt
    : parenDoWhileStmt
    | parenlessDoWhileStmt
    ;

parenDoWhileStmt
    : Do block While LParen expr RParen
    ;

parenlessDoWhileStmt
    : Do block While expr
    ;

whileStmt
    : parenWhileStmt
    | parenlessWhileStmt
    ;

parenWhileStmt
    : While LParen expr RParen block
    ;

parenlessWhileStmt
    : While expr block
    ;

block
    : LBrace stmt* RBrace
    ;

expr
    : assignment
    | lambda
    ;

assignment
    : Identifier (Colon Identifier)? assignmentOperator expr
    | logicalOr
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
    : Fn LParen identifiersZeroOrMore RParen block
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
    : primary (LParen exprsZeroOrMore RParen)*
    ;

identifiersZeroOrMore
    : (Identifier (Comma Identifier)*)?
    ;

identifiersOneOrMore
    : Identifier (Comma Identifier)*
    ;

exprsZeroOrMore
    : (expr (Comma expr)*)?
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
    | LParen expr RParen
    ;

listLiteral
    : LBracket exprsZeroOrMore RBracket
    ;

mapLiteral
    : LBrace ((expr Colon expr) (Comma (expr Colon expr))*)? RBrace
    ;
