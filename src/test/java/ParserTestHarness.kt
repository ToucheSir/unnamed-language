import ast.*
import ast.dsl.*
import org.antlr.runtime.ANTLRInputStream
import org.antlr.runtime.CommonTokenStream
import org.antlr.runtime.RecognitionException
import org.junit.Test

import java.io.IOException

import org.junit.Assert.*
import type.*

class ParserTestHarness {
    // Happy path
    @Test
    fun testEmptyMain() {
        val actual = runParser("empty_main.ul")
        val expected = program {
            function(VoidType, "main") { }
        }
        assertEquals(expected, actual)
    }

    @Test
    fun testFunctionBody() {
        val actual = runParser("function_body.ul")
        val expected = program {
            function(VoidType, "empty") {}
            function(VoidType, "hasDecls") {
                varDecl(IntegerType, "a")
                varDecl(FloatType, "b")
            }
            function(VoidType, "hasStatements") {
                statement("a".toId().assign(!"b".toId() + 1.toLit()))
                statement(PrintlnStatement(!"c".toId()))
            }
            function(VoidType, "hasDeclsStatements") {
                val a = varDecl(IntegerType, "a")
                val b = varDecl(FloatType, "b")
                statement(a.assign(!b + 1.toLit()))
                statement(PrintlnStatement(!"c".toId()))
            }
            function(VoidType, "main") {
                statement(ExpressionStatement(FunctionCall("empty".toId())))
                statement(ExpressionStatement(FunctionCall("hasDecls".toId())))
                statement(ExpressionStatement(FunctionCall("hasStatements".toId())))
                statement(ExpressionStatement(FunctionCall("hasDeclsStatements".toId())))
            }
        }
        assertEquals(expected, actual)
    }

    @Test
    fun testTypes() {
        val actual = runParser("types.ul")
        val expected = program {
            function(VoidType, "main") {
                varDecl(IntegerType, "i")
                varDecl(FloatType, "f")
                varDecl(CharType, "c")
                varDecl(StringType, "s")
                varDecl(BooleanType, "b")

                varDecl(IntegerType[1], "is")
                varDecl(FloatType[2], "fs")
                varDecl(CharType[3], "cs")
                varDecl(StringType[4], "ss")
                varDecl(BooleanType[5], "bs")
            }
            function(VoidType, "takesAllTypes") {
                param(IntegerType, "i")
                param(FloatType, "f")
                param(CharType, "c")
                param(StringType, "s")
                param(BooleanType, "b")

                param(IntegerType[1], "is")
                param(FloatType[2], "fs")
                param(CharType[3], "cs")
                param(StringType[4], "ss")
                param(BooleanType[5], "bs")
            }
            function(IntegerType, "returnsInt") {}
            function(FloatType, "returnsFloat") {}
            function(CharType, "returnsChar") {}
            function(StringType, "returnsString") {}
            function(BooleanType, "returnsBoolean") {}

            function(IntegerType[1], "returnsInts") {}
            function(FloatType[2], "returnsFloats") {}
            function(CharType[3], "returnsChars") {}
            function(StringType[4], "returnsStrings") {}
            function(BooleanType[5], "returnsBooleans") {}
        }
        assertEquals(expected, actual)
    }

    @Test
    fun testBasicStatement() {
        val actual = runParser("basic_statement.ul")
        val expected = program {
            function(VoidType, "main") {
                statement(IfStatement(true.toLit(), Block()))
                statement(IfStatement(true.toLit(), Block(), Block()))
                statement(WhileStatement(false.toLit(), Block()))
                statement(PrintStatement("hi".toLit()))
                statement(PrintlnStatement("hello".toLit()))
                statement("foo".toId().assign(1.toLit()))
                statement("bar".toId().assignIndex(0.toLit(), !"baz".toId()))
            }
        }
        assertEquals(expected, actual)
    }

