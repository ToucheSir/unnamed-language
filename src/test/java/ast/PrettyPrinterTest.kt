package ast

import junit.framework.TestCase.assertEquals
import org.antlr.runtime.ANTLRInputStream
import org.antlr.runtime.CommonTokenStream
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import parser.UnnamedLanguageLexer
import parser.UnnamedLanguageParser
import java.io.PrintStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

class PrettyPrinterTest {
    @Rule
    @JvmField
    val tmp = TemporaryFolder()

    private fun formatAndCompare(sourceFileName: String) {
        val classLoader = javaClass.classLoader
        val input = ANTLRInputStream(classLoader.getResourceAsStream(sourceFileName))

        val lexer = UnnamedLanguageLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = UnnamedLanguageParser(tokens)
        val ast = parser.program()

        val outputFile = tmp.newFile()
        val fmt = PrettyPrinter(PrintStream(outputFile, "UTF-8"))
        fmt.process(ast)

        val formattedFilePath = Paths.get(classLoader.getResource("formatted/" + sourceFileName).toURI())
        assertEquals(
            String(Files.readAllBytes(formattedFilePath), Charset.forName("UTF-8")),
            String(Files.readAllBytes(outputFile.toPath()), Charset.forName("UTF-8"))
        )
    }

    @Test
    fun testEmptyMain() = formatAndCompare("empty_main.ul")

    @Test
    fun testFunctionBody() = formatAndCompare("function_body.ul")

    @Test
    fun testTypes() = formatAndCompare("types.ul")

    @Test
    fun testBasicStatement() = formatAndCompare("basic_statement.ul")

    @Test
    fun testLiterals() = formatAndCompare("literals.ul")

    @Test
    fun testComplexExpression() = formatAndCompare("complex_expression.ul")

    @Test
    fun testComplexFunction() = formatAndCompare("complex_function.ul")

    @Test
    fun testWhitespace() = formatAndCompare("whitespace.ul")

    @Test
    fun testE2E() = formatAndCompare("e2e.ul")
}