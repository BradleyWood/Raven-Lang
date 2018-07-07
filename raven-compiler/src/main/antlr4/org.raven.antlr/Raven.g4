grammar Raven;

ravenFile
    :   ((packageDef semi) | (packageDef EOF))? ((importStatement semi) | (SEMI | NL+))* ((importStatement semi?) EOF)?
    ((statement semi) | (SEMI | NL+))* (statement semi?)? EOF
    ;

statement
    :   block
    |   tryCatchFinally
    |   raiseStatement
    |   forStatement
    |   varDeclaration
    |   annotationDeclaration
    |   methodDeclaration
    |   constructor
    |   ifStatement
    |   whileStatement
    |   deferStatement
    |   returnStatement
    |   classDef
    |   expression
    |   CONTINUE
    |   BREAK
    ;

packageDef
    :   PACK NL* qualifiedName
    ;

raiseStatement
    :   RAISE NL* expression
    ;

returnStatement
    :   RETURN NL* expression
    |   RETURN
    ;

deferStatement
    :   DEFER NL* (expression NL* '.' NL*)? funCall
    ;

ifStatement
    :   IF NL* '(' expression ')' NL* statement (NL* ELSE NL* statement SEMI?)?
    ;

tryCatchFinally
    :   TRY NL* block NL* CATCH NL* boxedId NL* block (NL* FINALLY NL* block)?
    ;

boxedId
    :   '(' NL* boxedId NL* ')'
    |   IDENTIFIER
    ;

whileStatement
    :   WHILE NL* '(' expression ')' NL* statement
    |   DO NL* statement NL* WHILE NL* '(' expression ')'
    ;

forStatement
    :   FOR NL* '(' NL* forControl NL* ')' NL* statement
    ;

forControl
    :   IDENTIFIER NL* COLON NL* expression
    |   IDENTIFIER NL* range
    |   (decl=varDeclaration? | (init=expression?)) SEMI cond=expression? SEMI after=paramList?
    ;

range
    :   'range' NL* expression NL* (inc|dec) NL* expression
    ;

inc
    :   'upto'
    |   'to'
    ;

dec :   'downto'
    ;

importStatement
    :  'import' NL* qualifiedName (NL* '.' NL* '*')?
    ;

qualifiedName
    :   (THIS NL* '.' NL*)? IDENTIFIER (NL* '.' NL* IDENTIFIER)*
    |   (SUPER NL* '.'NL* )? IDENTIFIER (NL* '.' NL* IDENTIFIER)*
    |   THIS
    |   SUPER
    ;

modifier
    :   (PUB|PRIV)
    ;

annotation
    :   AT NL* qualifiedName (NL* annotationParamList)?
    ;

annotationParamList
    :   '(' NL* annotationParam (NL* ',' NL* annotationParam)* NL* ')'
    ;

annotationParam
    :   paramDef NL* '=' NL* (literal)
    ;

constant
    :   literal
    |   qualifiedName NL* '.' NL* CLASS
    |   '[' NL* (constantList NL*)? ']'
    ;

constantList
    :   constant (NL* ',' NL* constant)*
    ;

annotationDeclaration
    :   AT NL* INTER NL* IDENTIFIER NL* '{' (NL* paramDef (NL* ',' NL* paramDef)*)? NL* '}'
    ;

methodDeclaration
    :   (annotation NL*)* (modifier NL*)* FUN IDENTIFIER '(' (NL* paramDef (NL* ',' NL* paramDef)*)? NL* ')' NL* block
    |   FUN? IDENTIFIER '(' (NL* paramDef (NL* ',' NL* paramDef)*)? NL* ')' NL* ASSIGNMENT NL* expression
    ;

constructor
    :   (annotation NL*)* (modifier NL*)* CONSTRUCTOR '(' (NL* paramDef (NL* ',' NL* paramDef)*)? NL* ')' NL* block
    ;

paramDef
    :   IDENTIFIER
    ;

block
    :   '{' NL* ((statement semi) | (SEMI | NL+))* (statement semi?)? '}'
    ;

varDeclaration
    :   (modifier NL*)* VAR NL* IDENTIFIER NL* ASSIGNMENT NL* expression
    |   (modifier NL*)* VAR NL* IDENTIFIER
    ;

classDef
    :   (modifier NL*)* CLASS NL* IDENTIFIER NL* ('(' NL* (fields=paramList NL*)? ')' NL*)? inheritance? NL* block
    ;

inheritance
    :   impl
    |   ext
    |   ext NL* impl
    ;

ext
    :   EXTENDS NL* qualifiedName (NL* '(' NL* paramList NL* ')')?
    ;

impl
    :   IMPL NL* interfaceList
    ;

interfaceList
    :   qualifiedName (NL* ',' NL* qualifiedName)*
    ;

expression
    :   literal
    |   expression NL* DOT NL* qualifiedName
    |   expression NL* DOT NL* funCall
    |   funCall
    |   goExpression
    |   qualifiedName
    |   expression NL* listIdx
    |   list
    |   expression NL* ternary='?' expression NL* ':' NL* expression
    |   dict
    |   (ADD|SUB|NOT) NL* expression
    |   '(' NL* expression NL* ')'
    |   lst=expression NL* '[' NL* lhs=expression? ':' rhs=expression? NL* ']'
    |   expression NL* (EXP) NL* expression
    |   expression NL* (MULT|DIV|MOD) NL* expression
    |   expression NL* (ADD|SUB) NL* expression
    |   expression NL* (GT|LT|GTE|LTE|EQUALS|NOT_EQUAL) NL* expression
    |   expression NL* (AND|OR) NL* expression
    |   varAssignment
    |   <assoc=right> lhs=expression listIdx ASSIGNMENT rhs=expression
    ;

varAssignment
    :   <assoc=right>
        qualifiedName NL*
        (   ASSIGNMENT
        |   ADD_ASSIGNMENT
        |   SUB_ASSIGNMENT
        |   MULT_ASSIGNMENT
        |   DIV_ASSIGNMENT
        |   MOD_ASSIGNMENT
        |   EXP_ASSIGNMENT
        )
        NL* expression
    ;

funCall
    :   IDENTIFIER '(' NL* (paramList NL*)? ')'
    |   SUPER   '(' NL* (paramList NL*)? ')'
    |   THIS '(' NL* (paramList NL*)? ')'
    ;

goExpression
    :   GO (NL* expression  NL* '.')?  NL* funCall
    ;

paramList
    :   param (NL* ',' NL* param)*
    ;

listIdx
    :   ('[' NL* expression NL* ']')+
    ;

list
    :   '[' NL* (paramList NL*)? ']'
    ;

dict
    :   '{' NL* (dictParamList NL*)? '}'
    ;

dictParamList
    :   dictParam (NL* ',' NL* dictParam)*
    ;

dictParam
    :   expression NL* ':' NL* expression
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
TRY     :   'try';
VAR     :   'var';
THIS    :   'this';
TRUE    :   'true';
CATCH   :   'catch';
RAISE   :   'raise';
WHILE   :   'while';
BREAK   :   'break';
FALSE   :   'false';
SUPER   :   'super';
CLASS   :   'class';
DEFER   :   'defer';
RETURN  :   'return';
PUB     :   'public';
FINALLY :   'finally';
EXTENDS :   'extends';
IMPL    :   'implements';
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

WS  : [\u0020\u0009\u000C] -> channel(HIDDEN);

NL: '\u000A' | '\u000D' '\u000A' ;

semi: NL+ | SEMI | SEMI NL+;

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
DOT
    : '.'
    ;