    @Test
    fun testLiterals() {
        val actual = runParser("literals.ul")
        val expected = program {
            function(VoidType, "main") {
                // Booleans
                statement(ExpressionStatement(true.toLit()))
                statement(ExpressionStatement(false.toLit()))

                // Integers
                statement(ExpressionStatement(0.toLit()))
                statement(ExpressionStatement(1.toLit()))
                statement(ExpressionStatement(42.toLit()))
                statement(ExpressionStatement(1234567890.toLit()))

                // Floats
                statement(ExpressionStatement(0.0f.toLit()))
                statement(ExpressionStatement(1.0f.toLit()))
                statement(ExpressionStatement(42.0f.toLit()))
                statement(ExpressionStatement(1234567890.0f.toLit()))
                statement(ExpressionStatement(0.12345f.toLit()))
                statement(ExpressionStatement(1.2345f.toLit()))
                statement(ExpressionStatement(12.345f.toLit()))
                // Shorthand notation
                statement(ExpressionStatement(.0f.toLit()))
                statement(ExpressionStatement(.1f.toLit()))
                statement(ExpressionStatement(.21345f.toLit()))
                // Scientific notation
                statement(ExpressionStatement(0.0E25f.toLit()))
                statement(ExpressionStatement(.0E-24f.toLit()))
                statement(ExpressionStatement(.1E12f.toLit()))
                statement(ExpressionStatement(5E1f.toLit()))

                // Characters
                statement(ExpressionStatement('0'.toLit()))
                statement(ExpressionStatement('a'.toLit()))
                statement(ExpressionStatement('_'.toLit()))
                statement(ExpressionStatement('\t'.toLit()))
                statement(ExpressionStatement('\n'.toLit()))
                statement(ExpressionStatement(0.toChar().toLit()))
                statement(ExpressionStatement('\\'.toLit()))
                // Unicode literals
                statement(ExpressionStatement('\u2012'.toLit()))

                // Strings
                statement(ExpressionStatement("".toLit()))
                statement(ExpressionStatement("0".toLit()))
                statement(ExpressionStatement("a".toLit()))
                statement(ExpressionStatement("_".toLit()))
                statement(ExpressionStatement("\t".toLit()))
                statement(ExpressionStatement("\n".toLit()))
                statement(ExpressionStatement(0.toChar().toString().toLit()))
                statement(ExpressionStatement("\\".toLit()))
                statement(ExpressionStatement("string".toLit()))
                // Unicode literals
                statement(ExpressionStatement("\u2011\u2012".toLit()))
                statement(ExpressionStatement("The quick brown fox jumped over the lazy dog".toLit()))
            }
        }
        assertEquals(expected, actual)
    }

    @Test
    fun testComplexExpression() {
        val actual = runParser("complex_expression.ul")
        val expected = program {
            function(VoidType, "main") {
                val a = varDecl(IntegerType, "a")
                val b = varDecl(IntegerType, "b")
                val c = varDecl(BooleanType, "c")
                val d = varDecl(BooleanType[3], "d")

                statement(a.assign(1.toLit()))
                statement(b.assign(!a - 4.toLit()))

                // a = a * b + 5 - 21 * b * b * a;
                statement(a.assign(!a * !b + 5.toLit() - 21.toLit() * !b * !b * !a))

                // c = a == b + 5 * 1 * (a + 1) - 3 < 5;
                statement(c.assign(run {
                    val sub1 = 5.toLit() * 1.toLit() * ParenExpression(!a + 1.toLit())
                    val sub2 = !b + sub1 - 3.toLit()
                    !a eq (sub2 lt 5.toLit())
                }))

                // d[1] = a < c == b + func() - 5 * (func(d[2])) + d[3] == (d[0]) == false + ('a' - 'b');
                statement(d.assignIndex(1.toLit(), run {
                    val func = "func".toId()
                    val sub1 = !a lt !c
                    val sub2 = !b + func() - 5.toLit() * ParenExpression(func(d[2.toLit()])) + d[3.toLit()]
                    val sub3 = ParenExpression(d[0.toLit()])
                    val sub4 = false.toLit() + ParenExpression('a'.toLit() - 'b'.toLit())
                    sub1 eq sub2 eq sub3 eq sub4
                }))

                statement(PrintlnStatement("foo ".toLit() + "bar \n".toLit()))

                // (a*n()*(s==((('4')))+'2'));
                statement(ExpressionStatement(run {
                    val n = "n".toId()
                    val s = "s".toId()
                    var sub1: Expression = '4'.toLit()
                    repeat(3) {
                        sub1 = ParenExpression(sub1)
                    }
                    val sub2 = ParenExpression(!s eq sub1 + '2'.toLit())
                    ParenExpression(!a * n() * sub2)
                }))
            }
        }
        assertEquals(expected, actual)
    }

