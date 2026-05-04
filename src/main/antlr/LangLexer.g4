lexer grammar LangLexer;

@header {
package net.aros.language;
}

// Keywords
If                       : 'if'         ;
Else                     : 'else'       ;
While                    : 'while'      ;
For                      : 'for'        ;
True                     : 'true'       ;
False                    : 'false'      ;
Null                     : 'null'       ;
In                       : 'in'         ;
Do                       : 'do'         ;
Fn                       : 'fn'         ;

// Symbols
LParen                   : '('          ;
RParen                   : ')'          ;
LBrace                   : '{'          ;
RBrace                   : '}'          ;
LBracket                 : '['          ;
RBracket                 : ']'          ;
Colon                    : ':'          ;
Comma                    : ','          ;
QuestionMark             : '?'          ;

// Operators
PlusAssign               : '+='         ;
Plus                     : '+'          ;
MinusAssign              : '-='         ;
Minus                    : '-'          ;
MultiplyAssign           : '*='         ;
Multiply                 : '*'          ;
DivideAssign             : '/='         ;
Divide                   : '/'          ;
ModuloAssign             : '%='         ;
Modulo                   : '%'          ;
BitAndAssign             : '&='         ;
BitAnd                   : '&'          ;
BitOrAssign              : '|='         ;
BitOr                    : '|'          ;
BitXorAssign             : '^='         ;
BitXor                   : '^'          ;
ShiftLeftAssign          : '<<='        ;
ShiftLeft                : '<<'         ;
ShiftRightAssign         : '>>='        ;
ShiftRight               : '>>'         ;
ShiftRightUnsignedAssign : '>>>='       ;
ShiftRightUnsigned       : '>>>'        ;
BitNot                   : '~'          ;
LogicAndAssign           : '&&='        ;
LogicAnd                 : '&&' | 'and' ;
LogicOrAssign            : '||='        ;
LogicNot                 : '!'  | 'not' ;
LogicXorAssign           : '^^='        ;
LogicXor                 : '^^' | 'xor' ;
LogicOr                  : '||' | 'or'  ;
Equals                   : '=='         ;
NotEquals                : '!='         ;
LessEqual                : '<='         ;
Less                     : '<'          ;
GreaterEqual             : '>='         ;
Greater                  : '>'          ;
Assign                   : '='          ;

//AnyAssignment
//    : PlusAssign
//    | MinusAssign
//    | MultiplyAssign
//    | DivideAssign
//    | ModuloAssign
//    | BitAndAssign
//    | BitOrAssign
//    | BitXorAssign
//    | ShiftLeftAssign
//    | ShiftRightAssign
//    | ShiftRightUnsignedAssign
//    | LogicAndAssign
//    | LogicOrAssign
//    | LogicXorAssign
//    | Assign;

Identifier    : [a-zA-Z_][a-zA-Z0-9_]*           ;
IntLiteral    : [0-9]+                           ;
FloatLiteral  : [0-9]* '.' [0-9]+                ;
StringLiteral : '"' ( '\\' . | ~["\\\r\n] )* '"' ;
WS            : [ \t\r\n]+ -> skip               ;