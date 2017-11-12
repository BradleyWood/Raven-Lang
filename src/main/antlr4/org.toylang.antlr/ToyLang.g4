// a very early version of the grammar

grammar ToyLang;

// PARSER


toyFile
    :   packageDef? (importStatement*) statement* EOF
    ;
statement
    :   block
    |   forStatement
    |   varDeclaration
    |   annotationDeclaration
    |   methodDeclaration
    |   constructor
    |   ifStatement
    |   goStatement
    |   whileStatement
    |   returnStatement
    |   classDef
    |   expression SEMI
    |   CONTINUE SEMI
    |   BREAK SEMI
    |   SEMI
    ;
packageDef
    :   PACK qualifiedName SEMI
    ;
returnStatement
    :   RETURN expression SEMI
    |   RETURN SEMI
    ;
ifStatement
    :   IF expression statement (ELSE statement)?
    ;
goStatement
    :   GO funCall SEMI
    ;
whileStatement
    :   WHILE expression statement
    |   DO statement WHILE expression SEMI
    ;
forStatement
    :   FOR forControl statement
    ;
forControl
    :   '(' forControl ')'
    |   IDENTIFIER COLON expression
    |   IDENTIFIER range
    |   (decl=varDeclaration? | (init=expression? SEMI)) cond=expression? SEMI after=paramList?
    ;
range
    :   'range' expression (inc|dec) expression
    ;
inc
    :   'upto'
    |   'to'
    ;
dec :   'downto'
    ;
importStatement
    :  'import' qualifiedName ('.' '*')? SEMI
    ;
qualifiedName
    :   THIS
    |   IDENTIFIER ('.' IDENTIFIER)*
    ;
modifier
    :   (PUB|PRIV)
    ;
annotation
    :   AT qualifiedName ('(' paramDef (',' paramDef)* ')')?
    ;
annotationDeclaration
    :   AT INTER '{' (paramDef (',' paramDef)*)? '}'
    ;
methodDeclaration
    :   (annotation*) (modifier*) FUN IDENTIFIER '(' (paramDef (',' paramDef)*)? ')' block
    |   FUN? IDENTIFIER '(' (paramDef (',' paramDef)*)? ')' ASSIGNMENT expression SEMI
    ;
constructor
    :   (annotation*) (modifier*) CONSTRUCTOR '(' (paramDef (',' paramDef)*)? ')' block
    ;
paramDef
    :   IDENTIFIER
    ;
block
    :   '{' statement* '}'
    ;
varDeclaration
    :   (modifier*) VAR IDENTIFIER ASSIGNMENT expression SEMI
    |   (modifier*) VAR IDENTIFIER SEMI
    ;
classDef
    :   (modifier*) CLASS IDENTIFIER ('(' fields=paramList ')')? inheritance? block
    ;
inheritance
    :   ext? impl?
    ;
ext
    :   EXTENDS qualifiedName ('(' paramList ')')?
    ;
impl
    :   IMPL interfaceList
    ;
interfaceList
    :   qualifiedName (',' qualifiedName)*
    ;
expression
    :   THIS '.' expression
    |   SUPER '.' expression
    |   literal
    |   expression '.' qualifiedName
    |   expression '.' funCall
    |   funCall
    |   qualifiedName
    |   listIdx
    |   slice
    |   list
    |   dict
    |   (ADD|SUB|NOT) expression
    |   '(' expression ')'
    |   expression (EXP) expression
    |   expression (MULT|DIV|MOD) expression
    |   expression (ADD|SUB)  expression
    |   expression (GT|LT|GTE|LTE|EQUALS|NOT_EQUAL) expression
    |   expression (AND|OR) expression
    |   varAssignment
    ;
varAssignment
    :   <assoc=right>
        qualifiedName
        (   ASSIGNMENT
        |   ADD_ASSIGNMENT
        |   SUB_ASSIGNMENT
        |   MULT_ASSIGNMENT
        |   DIV_ASSIGNMENT
        |   MOD_ASSIGNMENT
        |   EXP_ASSIGNMENT
        )
        expression
        |
        listIdx ASSIGNMENT expression
    ;
funCall
    :   IDENTIFIER '(' paramList? ')'
    |   SUPER   '(' paramList? ')'
    |   THIS '(' paramList? ')'
    ;
paramList
    :   param (',' param)*
    ;
listIdx
    :   qualifiedName ('[' expression ']')*
    ;
