grammar UnnamedLanguage;

tokens {
    KW_TRUE     = 'true';
    KW_FALSE    = 'false';
    KW_IF       = 'if';
    KW_ELSE     = 'else';
    KW_WHILE    = 'while';
    KW_PRINT    = 'print';
    KW_PRINTLN  = 'println';
    KW_RETURN   = 'return';
    KW_INT      = 'int';
    KW_FLOAT    = 'float';
    KW_CHAR     = 'char';
    KW_STRING   = 'string';
    KW_BOOLEAN  = 'boolean';
    KW_VOID     = 'void';
    OP_EQUALS   = '==';
    OP_PLUS     = '+';
    OP_MINUS    = '-';
    OP_LESSTHAN = '<';
    OP_MUL      = '*';
}

///*
@header {
    import ast.*;
}

@members {
    public Object recoverFromMismatchedToken(IntStream input,
                                             int ttype,
                                             BitSet follow)
        throws RecognitionException
    {
        throw new MismatchedTokenException(ttype, input);
    }
    public Object recoverFromMismatchedSet(IntStream input,
                                           RecognitionException e,
                                           BitSet follow)
        throws RecognitionException
    {
        reportError(e);
        throw e;
    }
}

@rulecatch {
    catch (RecognitionException ex) {
        reportError(ex);
        throw ex;
    }
}
//*/

program returns [Program prog]
    @init { prog = new Program(); }
    : (f = function { prog.addElement(f); })+ EOF
    ;

function returns [Function f]
    :
    fd = functionDecl
    fb = functionBody
    { f = new Function(fd, fb); }
    ;
    
functionDecl returns [FunctionDeclaration fd]
    :
    rt = compoundType
    name = identifier
    '(' params = formalParameters ')'
    { fd = new FunctionDeclaration(rt, name, params); }
    ;
    
formalParameters returns [FormalParameterList params]
    @init { params = new FormalParameterList(); }
    : // Empty parameter list
    |
    t = compoundType
    name = identifier
    { params.add(new FormalParameter(t, name)); }
    (param = moreFormals { params.add(param); })*
    ;
    
moreFormals returns [FormalParameter param]
    : ','
    t = compoundType
    name = identifier
    { param = new FormalParameter(t, name); }
    ;
    
functionBody returns [FunctionBody fb]
    @init {
        List<Declaration> decls = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();
    }
    : '{'
    (decl = varDecl { decls.add(decl); })*
    (s = statement { if (s != null) statements.add(s); })*
    '}'
    { fb = new FunctionBody(decls); }
    ;

varDecl returns [Declaration decl]
    :
    t = compoundType
    id = identifier ';'
    { decl = new Declaration(t, id); }
    ;

compoundType returns [TypeNode tn]
    @after { tn = new TypeNode(t); }
    : t = type
    | et = type '[' size = INTEGER_CONSTANT ']'
    { t = new ArrayType(et, Integer.parseInt($size.text)); }
    ;

type returns [Type t]
    : KW_INT     { t = new IntegerType(); }
    | KW_FLOAT   { t = new FloatType(); }
    | KW_CHAR    { t = new CharType(); }
    | KW_STRING  { t = new StringType(); }
    | KW_BOOLEAN { t = new BooleanType(); }
    | KW_VOID    { t = new VoidType(); }
    ;
    
statement returns [Statement s] options {backtrack=true;}
    @after { s = st; }
    : ';'
    | st = ifStatement
    | st = whileStatement
    | st = printStatement
    | st = printlnStatement
    | st = returnStatement
    | st = assignmentStatement
    | st = expressionStatement
    ;

ifStatement returns [Statement s]
    : KW_IF '(' cond = expr ')' thenClause = block (KW_ELSE elseClause = block)?
    { s = new IfStatement(cond, thenClause, elseClause); }
    ;
whileStatement returns [Statement s]
    : KW_WHILE '(' cond = expr ')' body = block
    { s = new WhileStatement(cond, body); }
    ;
printStatement returns [Statement s]
    : KW_PRINT exp = expr ';'
    { s = new PrintStatement(exp); }
    ;
