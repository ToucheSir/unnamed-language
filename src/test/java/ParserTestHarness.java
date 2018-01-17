import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ParserTestHarness {
    // Happy path
    @Test
    public void testEmptyMain() throws IOException, RecognitionException {
        runParser("empty_main.ul");
    }

    @Test
    public void testFunctionBody() throws IOException, RecognitionException {
        runParser("function_body.ul");
    }

    @Test
    public void testTypes() throws IOException, RecognitionException {
        runParser("types.ul");
    }

    @Test
    public void testBasicStatement() throws IOException, RecognitionException {
        runParser("basic_statement.ul");
    }

    @Test
    public void testLiterals() throws IOException, RecognitionException {
        runParser("literals.ul");
    }

    @Test
    public void testComplexExpression() throws IOException, RecognitionException {
        runParser("complex_expression.ul");
    }

    @Test
    public void testComplexFunction() throws IOException, RecognitionException {
        runParser("complex_function.ul");
    }

    @Test
    public void testWhitespace() throws IOException, RecognitionException {
        runParser("whitespace.ul");
    }

    @Test
    public void testE2E() throws IOException, RecognitionException {
        runParser("e2e.ul");
    }

    // Not-so-happy path
    @Test(expected = RecognitionException.class)
    public void testEmptyProgram() throws IOException, RecognitionException {
        runParser("empty_program.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testCommentOnly() throws IOException, RecognitionException {
        runParser("comment_only.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testNoParamsFunction() throws IOException, RecognitionException {
        runParser("no_params_function.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testInvalidTypeDecl() throws IOException, RecognitionException {
        runParser("invalid_type_decl.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testNameKeywordConflict() throws IOException, RecognitionException {
        runParser("name_keyword_conflict.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testNoSemiStatement() throws IOException, RecognitionException {
        runParser("no_semi_statement.ul");
    }

    @Test(expected = RecognitionException.class)
    public void testStatementBeforeDecl() throws IOException, RecognitionException {
        runParser("statement_before_decl.ul");
    }

    private void runParser(String fixturePath) throws IOException, RecognitionException {
        ClassLoader classLoader = getClass().getClassLoader();
        ANTLRInputStream input = new ANTLRInputStream(classLoader.getResourceAsStream(fixturePath));

        // The name of the grammar here is "UnnamedLanguage",
        // so ANTLR generates UnnamedLanguageLexer and UnnamedLanguageParser
        UnnamedLanguageLexer lexer = new UnnamedLanguageLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        UnnamedLanguageParser parser = new UnnamedLanguageParser(tokens);

        parser.program();
    }
}