slice
    :   qualifiedName '[' lhs=expression? ':' rhs=expression? ']'
    ;
list
    :   '[' paramList? ']'
    ;
dict
    :   '{' dictParamList? '}'
    ;
dictParamList
    :   dictParam (',' dictParam)*
    ;
dictParam
    :   expression ':' expression
    ;
param
    :   expression
    ;
literal
    :   number
    |   booleanLiteral
    |   stringLiteral
    |   'null'
    ;
stringLiteral
    :   StringLiteral
    ;
booleanLiteral
    :   TRUE
    |   FALSE
    ;
number
    : INT | HEX | FLOAT | HEX_FLOAT
    ;
// LEXER
StringLiteral
    :   '"' StringCharacters? '"'
    ;
fragment
StringCharacters
    :   StringCharacter+
    ;
fragment
StringCharacter
    :   ~["\\]
    |   EscapeSequence
    ;
// §3.10.6 Escape Sequences for Character and String Literals
fragment
EscapeSequence
    :   '\\' [btnfr"'\\]
    |   OctalEscape
    |   UnicodeEscape
    ;

fragment
OctalEscape
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' ZeroToThree OctalDigit OctalDigit
    ;
fragment
OctalDigit
    :   [0-7]
    ;
fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
ZeroToThree
    :   [0-3]
    ;

// Keywords

FUN     :   'fun';
IF      :   'if';
DO      :   'do';
GO      :   'go';
ELSE    :   'else';
FOR     :   'for';
VAR     :   'var';
THIS    :   'this';
TRUE    :   'true';
WHILE   :   'while';
BREAK   :   'break';
FALSE   :   'false';
SUPER   :   'super';
CLASS   :   'class';
RETURN  :   'return';
PUB     :   'public';
EXTENDS :   'extends';
IMPL    :   'impl' | 'implements';
PRIV    :   'private';
PACK    :   'package';
CONTINUE:   'continue';
ANNO    :   'annotation';
INTER   :   'interface';
CONSTRUCTOR :   'constructor';

NULL    :   'null';

IMPORT  :   'import';
SEMI    :   ';';




IDENTIFIER
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

WS  :  [ \t\r\n\u000C]+ -> skip
    ;


COMMENT
    :   '/*' .*? '*/' -> channel(HIDDEN)
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> channel(HIDDEN)
    ;

INT
    : Digit+
    ;

HEX
    : '0' [xX] HexDigit+
    ;

FLOAT
    : Digit+ '.' Digit* ExponentPart?
    | '.' Digit+ ExponentPart?
    | Digit+ ExponentPart
    ;

HEX_FLOAT
    : '0' [xX] HexDigit+ '.' HexDigit* HexExponentPart?
    | '0' [xX] '.' HexDigit+ HexExponentPart?
    | '0' [xX] HexDigit+ HexExponentPart
    ;

fragment
ExponentPart
    : [eE] [+-]? Digit+
    ;

fragment
HexExponentPart
    : [pP] [+-]? Digit+
    ;

fragment
Digit
    : [0-9]
    ;

fragment
HexDigit
    : [0-9a-fA-F]
    ;

// Operators
ASSIGNMENT
    : '='
    ;
GT
    : '>'
    ;
GTE
    : '>='
    ;
LTE
    : '<='
    ;
LT
    : '<'
    ;
EQUALS
    : '=='
    ;
NOT
    : '!'
    ;
NOT_EQUAL
    : '!='
    ;
COLON
    : ':'
    ;
AND
    : '&&'
    ;
OR
    : '||'
    ;
ADD
    : '+'
    ;
SUB
    : '-'
    ;
MULT
    : '*'
    ;
DIV
    : '/'
    ;
MOD
    : '%'
    ;
EXP
    : '**'
    ;
BT_LS
    : '<<'
    ;
BT_RS
    : '>>'
    ;
BT_ARS
    : '>>>'
    ;
BT_NOT
    : '~'
    ;
BT_AND
    : '&'
    ;
BT_OR
    : '|'
    ;
BT_XOR
    : '^'
    ;
ADD_ASSIGNMENT
    : '+='
    ;
SUB_ASSIGNMENT
    : '-='
    ;
MULT_ASSIGNMENT
    : '*='
    ;
DIV_ASSIGNMENT
    : '/='
    ;
MOD_ASSIGNMENT
    : '%='
    ;
EXP_ASSIGNMENT
    : '**='
    ;
AT
    : '@'
    ;