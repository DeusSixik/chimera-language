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
    : expr Semicolon
    ;

fnStmt
    : modifier* Fn Identifier LParen parameters RParen (Colon type)? (block | (Assign expr))
    ;

returnStmt
    : Return expr? Semicolon
    ;

ifStmt
    : parenIfStmt
    | parenlessIfStmt
    ;

parenIfStmt
    : If LParen expr RParen blockOrStmt (Else blockOrStmt)?
    ;

parenlessIfStmt
    : If expr blockOrStmt (Else blockOrStmt)?
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
    : For LParen (Identifier (Comma Identifier)*) In expr RParen blockOrStmt
    ;

parenlessForStmt
    : For (Identifier (Comma Identifier)*) In expr blockOrStmt
    ;

doWhileStmt
    : parenDoWhileStmt
    | parenlessDoWhileStmt
    ;

parenDoWhileStmt
    : Do block While LParen expr RParen Semicolon
    ;

parenlessDoWhileStmt
    : Do block While expr Semicolon
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
    : lambda
    | assignment
    ;

modifier
    : At
    | Const
    ;

assignment
    : modifier* primary postfix* (Colon type)? assignmentOperator assignment
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
    : Fn LParen parameters RParen (Colon type)? (block | (Assign expr))
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
    : primary postfix*
    ;

postfix
    : LParen arguments RParen
    | Dot Identifier
    ;

arguments
    : (argument (Comma argument)*)?
    ;

argument
    : namedArgument
    | positionalArgument
    ;

namedArgument
    : Identifier Assign expr
    ;

positionalArgument
    : expr
    ;

parameters
    : (parameter (Comma parameter)*)?
    ;

parameter
    : Identifier (Colon type)? (Assign expr)?
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
    : LBracket (expr (Comma expr)*)? RBracket
    ;

mapLiteral
    : LBrace ((expr Colon expr) (Comma (expr Colon expr))*)? RBrace
    ;


// Types

type
    : unionType
    ;

unionType
    : intersectionType (BitOr intersectionType)*
    ;

intersectionType
    : postfixType (BitAnd postfixType)*
    ;

postfixType
    : primaryType QuestionMark?
    ;

primaryType
    : Identifier
    | tupleType
    | functionType
    | listType
    | mapType
    | LParen type RParen
    ;

functionType
    : LParen (type (Comma type)*)? RParen RArrow type
    ;

tupleType
    : Tuple Less type (Comma type)* Greater // at least one
    ;

listType
    : List Less type Greater
    ;

mapType
    : Map Less type Comma type Greater
    ;