    @Test
    fun testComplexFunction() {
        val actual = runParser("complex_function.ul")
        val expected = program {
            // Unfortunately, we can't use function() because of the recursive reference
            val factorial = "factorial".toId()
            function(IntegerType, "factorial") {
                val n = param(IntegerType, "n")
                statement(IfStatement(!n eq 1.toLit(), Block().apply {
                    add(ReturnStatement(1.toLit()))
                }, Block().apply {
                    add(ReturnStatement(!n * factorial(!n - 1.toLit())))
                }))
            }
            function(VoidType, "main") {
                statement(PrintStatement("The factorial of 8 is ".toLit()))
                statement(PrintlnStatement(factorial(8.toLit())))
            }
        }
        assertEquals(expected, actual)
    }

    @Test
    fun testWhitespace() {
        val actual = runParser("whitespace.ul")
        val expected = program {
            function(VoidType, "main") {
                param(IntegerType, "argc")
                param(StringType[3], "argv")
                statement(PrintlnStatement("Hello, world!".toLit()))
            }
            function(IntegerType, "negate") {
                val i = param(IntegerType, "i")
                statement(ReturnStatement(0.0f.toLit() - !i))
            }
            function(BooleanType, "and") {
                val a = param(BooleanType, "a")
                val b = param(BooleanType, "b")
                statement(IfStatement(!a, Block().apply {
                    add(IfStatement(!b, Block().apply {
                        add(ReturnStatement(true.toLit()))
                    }))
                }))
                statement(ReturnStatement(false.toLit()))
            }
        }
        assertEquals(expected, actual)
    }

    @Test
    fun testE2E() {
        // TODO do we want to type the entire AST for this test? Seems pretty excessive, but...
        runParser("e2e.ul")
    }

    // Not-so-happy path
    @Test(expected = RecognitionException::class)
    @Throws(IOException::class, RecognitionException::class)
    fun testEmptyProgram() {
        runParser("empty_program.ul")
    }

    @Test(expected = RecognitionException::class)
    @Throws(IOException::class, RecognitionException::class)
    fun testCommentOnly() {
        runParser("comment_only.ul")
    }

    @Test(expected = RecognitionException::class)
    @Throws(IOException::class, RecognitionException::class)
    fun testNoParamsFunction() {
        runParser("no_params_function.ul")
    }

    @Test(expected = RecognitionException::class)
    @Throws(IOException::class, RecognitionException::class)
    fun testInvalidTypeDecl() {
        runParser("invalid_type_decl.ul")
    }

    @Test(expected = RecognitionException::class)
    @Throws(IOException::class, RecognitionException::class)
    fun testNameKeywordConflict() {
        runParser("name_keyword_conflict.ul")
    }

    @Test(expected = RecognitionException::class)
    @Throws(IOException::class, RecognitionException::class)
    fun testNoSemiStatement() {
        runParser("no_semi_statement.ul")
    }

    @Test(expected = RecognitionException::class)
    @Throws(IOException::class, RecognitionException::class)
    fun testStatementBeforeDecl() {
        runParser("statement_before_decl.ul")
    }

    @Throws(IOException::class, RecognitionException::class)
    private fun runParser(fixturePath: String): ASTNode {
        val classLoader = javaClass.classLoader
        val input = ANTLRInputStream(classLoader.getResourceAsStream(fixturePath))

        // The name of the grammar here is "UnnamedLanguage",
        // so ANTLR generates UnnamedLanguageLexer and UnnamedLanguageParser
        val lexer = UnnamedLanguageLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = UnnamedLanguageParser(tokens)

        return parser.program()
    }
}
