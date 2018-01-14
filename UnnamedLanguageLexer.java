// $ANTLR 3.0.1 UnnamedLanguage.g 2018-01-13 16:26:10

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class UnnamedLanguageLexer extends Lexer {
    public static final int KW_RETURN=11;
    public static final int UNICODE_ESC=31;
    public static final int KW_CHAR=14;
    public static final int KW_INT=12;
    public static final int INTEGER_CONSTANT=18;
    public static final int CHARACTER_CONSTANT=26;
    public static final int KW_IF=6;
    public static final int OP_MINUS=22;
    public static final int KW_PRINT=9;
    public static final int OP_EQUALS=19;
    public static final int ID=27;
    public static final int OCTAL_ESC=32;
    public static final int Tokens=44;
    public static final int KW_PRINTLN=10;
    public static final int OP_MUL=23;
    public static final int KW_STRING=15;
    public static final int T36=36;
    public static final int T35=35;
    public static final int T38=38;
    public static final int T37=37;
    public static final int KW_FALSE=5;
    public static final int T39=39;
    public static final int KW_WHILE=8;
    public static final int OP_PLUS=21;
    public static final int KW_BOOLEAN=16;
    public static final int KW_TRUE=4;
    public static final int KW_VOID=17;
    public static final int COMMENT=33;
    public static final int T41=41;
    public static final int T40=40;
    public static final int T43=43;
    public static final int T42=42;
    public static final int HEX_DIGIT=30;
    public static final int ESC_SEQ=29;
    public static final int STRING_CONSTANT=24;
    public static final int WS=34;
    public static final int EOF=-1;
    public static final int KW_ELSE=7;
    public static final int OP_LESSTHAN=20;
    public static final int KW_FLOAT=13;
    public static final int EXPONENT=28;
    public static final int FLOAT_CONSTANT=25;
    public UnnamedLanguageLexer() {;} 
    public UnnamedLanguageLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "UnnamedLanguage.g"; }

    // $ANTLR start KW_TRUE
    public final void mKW_TRUE() throws RecognitionException {
        try {
            int _type = KW_TRUE;
            // UnnamedLanguage.g:3:9: ( 'true' )
            // UnnamedLanguage.g:3:11: 'true'
            {
            match("true"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_TRUE

    // $ANTLR start KW_FALSE
    public final void mKW_FALSE() throws RecognitionException {
        try {
            int _type = KW_FALSE;
            // UnnamedLanguage.g:4:10: ( 'false' )
            // UnnamedLanguage.g:4:12: 'false'
            {
            match("false"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_FALSE

    // $ANTLR start KW_IF
    public final void mKW_IF() throws RecognitionException {
        try {
            int _type = KW_IF;
            // UnnamedLanguage.g:5:7: ( 'if' )
            // UnnamedLanguage.g:5:9: 'if'
            {
            match("if"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_IF

    // $ANTLR start KW_ELSE
    public final void mKW_ELSE() throws RecognitionException {
        try {
            int _type = KW_ELSE;
            // UnnamedLanguage.g:6:9: ( 'else' )
            // UnnamedLanguage.g:6:11: 'else'
            {
            match("else"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_ELSE

    // $ANTLR start KW_WHILE
    public final void mKW_WHILE() throws RecognitionException {
        try {
            int _type = KW_WHILE;
            // UnnamedLanguage.g:7:10: ( 'while' )
            // UnnamedLanguage.g:7:12: 'while'
            {
            match("while"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_WHILE

    // $ANTLR start KW_PRINT
    public final void mKW_PRINT() throws RecognitionException {
        try {
            int _type = KW_PRINT;
            // UnnamedLanguage.g:8:10: ( 'print' )
            // UnnamedLanguage.g:8:12: 'print'
            {
            match("print"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_PRINT

    // $ANTLR start KW_PRINTLN
    public final void mKW_PRINTLN() throws RecognitionException {
        try {
            int _type = KW_PRINTLN;
            // UnnamedLanguage.g:9:12: ( 'println' )
            // UnnamedLanguage.g:9:14: 'println'
            {
            match("println"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_PRINTLN

    // $ANTLR start KW_RETURN
    public final void mKW_RETURN() throws RecognitionException {
        try {
            int _type = KW_RETURN;
            // UnnamedLanguage.g:10:11: ( 'return' )
            // UnnamedLanguage.g:10:13: 'return'
            {
            match("return"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_RETURN

    // $ANTLR start KW_INT
    public final void mKW_INT() throws RecognitionException {
        try {
            int _type = KW_INT;
            // UnnamedLanguage.g:11:8: ( 'int' )
            // UnnamedLanguage.g:11:10: 'int'
            {
            match("int"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_INT

    // $ANTLR start KW_FLOAT
    public final void mKW_FLOAT() throws RecognitionException {
        try {
            int _type = KW_FLOAT;
            // UnnamedLanguage.g:12:10: ( 'float' )
            // UnnamedLanguage.g:12:12: 'float'
            {
            match("float"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_FLOAT

    // $ANTLR start KW_CHAR
    public final void mKW_CHAR() throws RecognitionException {
        try {
            int _type = KW_CHAR;
            // UnnamedLanguage.g:13:9: ( 'char' )
            // UnnamedLanguage.g:13:11: 'char'
            {
            match("char"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_CHAR

    // $ANTLR start KW_STRING
    public final void mKW_STRING() throws RecognitionException {
        try {
            int _type = KW_STRING;
            // UnnamedLanguage.g:14:11: ( 'string' )
            // UnnamedLanguage.g:14:13: 'string'
            {
            match("string"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_STRING

    // $ANTLR start KW_BOOLEAN
    public final void mKW_BOOLEAN() throws RecognitionException {
        try {
            int _type = KW_BOOLEAN;
            // UnnamedLanguage.g:15:12: ( 'boolean' )
            // UnnamedLanguage.g:15:14: 'boolean'
            {
            match("boolean"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_BOOLEAN

    // $ANTLR start KW_VOID
    public final void mKW_VOID() throws RecognitionException {
        try {
            int _type = KW_VOID;
            // UnnamedLanguage.g:16:9: ( 'void' )
            // UnnamedLanguage.g:16:11: 'void'
            {
            match("void"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end KW_VOID

    // $ANTLR start T35
    public final void mT35() throws RecognitionException {
        try {
            int _type = T35;
            // UnnamedLanguage.g:17:5: ( '(' )
            // UnnamedLanguage.g:17:7: '('
            {
            match('('); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T35

    // $ANTLR start T36
    public final void mT36() throws RecognitionException {
        try {
            int _type = T36;
            // UnnamedLanguage.g:18:5: ( ')' )
            // UnnamedLanguage.g:18:7: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T36

    // $ANTLR start T37
    public final void mT37() throws RecognitionException {
        try {
            int _type = T37;
            // UnnamedLanguage.g:19:5: ( ',' )
            // UnnamedLanguage.g:19:7: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T37

    // $ANTLR start T38
    public final void mT38() throws RecognitionException {
        try {
            int _type = T38;
            // UnnamedLanguage.g:20:5: ( '{' )
            // UnnamedLanguage.g:20:7: '{'
            {
            match('{'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T38

    // $ANTLR start T39
    public final void mT39() throws RecognitionException {
        try {
            int _type = T39;
            // UnnamedLanguage.g:21:5: ( '}' )
            // UnnamedLanguage.g:21:7: '}'
            {
            match('}'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T39

    // $ANTLR start T40
    public final void mT40() throws RecognitionException {
        try {
            int _type = T40;
            // UnnamedLanguage.g:22:5: ( ';' )
            // UnnamedLanguage.g:22:7: ';'
            {
            match(';'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T40

    // $ANTLR start T41
    public final void mT41() throws RecognitionException {
        try {
            int _type = T41;
            // UnnamedLanguage.g:23:5: ( '[' )
            // UnnamedLanguage.g:23:7: '['
            {
            match('['); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T41

    // $ANTLR start T42
    public final void mT42() throws RecognitionException {
        try {
            int _type = T42;
            // UnnamedLanguage.g:24:5: ( ']' )
            // UnnamedLanguage.g:24:7: ']'
            {
            match(']'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T42

    // $ANTLR start T43
    public final void mT43() throws RecognitionException {
        try {
            int _type = T43;
            // UnnamedLanguage.g:25:5: ( '=' )
            // UnnamedLanguage.g:25:7: '='
            {
            match('='); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T43

    // $ANTLR start OP_EQUALS
    public final void mOP_EQUALS() throws RecognitionException {
        try {
            int _type = OP_EQUALS;
            // UnnamedLanguage.g:146:2: ( '==' )
            // UnnamedLanguage.g:146:4: '=='
            {
            match("=="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OP_EQUALS

    // $ANTLR start OP_PLUS
    public final void mOP_PLUS() throws RecognitionException {
        try {
            int _type = OP_PLUS;
            // UnnamedLanguage.g:148:2: ( '+' )
            // UnnamedLanguage.g:148:4: '+'
            {
            match('+'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OP_PLUS

    // $ANTLR start OP_MINUS
    public final void mOP_MINUS() throws RecognitionException {
        try {
            int _type = OP_MINUS;
            // UnnamedLanguage.g:150:2: ( '-' )
            // UnnamedLanguage.g:150:4: '-'
            {
            match('-'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OP_MINUS

    // $ANTLR start OP_LESSTHAN
    public final void mOP_LESSTHAN() throws RecognitionException {
        try {
            int _type = OP_LESSTHAN;
            // UnnamedLanguage.g:152:2: ( '<' )
            // UnnamedLanguage.g:152:4: '<'
            {
            match('<'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OP_LESSTHAN

    // $ANTLR start OP_MUL
    public final void mOP_MUL() throws RecognitionException {
        try {
            int _type = OP_MUL;
            // UnnamedLanguage.g:154:2: ( '*' )
            // UnnamedLanguage.g:154:4: '*'
            {
            match('*'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OP_MUL

    // $ANTLR start ID
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            // UnnamedLanguage.g:156:3: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // UnnamedLanguage.g:156:5: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // UnnamedLanguage.g:156:29: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // UnnamedLanguage.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ID

    // $ANTLR start INTEGER_CONSTANT
    public final void mINTEGER_CONSTANT() throws RecognitionException {
        try {
            int _type = INTEGER_CONSTANT;
            // UnnamedLanguage.g:159:2: ( ( '0' | ( '1' .. '9' ( '0' .. '9' )* ) ) )
            // UnnamedLanguage.g:159:4: ( '0' | ( '1' .. '9' ( '0' .. '9' )* ) )
            {
            // UnnamedLanguage.g:159:4: ( '0' | ( '1' .. '9' ( '0' .. '9' )* ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='0') ) {
                alt3=1;
            }
            else if ( ((LA3_0>='1' && LA3_0<='9')) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("159:4: ( '0' | ( '1' .. '9' ( '0' .. '9' )* ) )", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // UnnamedLanguage.g:159:5: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // UnnamedLanguage.g:159:11: ( '1' .. '9' ( '0' .. '9' )* )
                    {
                    // UnnamedLanguage.g:159:11: ( '1' .. '9' ( '0' .. '9' )* )
                    // UnnamedLanguage.g:159:12: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); 
                    // UnnamedLanguage.g:159:20: ( '0' .. '9' )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // UnnamedLanguage.g:159:21: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    }


                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INTEGER_CONSTANT

    // $ANTLR start FLOAT_CONSTANT
    public final void mFLOAT_CONSTANT() throws RecognitionException {
        try {
            int _type = FLOAT_CONSTANT;
            // UnnamedLanguage.g:163:2: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
            int alt10=3;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // UnnamedLanguage.g:163:4: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )?
                    {
                    // UnnamedLanguage.g:163:4: ( '0' .. '9' )+
                    int cnt4=0;
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // UnnamedLanguage.g:163:5: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt4 >= 1 ) break loop4;
                                EarlyExitException eee =
                                    new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);

                    match('.'); 
                    // UnnamedLanguage.g:163:20: ( '0' .. '9' )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // UnnamedLanguage.g:163:21: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    // UnnamedLanguage.g:163:32: ( EXPONENT )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0=='E'||LA6_0=='e') ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // UnnamedLanguage.g:163:32: EXPONENT
                            {
                            mEXPONENT(); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // UnnamedLanguage.g:164:4: '.' ( '0' .. '9' )+ ( EXPONENT )?
                    {
                    match('.'); 
                    // UnnamedLanguage.g:164:8: ( '0' .. '9' )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // UnnamedLanguage.g:164:9: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);

                    // UnnamedLanguage.g:164:20: ( EXPONENT )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='E'||LA8_0=='e') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // UnnamedLanguage.g:164:20: EXPONENT
                            {
                            mEXPONENT(); 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // UnnamedLanguage.g:165:4: ( '0' .. '9' )+ EXPONENT
                    {
                    // UnnamedLanguage.g:165:4: ( '0' .. '9' )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // UnnamedLanguage.g:165:5: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt9 >= 1 ) break loop9;
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);

                    mEXPONENT(); 

                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FLOAT_CONSTANT

    // $ANTLR start STRING_CONSTANT
    public final void mSTRING_CONSTANT() throws RecognitionException {
        try {
            int _type = STRING_CONSTANT;
            // UnnamedLanguage.g:169:2: ( '\"' ( ESC_SEQ | ~ ( '\\\\' | '\"' ) )* '\"' )
            // UnnamedLanguage.g:169:5: '\"' ( ESC_SEQ | ~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 
            // UnnamedLanguage.g:169:9: ( ESC_SEQ | ~ ( '\\\\' | '\"' ) )*
            loop11:
            do {
                int alt11=3;
                int LA11_0 = input.LA(1);

                if ( (LA11_0=='\\') ) {
                    alt11=1;
                }
                else if ( ((LA11_0>='\u0000' && LA11_0<='!')||(LA11_0>='#' && LA11_0<='[')||(LA11_0>=']' && LA11_0<='\uFFFE')) ) {
                    alt11=2;
                }


                switch (alt11) {
            	case 1 :
            	    // UnnamedLanguage.g:169:11: ESC_SEQ
            	    {
            	    mESC_SEQ(); 

            	    }
            	    break;
            	case 2 :
            	    // UnnamedLanguage.g:169:21: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

            match('\"'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STRING_CONSTANT

    // $ANTLR start CHARACTER_CONSTANT
    public final void mCHARACTER_CONSTANT() throws RecognitionException {
        try {
            int _type = CHARACTER_CONSTANT;
            // UnnamedLanguage.g:173:2: ( '\\'' ( ESC_SEQ | ~ ( '\\'' | '\\\\' ) ) '\\'' )
            // UnnamedLanguage.g:173:5: '\\'' ( ESC_SEQ | ~ ( '\\'' | '\\\\' ) ) '\\''
            {
            match('\''); 
            // UnnamedLanguage.g:173:10: ( ESC_SEQ | ~ ( '\\'' | '\\\\' ) )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='\\') ) {
                alt12=1;
            }
            else if ( ((LA12_0>='\u0000' && LA12_0<='&')||(LA12_0>='(' && LA12_0<='[')||(LA12_0>=']' && LA12_0<='\uFFFE')) ) {
                alt12=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("173:10: ( ESC_SEQ | ~ ( '\\'' | '\\\\' ) )", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // UnnamedLanguage.g:173:12: ESC_SEQ
                    {
                    mESC_SEQ(); 

                    }
                    break;
                case 2 :
                    // UnnamedLanguage.g:173:22: ~ ( '\\'' | '\\\\' )
                    {
                    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;

            }

            match('\''); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end CHARACTER_CONSTANT

    // $ANTLR start EXPONENT
    public final void mEXPONENT() throws RecognitionException {
        try {
            // UnnamedLanguage.g:176:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // UnnamedLanguage.g:176:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // UnnamedLanguage.g:176:22: ( '+' | '-' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='+'||LA13_0=='-') ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // UnnamedLanguage.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;

            }

            // UnnamedLanguage.g:176:33: ( '0' .. '9' )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>='0' && LA14_0<='9')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // UnnamedLanguage.g:176:34: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt14 >= 1 ) break loop14;
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end EXPONENT

    // $ANTLR start HEX_DIGIT
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // UnnamedLanguage.g:179:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // UnnamedLanguage.g:179:13: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
        }
    }
    // $ANTLR end HEX_DIGIT

    // $ANTLR start ESC_SEQ
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // UnnamedLanguage.g:183:2: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt15=3;
            int LA15_0 = input.LA(1);

            if ( (LA15_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt15=1;
                    }
                    break;
                case 'u':
                    {
                    alt15=2;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt15=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("181:1: fragment ESC_SEQ : ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC );", 15, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("181:1: fragment ESC_SEQ : ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC );", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // UnnamedLanguage.g:183:4: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 
                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;
                case 2 :
                    // UnnamedLanguage.g:184:4: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 

                    }
                    break;
                case 3 :
                    // UnnamedLanguage.g:185:4: OCTAL_ESC
                    {
                    mOCTAL_ESC(); 

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end ESC_SEQ

    // $ANTLR start OCTAL_ESC
    public final void mOCTAL_ESC() throws RecognitionException {
        try {
            // UnnamedLanguage.g:190:2: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt16=3;
            int LA16_0 = input.LA(1);

            if ( (LA16_0=='\\') ) {
                int LA16_1 = input.LA(2);

                if ( ((LA16_1>='0' && LA16_1<='3')) ) {
                    int LA16_2 = input.LA(3);

                    if ( ((LA16_2>='0' && LA16_2<='7')) ) {
                        int LA16_5 = input.LA(4);

                        if ( ((LA16_5>='0' && LA16_5<='7')) ) {
                            alt16=1;
                        }
                        else {
                            alt16=2;}
                    }
                    else {
                        alt16=3;}
                }
                else if ( ((LA16_1>='4' && LA16_1<='7')) ) {
                    int LA16_3 = input.LA(3);

                    if ( ((LA16_3>='0' && LA16_3<='7')) ) {
                        alt16=2;
                    }
                    else {
                        alt16=3;}
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("188:1: fragment OCTAL_ESC : ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) );", 16, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("188:1: fragment OCTAL_ESC : ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) );", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // UnnamedLanguage.g:190:4: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // UnnamedLanguage.g:190:9: ( '0' .. '3' )
                    // UnnamedLanguage.g:190:10: '0' .. '3'
                    {
                    matchRange('0','3'); 

                    }

                    // UnnamedLanguage.g:190:20: ( '0' .. '7' )
                    // UnnamedLanguage.g:190:21: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // UnnamedLanguage.g:190:31: ( '0' .. '7' )
                    // UnnamedLanguage.g:190:32: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 2 :
                    // UnnamedLanguage.g:191:4: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // UnnamedLanguage.g:191:9: ( '0' .. '7' )
                    // UnnamedLanguage.g:191:10: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // UnnamedLanguage.g:191:20: ( '0' .. '7' )
                    // UnnamedLanguage.g:191:21: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 3 :
                    // UnnamedLanguage.g:192:4: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 
                    // UnnamedLanguage.g:192:9: ( '0' .. '7' )
                    // UnnamedLanguage.g:192:10: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end OCTAL_ESC

    // $ANTLR start COMMENT
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            // UnnamedLanguage.g:196:2: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // UnnamedLanguage.g:196:4: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); 

            // UnnamedLanguage.g:196:9: (~ ( '\\n' | '\\r' ) )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>='\u0000' && LA17_0<='\t')||(LA17_0>='\u000B' && LA17_0<='\f')||(LA17_0>='\u000E' && LA17_0<='\uFFFE')) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // UnnamedLanguage.g:196:9: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            // UnnamedLanguage.g:196:23: ( '\\r' )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0=='\r') ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // UnnamedLanguage.g:196:23: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            channel=HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMENT

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // UnnamedLanguage.g:199:1: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // UnnamedLanguage.g:199:3: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            channel=HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WS

    // $ANTLR start UNICODE_ESC
    public final void mUNICODE_ESC() throws RecognitionException {
        try {
            // UnnamedLanguage.g:208:2: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // UnnamedLanguage.g:208:4: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
            {
            match('\\'); 
            match('u'); 
            mHEX_DIGIT(); 
            mHEX_DIGIT(); 
            mHEX_DIGIT(); 
            mHEX_DIGIT(); 

            }

        }
        finally {
        }
    }
    // $ANTLR end UNICODE_ESC

    public void mTokens() throws RecognitionException {
        // UnnamedLanguage.g:1:8: ( KW_TRUE | KW_FALSE | KW_IF | KW_ELSE | KW_WHILE | KW_PRINT | KW_PRINTLN | KW_RETURN | KW_INT | KW_FLOAT | KW_CHAR | KW_STRING | KW_BOOLEAN | KW_VOID | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | OP_EQUALS | OP_PLUS | OP_MINUS | OP_LESSTHAN | OP_MUL | ID | INTEGER_CONSTANT | FLOAT_CONSTANT | STRING_CONSTANT | CHARACTER_CONSTANT | COMMENT | WS )
        int alt19=35;
        alt19 = dfa19.predict(input);
        switch (alt19) {
            case 1 :
                // UnnamedLanguage.g:1:10: KW_TRUE
                {
                mKW_TRUE(); 

                }
                break;
            case 2 :
                // UnnamedLanguage.g:1:18: KW_FALSE
                {
                mKW_FALSE(); 

                }
                break;
            case 3 :
                // UnnamedLanguage.g:1:27: KW_IF
                {
                mKW_IF(); 

                }
                break;
            case 4 :
                // UnnamedLanguage.g:1:33: KW_ELSE
                {
                mKW_ELSE(); 

                }
                break;
            case 5 :
                // UnnamedLanguage.g:1:41: KW_WHILE
                {
                mKW_WHILE(); 

                }
                break;
            case 6 :
                // UnnamedLanguage.g:1:50: KW_PRINT
                {
                mKW_PRINT(); 

                }
                break;
            case 7 :
                // UnnamedLanguage.g:1:59: KW_PRINTLN
                {
                mKW_PRINTLN(); 

                }
                break;
            case 8 :
                // UnnamedLanguage.g:1:70: KW_RETURN
                {
                mKW_RETURN(); 

                }
                break;
            case 9 :
                // UnnamedLanguage.g:1:80: KW_INT
                {
                mKW_INT(); 

                }
                break;
            case 10 :
                // UnnamedLanguage.g:1:87: KW_FLOAT
                {
                mKW_FLOAT(); 

                }
                break;
            case 11 :
                // UnnamedLanguage.g:1:96: KW_CHAR
                {
                mKW_CHAR(); 

                }
                break;
            case 12 :
                // UnnamedLanguage.g:1:104: KW_STRING
                {
                mKW_STRING(); 

                }
                break;
            case 13 :
                // UnnamedLanguage.g:1:114: KW_BOOLEAN
                {
                mKW_BOOLEAN(); 

                }
                break;
            case 14 :
                // UnnamedLanguage.g:1:125: KW_VOID
                {
                mKW_VOID(); 

                }
                break;
            case 15 :
                // UnnamedLanguage.g:1:133: T35
                {
                mT35(); 

                }
                break;
            case 16 :
                // UnnamedLanguage.g:1:137: T36
                {
                mT36(); 

                }
                break;
            case 17 :
                // UnnamedLanguage.g:1:141: T37
                {
                mT37(); 

                }
                break;
            case 18 :
                // UnnamedLanguage.g:1:145: T38
                {
                mT38(); 

                }
                break;
            case 19 :
                // UnnamedLanguage.g:1:149: T39
                {
                mT39(); 

                }
                break;
            case 20 :
                // UnnamedLanguage.g:1:153: T40
                {
                mT40(); 

                }
                break;
            case 21 :
                // UnnamedLanguage.g:1:157: T41
                {
                mT41(); 

                }
                break;
            case 22 :
                // UnnamedLanguage.g:1:161: T42
                {
                mT42(); 

                }
                break;
            case 23 :
                // UnnamedLanguage.g:1:165: T43
                {
                mT43(); 

                }
                break;
            case 24 :
                // UnnamedLanguage.g:1:169: OP_EQUALS
                {
                mOP_EQUALS(); 

                }
                break;
            case 25 :
                // UnnamedLanguage.g:1:179: OP_PLUS
                {
                mOP_PLUS(); 

                }
                break;
            case 26 :
                // UnnamedLanguage.g:1:187: OP_MINUS
                {
                mOP_MINUS(); 

                }
                break;
            case 27 :
                // UnnamedLanguage.g:1:196: OP_LESSTHAN
                {
                mOP_LESSTHAN(); 

                }
                break;
            case 28 :
                // UnnamedLanguage.g:1:208: OP_MUL
                {
                mOP_MUL(); 

                }
                break;
            case 29 :
                // UnnamedLanguage.g:1:215: ID
                {
                mID(); 

                }
                break;
            case 30 :
                // UnnamedLanguage.g:1:218: INTEGER_CONSTANT
                {
                mINTEGER_CONSTANT(); 

                }
                break;
            case 31 :
                // UnnamedLanguage.g:1:235: FLOAT_CONSTANT
                {
                mFLOAT_CONSTANT(); 

                }
                break;
            case 32 :
                // UnnamedLanguage.g:1:250: STRING_CONSTANT
                {
                mSTRING_CONSTANT(); 

                }
                break;
            case 33 :
                // UnnamedLanguage.g:1:266: CHARACTER_CONSTANT
                {
                mCHARACTER_CONSTANT(); 

                }
                break;
            case 34 :
                // UnnamedLanguage.g:1:285: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 35 :
                // UnnamedLanguage.g:1:293: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    protected DFA19 dfa19 = new DFA19(this);
    static final String DFA10_eotS =
        "\5\uffff";
    static final String DFA10_eofS =
        "\5\uffff";
    static final String DFA10_minS =
        "\2\56\3\uffff";
    static final String DFA10_maxS =
        "\1\71\1\145\3\uffff";
    static final String DFA10_acceptS =
        "\2\uffff\1\2\1\1\1\3";
    static final String DFA10_specialS =
        "\5\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
            "",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "162:1: FLOAT_CONSTANT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT );";
        }
    }
    static final String DFA19_eotS =
        "\1\uffff\13\31\10\uffff\1\57\5\uffff\2\60\5\uffff\4\31\1\66\10\31"+
        "\3\uffff\1\60\3\31\1\102\1\uffff\10\31\1\113\2\31\1\uffff\1\116"+
        "\3\31\1\122\2\31\1\125\1\uffff\1\126\1\127\1\uffff\1\130\1\132\1"+
        "\31\1\uffff\2\31\4\uffff\1\31\1\uffff\1\137\1\140\1\31\1\142\2\uffff"+
        "\1\143\2\uffff";
    static final String DFA19_eofS =
        "\144\uffff";
    static final String DFA19_minS =
        "\1\11\1\162\1\141\1\146\1\154\1\150\1\162\1\145\1\150\1\164\2\157"+
        "\10\uffff\1\75\5\uffff\2\56\5\uffff\1\165\1\157\1\154\1\164\1\60"+
        "\1\163\2\151\1\164\1\141\1\162\1\157\1\151\3\uffff\1\56\1\145\1"+
        "\141\1\163\1\60\1\uffff\1\145\1\154\1\156\1\165\1\162\1\151\1\154"+
        "\1\144\1\60\1\164\1\145\1\uffff\1\60\1\145\1\164\1\162\1\60\1\156"+
        "\1\145\1\60\1\uffff\2\60\1\uffff\2\60\1\156\1\uffff\1\147\1\141"+
        "\4\uffff\1\156\1\uffff\2\60\1\156\1\60\2\uffff\1\60\2\uffff";
    static final String DFA19_maxS =
        "\1\175\1\162\1\154\1\156\1\154\1\150\1\162\1\145\1\150\1\164\2\157"+
        "\10\uffff\1\75\5\uffff\2\145\5\uffff\1\165\1\157\1\154\1\164\1\172"+
        "\1\163\2\151\1\164\1\141\1\162\1\157\1\151\3\uffff\2\145\1\141\1"+
        "\163\1\172\1\uffff\1\145\1\154\1\156\1\165\1\162\1\151\1\154\1\144"+
        "\1\172\1\164\1\145\1\uffff\1\172\1\145\1\164\1\162\1\172\1\156\1"+
        "\145\1\172\1\uffff\2\172\1\uffff\2\172\1\156\1\uffff\1\147\1\141"+
        "\4\uffff\1\156\1\uffff\2\172\1\156\1\172\2\uffff\1\172\2\uffff";
    static final String DFA19_acceptS =
        "\14\uffff\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\uffff\1\31\1"+
        "\32\1\33\1\34\1\35\2\uffff\1\37\1\40\1\41\1\42\1\43\15\uffff\1\30"+
        "\1\27\1\36\5\uffff\1\3\13\uffff\1\11\10\uffff\1\1\2\uffff\1\4\3"+
        "\uffff\1\13\2\uffff\1\16\1\12\1\2\1\5\1\uffff\1\6\4\uffff\1\10\1"+
        "\14\1\uffff\1\7\1\15";
    static final String DFA19_specialS =
        "\144\uffff}>";
    static final String[] DFA19_transitionS = {
            "\2\40\2\uffff\1\40\22\uffff\1\40\1\uffff\1\35\4\uffff\1\36\1"+
            "\14\1\15\1\30\1\25\1\16\1\26\1\34\1\37\1\32\11\33\1\uffff\1"+
            "\21\1\27\1\24\3\uffff\32\31\1\22\1\uffff\1\23\1\uffff\1\31\1"+
            "\uffff\1\31\1\12\1\10\1\31\1\4\1\2\2\31\1\3\6\31\1\6\1\31\1"+
            "\7\1\11\1\1\1\31\1\13\1\5\3\31\1\17\1\uffff\1\20",
            "\1\41",
            "\1\43\12\uffff\1\42",
            "\1\45\7\uffff\1\44",
            "\1\46",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\56",
            "",
            "",
            "",
            "",
            "",
            "\1\34\1\uffff\12\34\13\uffff\1\34\37\uffff\1\34",
            "\1\34\1\uffff\12\61\13\uffff\1\34\37\uffff\1\34",
            "",
            "",
            "",
            "",
            "",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "",
            "",
            "",
            "\1\34\1\uffff\12\61\13\uffff\1\34\37\uffff\1\34",
            "\1\77",
            "\1\100",
            "\1\101",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\114",
            "\1\115",
            "",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\117",
            "\1\120",
            "\1\121",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\123",
            "\1\124",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\13\31\1\131\16\31",
            "\1\133",
            "",
            "\1\134",
            "\1\135",
            "",
            "",
            "",
            "",
            "\1\136",
            "",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "\1\141",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "",
            "",
            "\12\31\7\uffff\32\31\4\uffff\1\31\1\uffff\32\31",
            "",
            ""
    };

    static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
    static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
    static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
    static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
    static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
    static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
    static final short[][] DFA19_transition;

    static {
        int numStates = DFA19_transitionS.length;
        DFA19_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
        }
    }

    class DFA19 extends DFA {

        public DFA19(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 19;
            this.eot = DFA19_eot;
            this.eof = DFA19_eof;
            this.min = DFA19_min;
            this.max = DFA19_max;
            this.accept = DFA19_accept;
            this.special = DFA19_special;
            this.transition = DFA19_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( KW_TRUE | KW_FALSE | KW_IF | KW_ELSE | KW_WHILE | KW_PRINT | KW_PRINTLN | KW_RETURN | KW_INT | KW_FLOAT | KW_CHAR | KW_STRING | KW_BOOLEAN | KW_VOID | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | OP_EQUALS | OP_PLUS | OP_MINUS | OP_LESSTHAN | OP_MUL | ID | INTEGER_CONSTANT | FLOAT_CONSTANT | STRING_CONSTANT | CHARACTER_CONSTANT | COMMENT | WS );";
        }
    }
 

}