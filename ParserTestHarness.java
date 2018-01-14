import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ParserTestHarness {
    // Happy path
    @Test
    public void testEmptyMain() throws IOException, RecognitionException {
        runParser("tests/empty_main.ul");
    }

    @Test
    public void testFunctionBody() throws IOException, RecognitionException {
        runParser("tests/function_body.ul");
    }

    @Test
    public void testTypes() throws IOException, RecognitionException {
        runParser("tests/types.ul");
    }

    @Test
    public void testBasicStatement() throws IOException, RecognitionException {
        runParser("tests/basic_statement.ul");
    }

    @Test
    public void testLiterals() throws IOException, RecognitionException {
        runParser("tests/literals.ul");
    }

    @Test
    public void testComplexExpression() throws IOException, RecognitionException {
        runParser("tests/complex_expression.ul");
    }

    @Test
    public void testComplexFunction() throws IOException, RecognitionException {
        runParser("tests/complex_function.ul");
    }

    @Test
    public void testWhitespace() throws IOException, RecognitionException {
        runParser("tests/whitespace.ul");
    }

    @Test
    public void testE2E() throws IOException, RecognitionException {
        runParser("tests/e2e.ul");
    }

    // Not-so-happy path
    @Test(expected = RecognitionException.class)
    public void testEmptyProgram() throws IOException, RecognitionException {
        runParser("tests/empty_program.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testCommentOnly() throws IOException, RecognitionException {
        runParser("tests/comment_only.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testNoParamsFunction() throws IOException, RecognitionException {
        runParser("tests/no_params_function.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testInvalidTypeDecl() throws IOException, RecognitionException {
        runParser("tests/invalid_type_decl.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testNameKeywordConflict() throws IOException, RecognitionException {
        runParser("tests/name_keyword_conflict.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testNoSemiStatement() throws IOException, RecognitionException {
        runParser("tests/no_semi_statement.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testStatementBeforeDecl() throws IOException, RecognitionException {
        runParser("tests/statement_before_decl.ul");
    }

    private static void runParser(String fixturePath) throws IOException, RecognitionException {
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(fixturePath));

        // The name of the grammar here is "UnnamedLanguage",
        // so ANTLR generates UnnamedLanguageLexer and UnnamedLanguageParser
        UnnamedLanguageLexer lexer = new UnnamedLanguageLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        UnnamedLanguageParser parser = new UnnamedLanguageParser(tokens);

        parser.program();
    }
}