printlnStatement returns [Statement s]
    : KW_PRINTLN exp = expr ';'
    { s = new PrintlnStatement(exp); }
    ;
returnStatement returns [Statement s]
    : KW_RETURN retVal = expr? ';'
    { s = new ReturnStatement(retVal); }
    ;
assignmentStatement returns [Statement s]
    : (name = identifier) '=' (value = expr) ';'
    { s = new AssignmentStatement(new VariableAssignment(name, value)); }
    | (name = identifier) '[' (index = expr) ']' '=' (value = expr) ';'
    { s = new AssignmentStatement(new ArrayAssignment(name, index, value)); }
    ;
expressionStatement returns [Statement s]
    : e = expr ';'
    { s = new ExpressionStatement(e); }
    ;

block returns [Block b]
    @init { List<Statement> statements = new ArrayList<>(); }
    : '{' statement* '}'
    { b = new Block(); }
    ;

expr returns [Expression e]
    : lhs = exprLessThan { e = lhs; }
    (OP_EQUALS rhs = exprLessThan { e = new EqualityExpression(e, rhs); })*
    ;

exprLessThan returns [Expression e]
    : lhs = exprPlusMinus { e = lhs; }
    (OP_LESSTHAN rhs = exprPlusMinus { e = new LessThanExpression(e, rhs); })*
    ;

exprPlusMinus returns [Expression e]
    : lhs = exprMul { e = lhs; }
    (op = (OP_PLUS|OP_MINUS) rhs = exprMul {
        if ($op.type == OP_PLUS) {
            e = new AddExpression(e, rhs);
        } else {
            e = new SubtractExpression(e, rhs);
        }
    })*
    ;

exprMul returns [Expression e]
    : lhs = atom { e = lhs; } (OP_MUL rhs = atom {
        e = new MultExpression(e, rhs);
    })*
    ;
    
atom returns [Expression e]
    @init { e = new Atom(); }
    : identifier '[' expr ']'
    | identifier '(' exprList ')' 
    | identifier 
    | literal 
    | '(' expr ')'
    ;

literal returns [Literal l]
    : s = STRING_CONSTANT    { l = new StringLiteral($s.text); }
    | i = INTEGER_CONSTANT   { l = new IntegerLiteral(Integer.parseInt($i.text)); }
    | f = FLOAT_CONSTANT     { l = new FloatLiteral(Float.parseFloat($f.text)); }
    | c = CHARACTER_CONSTANT { l = new CharacterLiteral($c.text.charAt(0)); }
    | KW_TRUE                { l = new BooleanLiteral(true); }
    | KW_FALSE               { l = new BooleanLiteral(false); }
    ;

exprList
    : expr exprMore*
    |
    ;

exprMore
    : ',' expr
    ;

identifier returns [Identifier id]
    : ID
    { id = new Identifier(); }
    ;
    
// Lexer Rules
ID  : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INTEGER_CONSTANT
    : ('0' | ('1'..'9'('0'..'9')*))
    ;

// All of the following lexical elements here were auto-generated by ANTLRWORKS
FLOAT_CONSTANT
    : ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    | '.' ('0'..'9')+ EXPONENT?
    | ('0'..'9')+ EXPONENT
    ;

STRING_CONSTANT
    : '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

CHARACTER_CONSTANT
    : '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
    ;

fragment
EXPONENT
    : ('e'|'E') ('+'|'-')? ('0'..'9')+
    ;

fragment
HEX_DIGIT
    : ('0'..'9'|'a'..'f'|'A'..'F')
    ;

fragment
ESC_SEQ
    : '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    | UNICODE_ESC
    | OCTAL_ESC
    ;

fragment
OCTAL_ESC
    : '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    | '\\' ('0'..'7') ('0'..'7')
    | '\\' ('0'..'7')
    ;
    
COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;

WS  :(' '
    | '\t'
    | '\r'
    | '\n'
) {$channel=HIDDEN;}
;

fragment
UNICODE_ESC
    : '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
