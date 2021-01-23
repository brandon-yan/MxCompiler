grammar Mx;

program: programDecl* EOF;

programDecl : varDecl | funcDecl | classDecl ;

varDecl : type variablelist ';';

variablelist : variable (',' variable)*;

variable : Identifier ('=' expression)?;

funcDecl : type Identifier '(' parameterlist? ')' suite;

classDecl : Class Identifier '{' (varDecl | funcDecl | constructDecl)* '}' ';' ;

constructDecl : Identifier '(' parameterlist? ')' suite;

parameterlist : parameter (',' parameter)*;

parameter : type Identifier;

type
    : arraytype
    | noarraytype
    | Void
    ;

arraytype : noarraytype ('[' ']')+;

noarraytype
    : Int
    | Bool
    | String
    | Identifier
    ;

suite : '{' statement* '}';

statement
    : suite
    | varDeclStmt
    | ifStmt
    | forStmt
    | whileStmt
    | flowStmt
    | expression ';'
    | ';'
    ;

varDeclStmt : VarDecl ;

ifStmt
    : If '(' expression ')' trueStmt = statement
        (Else falseStmt = statement)?
    ;

forStmt
    : For '(' init = expression?  ';' condition = expression? ';' increase = expression? ')' statement
    ;
whileStmt
    : While '(' expression ')' statement
    ;

flowStmt
    : Break ';'
    | Continue ';'
    | Return expression? ';'
    ;

expression
    : primary                                                             #atomExpr
    | lhs = expression op = ('+' | '-') rhs = expression                  #binaryExpr
    | lhs = expression op = ('==' | '!=') rhs = expression                #binaryExpr
    | lhs = expression op = ('*' | '/' | '%') rhs = expression            #binaryExpr
    | lhs = expression op = ('<<' | '>>') rhs = expression                #binaryExpr
    | lhs = expression op = ('<' | '>') rhs = expression                  #binaryExpr
    | lhs = expression op = ('<=' | '>=') rhs = expression                #binaryExpr
    | lhs = expression op = '&' rhs = expression                          #binaryExpr
    | lhs = expression op = '|' rhs = expression                          #binaryExpr
    | lhs = expression op = '^' rhs = expression                          #binaryExpr
    | lhs = expression op = '&&' rhs = expression                         #binaryExpr
    | lhs = expression op = '||' rhs = expression                         #binaryExpr
    | lhs = expression op = ('++' | '--')                                 #suffixExpr

    | <assoc=right> lhs = expression '=' rhs = expression                 #assignExpr
    | <assoc=right> op = ('++' | '--') expression                         #prefixExpr
    | <assoc=right> op = ('+' | '-') expression                           #prefixExpr
    | <assoc=right> op = ('!' | '~') expression                           #prefixExpr

    | <assoc=right> New creator                                           #newExpr
    | expression '.' Identifier                                           #memberExpr
    | expression '(' expressionlist? ')'                                  #funccallExpr
    | array = expression '[' index = expression ']'                       #arrayExpr

    ;

expressionlist : expression (',' expression)* ;

creator
    : noarraytype ('[' expression ']')* ('[' ']')+ ('[' expression ']')+   #errorCreator
    | noarraytype ('[' expression ']')+ ('[' ']')*                         #arrayCreator
    | noarraytype '(' ')'                                                  #classCreator
    | noarraytype                                                          #basicCreator
    ;

primary
    : '(' expression ')'
    | Identifier 
    | literal
    | This
    ;

literal
    : IntegerConstant
    | BoolConstant
    | StringConstant
    | NullConstant
    ;

IntegerConstant
    : [1-9] [0-9]*
    | '0'
    ;

BoolConstant
    : True
    | False
    ;

StringConstant
    : '"' ('\\n' | '\\\\' | '\\"' | .)*? '"'
    ;

NullConstant
    : Null
    ;

Int : 'int';
Bool: 'bool';
String: 'string';
True : 'true';
False : 'false';
Null : 'null';
Void : 'void';

If : 'if';
Else : 'else';
For : 'for';
While : 'while';
Break : 'break';
Continue : 'continue';
Return : 'return';
This : 'this';
New : 'new';
Class : 'class';

LeftParen : '(';
RightParen : ')';
LeftBracket : '[';
RightBracket : ']';
LeftBrace : '{';
RightBrace : '}';

Less : '<';
LessEqual : '<=';
Greater : '>';
GreaterEqual : '>=';
LeftShift : '<<';
RightShift : '>>';

Plus : '+';
Minus : '-';

And : '&';
Or : '|';
AndAnd : '&&';
OrOr : '||';
Caret : '^';
Not : '!';
Tilde : '~';

Question : '?';
Colon : ':';
Semi : ';';
Comma : ',';

Assign : '=';
Equal : '==';
NotEqual : '!=';

Identifier
    : [a-zA-Z] [a-zA-Z_0-9]*
    ;


Whitespace
    :   [ \t]+
        -> skip
    ;

Newline
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> skip
    ;

BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;

LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;