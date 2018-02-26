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
@parser::header {
    package parser;
    import ast.*;
    import type.*;
    import org.apache.commons.lang3.tuple.Pair;
    import static org.apache.commons.text.StringEscapeUtils.unescapeJava;
}
@lexer::header {
    package parser;
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
    private void setLineOffset(ASTNode node, Token t) {
        node.setLine(t.getLine());
        node.setOffset(t.getCharPositionInLine());
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
    : (f = function { prog.add(f); })+ EOF
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
        VariableDeclarationList declarations = new VariableDeclarationList();
        StatementList statements = new StatementList();
    }
    : '{'
    (decl = varDecl { declarations.add(decl); })*
    (stmt = statement { if (stmt != null) statements.add(stmt); })*
    '}'
    { fb = new FunctionBody(declarations, statements); }
    ;

varDecl returns [VariableDeclaration decl]
    :
    t = compoundType
    id = identifier ';'
    { decl = new VariableDeclaration(t, id); }
    ;

compoundType returns [TypeNode tn]
    @init { Type t = null; }
    @after {
        tn = new TypeNode(t);
        setLineOffset(tn, tp.getRight());
    }
    : tp = type
    { t = tp.getLeft(); }
    | tp = type '[' size = INTEGER_CONSTANT ']'
    { t = new ArrayType(tp.getLeft(), Integer.parseInt($size.text)); }
    ;

type returns [Pair<Type, Token> p]
    @init { Type t = null; }
    @after { p = Pair.of(t, k); }
    : k = KW_INT     { t = IntegerType.INSTANCE; }
    | k = KW_FLOAT   { t = FloatType.INSTANCE; }
    | k = KW_CHAR    { t = CharType.INSTANCE; }
    | k = KW_STRING  { t = StringType.INSTANCE; }
    | k = KW_BOOLEAN { t = BooleanType.INSTANCE; }
    | k = KW_VOID    { t = VoidType.INSTANCE; }
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
    @after { setLineOffset(s, $rkw); }
    : rkw = KW_RETURN retVal = expr? ';'
    { s = new ReturnStatement(retVal); }
    ;
assignmentStatement returns [Statement s]
    : (name = identifier) '[' (index = expr) ']' '=' (value = expr) ';'
    { s = new AssignmentStatement(new ArrayAssignment(name, index, value)); }
    | (name = identifier) '=' (value = expr) ';'
    { s = new AssignmentStatement(new VariableAssignment(name, value)); }
    ;
expressionStatement returns [Statement s]
    : e = expr ';'
    { s = new ExpressionStatement(e); }
    ;

block returns [Block b]
    @init { b = new Block(); }
    : '{' (s = statement { if (s != null) b.add(s); })* '}'
    ;

expr returns [Expression e]
    : lhs = exprLessThan { e = lhs; }
    (op = OP_EQUALS rhs = exprLessThan {
        e = new EqualityExpression(e, rhs);
        setLineOffset(e, $op);
    })*
    ;

exprLessThan returns [Expression e]
    : lhs = exprPlusMinus { e = lhs; }
    (op = OP_LESSTHAN rhs = exprPlusMinus {
        e = new LessThanExpression(e, rhs);
        setLineOffset(e, $op);
    })*
    ;

exprPlusMinus returns [Expression e]
    : lhs = exprMul { e = lhs; }
    (op = (OP_PLUS|OP_MINUS) rhs = exprMul {
        if ($op.type == OP_PLUS) {
            e = new AddExpression(e, rhs);
        } else if ($op.type == OP_MINUS) {
            e = new SubtractExpression(e, rhs);
        }
        setLineOffset(e, $op);
    })*
    ;

exprMul returns [Expression e]
    : lhs = atom { e = lhs; } (op = OP_MUL rhs = atom {
        e = new MultExpression(e, rhs);
        setLineOffset(e, $op);
    })*
    ;
    
atom returns [Expression e]
    @after { if (lb != null) setLineOffset(e, $lb); }
    : name = identifier lb = '[' index = expr ']'
    { e = new ArrayReference(name, index); }
    | name = identifier lb = '(' expressions = exprList ')'
    { e = new FunctionCall(name, expressions); }
    | id = identifier
    { e = new IdentifierValue(id); }
    | l = literal
    { e = l; }
    | lb = '(' exp = expr ')'
    { e = new ParenExpression(exp); }
    ;

literal returns [Literal l]
    @after { setLineOffset(l, $t); }
    : t = STRING_CONSTANT    { l = new StringLiteral(unescapeJava($t.text.substring(1, $t.text.length() - 1))); }
    | t = INTEGER_CONSTANT   { l = new IntegerLiteral(Integer.parseInt($t.text)); }
    | t = FLOAT_CONSTANT     { l = new FloatLiteral(Float.parseFloat($t.text)); }
    | t = CHARACTER_CONSTANT { l = new CharacterLiteral(unescapeJava($t.text).charAt(1)); }
    | t = KW_TRUE            { l = new BooleanLiteral(true); }
    | t = KW_FALSE           { l = new BooleanLiteral(false); }
    ;

exprList returns [ExpressionList el]
    @init { el = new ExpressionList(); }
    : exp = expr { el.add(exp); }
    (exp = exprMore { el.add(exp); })*
    |
    ;

exprMore returns [Expression e]
    : ',' exp = expr
    { e = exp; }
    ;

identifier returns [Identifier id]
    @after { setLineOffset(id, $name); }
    : name = ID
    { id = new Identifier($name.text); }
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
