package semantic

import org.antlr.runtime.ANTLRInputStream
import org.antlr.runtime.CommonTokenStream
import org.hamcrest.CoreMatchers.*
import org.hamcrest.beans.HasPropertyWithValue.hasProperty
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import parser.UnnamedLanguageLexer
import parser.UnnamedLanguageParser
import type.*
import java.io.ByteArrayInputStream

class TypeCheckerTest {
    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    private fun checkFile(sourceFileName: String) {
        val classLoader = javaClass.classLoader
        val input = ANTLRInputStream(classLoader.getResourceAsStream(sourceFileName))

        val lexer = UnnamedLanguageLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = UnnamedLanguageParser(tokens)
        val ast = parser.program()

        val checker = TypeChecker()
        checker.process(ast)
    }

    private fun check(input: String, errorMessage: String? = null, line: Int = 0, col: Int = 0) {
        val stream = ANTLRInputStream(ByteArrayInputStream(input.toByteArray()))
        val lexer = UnnamedLanguageLexer(stream)
        val tokens = CommonTokenStream(lexer)
        val parser = UnnamedLanguageParser(tokens)
        val ast = parser.program()

        val checker = TypeChecker()
        try {
            checker.process(ast)
            errorMessage?.apply { fail("semantic error expected") }
        } catch (e: SemanticException) {
            assertThat(
                e, allOf(
                    hasProperty("message", equalTo(errorMessage)),
                    hasProperty("line", equalTo(line)),
                    hasProperty("col", equalTo(col))
                )
            )
        }
    }

    @Test
    fun `1-1 One function must be called main`() = check(
        "void notMain() {}",
        "no main function found", 0, 0
    )

    @Test
    fun `1-2 main must take no parameters`() = check(
        "void main(int a) {}",
        "main function must not take any parameters", 1, 10
    )

    @Test
    fun `1-3 main must have a return "type" of void`() = check(
        "int main() {}",
        "main function must return void", 1, 0
    )

    @Test
    fun `1-4 No two functions may have the same name`() = check(
        """
        int main() {}
        void main() {}
        """.trimIndent(),
        "function with name 'main' already exists", 2, 5
    )

    @Test
    fun `2-1 No two parameters of a function may have the same name`() = check(
        """
        void twoArgs(int a, float a) {}
        void main() {}
        """.trimIndent(),
        "redefinition of parameter a", 1, 26
    )

    @Test
    fun `2-2 No two local variables declared in a function may have the same name`() = check(
        """
        void main() {
            int foo;
            float foo;
        }
        """.trimIndent(),
        "variable foo is already declared", 3, 10
    )

    @Test
    fun `2-3 No parameter may have a "type" of void`() {
        check(
            """
            void main() {}
            void foo(void arg) {}
            """.trimIndent(),
            "parameter arg must not be void", 2, 9
        )
        check(
            """
            void main() {}
            void foo(void[1] arg) {}
            """.trimIndent(),
            "parameter arg must not be void[1]", 2, 9
        )
    }

    @Test
    fun `2-4 No local variable may have a "type" of void`() {
        check(
            """
            void main() {
                void foo;
            }
            """.trimIndent(),
            "variable foo must not be void", 2, 9
        )
        check(
            """
            void main() {
                void[1] foo;
            }
            """.trimIndent(),
            "variable foo must not be void[1]", 2, 12
        )
    }

    @Test
    fun `2-5 A function parameter may hide the name of the function`() = check(
        """
        void foo(int foo) {}
        void main() {}
        """.trimIndent()
    )

    @Test
    fun `2-6 A local variable may not hide the name of a parameter`() = check(
        """
        void bar(int foo) {
            float foo;
        }
        void main() {}
        """.trimIndent(),
        "variable foo is already declared", 2, 10
    )

    @Test
    fun `2-7 Each local variable must be defined before being used`() = check(
        """
        void main() {
            println foo;
        }
        """.trimIndent(),
        "undeclared identifier foo", 2, 12
    )

    @Test
    fun `3-1 Atomic types must compare exactly`() {
        check(
            """
            void main() {
                int i;
                float f;
                char c;
                string s;
                boolean b;
                i = 0;
                f = 0.0;
                c = '0';
                s = "0";
                b = false;
            }
            """.trimIndent()
        )
        val lits = arrayOf("0", "0.0", "'0'", "\"0\"", "false")
        val types = arrayOf(IntegerType, FloatType, CharType, StringType, BooleanType)
        types.forEachIndexed { i, t ->
            lits.forEachIndexed { j, l ->
                if (i != j) check(
                    """
                    void main() {
                        $t var;
                        var = $l;
                    }
                    """.trimIndent(),
                    "cannot assign value of type ${types[j]} to variable of type $t",
                    3, 10
                )
            }
        }
    }

    @Test
    fun `3-2 Array types must compare structurally`() {
        check(
            """
            void main() {
                int[1] is1; int[1] is2;
                float[2] fs1; float[2] fs2;
                char[3] cs1; char[3] cs2;
                string[4] ss1; string[4] ss2;
                boolean[5] bs1; boolean[5] bs2;
                is1 = is2;
                fs1 = fs2;
                cs1 = cs2;
                ss1 = ss2;
                bs1 = bs2;
            }
            """.trimIndent()
        )
        val types = arrayOf(IntegerType, FloatType, CharType, StringType, BooleanType)
        val aTypes = types.map { ArrayType(it, 1) }
        for (t1 in aTypes) for (it in aTypes.filter { t1 != it }) check(
            """
            void main() {
                $t1 a1;
                $it a2;
                a1 = a2;
            }
            """.trimIndent(),
            "cannot assign value of type $it to variable of type $t1",
            4, 9
        )
        for ((at1, at2) in aTypes.zip(types.map { ArrayType(it, 2) })) check(
            """
            void main() {
                $at1 a1;
                $at2 a2;
                a1 = a2;
            }
            """.trimIndent(),
            "cannot assign value of type $at2 to variable of type $at1",
            4, 9
        )
    }

    @Test
    fun `4-1 An if-statement condition must have type boolean`() {
        check(
            """
            void main() {
                if (true) { }
            }
            """.trimIndent()
        )
        val lits = arrayOf("0", "0.0", "'0'", "\"0\"")
        val types = arrayOf(IntegerType, FloatType, CharType, StringType)
        for ((t, l) in types.zip(lits)) check(
            """
            void main() {
                if ($l) { }
            }
            """.trimIndent(),
            "if statement condition must be of type boolean, $t given", 2, 8
        )
    }

    @Test
    fun `4-2 An while-statement condition must have type boolean`() {
        check(
            """
            void main() {
                while (true) { }
            }
            """.trimIndent()
        )
        val lits = arrayOf("0", "0.0", "'0'", "\"0\"")
        val types = arrayOf(IntegerType, FloatType, CharType, StringType)
        for ((t, l) in types.zip(lits)) check(
            """
            void main() {
                while ($l) { }
            }
            """.trimIndent(),
            "while statement condition must be of type boolean, $t given", 2, 11
        )
    }

    @Test
    fun `4-3 A print statement value must be simple and non-void`() {
        check(
            """
            void main() {
                print main();
            }
            """.trimIndent(),
            "print does not support value of type void", 2, 14

        )
        check(
            """
            void main() {
                int[1] a;
                print a;
            }
            """.trimIndent(),
            "print does not support value of type int[1]", 3, 10

        )
        val types = arrayOf(IntegerType, FloatType, CharType, StringType, BooleanType)
        val lits = arrayOf("0", "0.0", "'0'", "\"0\"", "false")
        check(
            """
            void main() {
            ${types.mapIndexed { i, t ->
                "$t var$i; $t[1] aVar$i;"
            }.joinToString("\n")}
            ${types.indices.joinToString("\n") {
                "print var$it; print aVar$it[0];"
            }}
            ${lits.joinToString("\n") {
                "print $it;"
            }}
            }
            """.trimIndent()
        )
    }

    @Test
    fun `4-4 A println statement value must be simple and non-void`() {
        check(
            """
            void main() {
                println main();
            }
            """.trimIndent(),
            "println does not support value of type void", 2, 16

        )
        check(
            """
            void main() {
                int[1] a;
                println a;
            }
            """.trimIndent(),
            "println does not support value of type int[1]", 3, 12
        )
        val types = arrayOf(IntegerType, FloatType, CharType, StringType, BooleanType)
        val lits = arrayOf("0", "0.0", "'0'", "\"0\"", "false")
        check(
            """
            void main() {
            ${types.mapIndexed { i, t ->
                "$t var$i; $t[1] aVar$i;"
            }.joinToString("\n")}
            ${types.indices.joinToString("\n") {
                "println var$it; println aVar$it[0];"
            }}
            ${lits.joinToString("\n") {
                "println $it;"
            }}
            }
            """.trimIndent()
        )
    }

    @Test
    fun `4-5 A return statement value must match the function return type`() {
        val primitives = arrayOf(IntegerType, FloatType, CharType, StringType, BooleanType)
        val types = primitives + primitives.map { ArrayType(it, 1) }
        for (t in types) check(
            """
            void main() {}
            $t foo() {
                $t ret;
                return ret;
            }
            """.trimIndent()
        )
        for (t1 in types) for (it in types.filter { t1 != it }) check(
            """
            void main() {}
            $t1 foo() {
                $it ret;
                return ret;
            }
            """.trimIndent(),
            "cannot return value of type $it from function of type $t1",
            4, 11
        )
        check(
            """
            void main() {}
            int foo() {
                return;
            }
            """.trimIndent(),
            "missing return value", 3, 4
        )
    }

    @Test
    fun `5-1 A non-array type must not be indexed`() = check(
        """
        void main() {
            int a;
            a[0];
        }
        """.trimIndent(),
        "cannot index non-array value of type int", 3, 5
    )

    @Test
    fun `5-2 An array index expression must have type int`() {
        check(
            """
            void main() {
                int[1] a;
                a[foo()];
            }
            string foo() { return ""; }
            """.trimIndent(),
            "cannot index array with expression of type string", 3, 9
        )
        check(
            """
            void main() {
                int[1] a;
                a[bar()];
            }
            int bar() { return 0; }
            """.trimIndent()
        )
    }

    private fun checkOperator(op: String, typeTable: Map<Pair<Type, Type>, Type>) {
        val types = arrayOf(IntegerType, FloatType, CharType, StringType, BooleanType)
        val template =
            """
            void main() {
                ${types.joinToString(" ") { "$it ${it}Var;" }}
                %s
            }
            """.trimIndent()
        for (t1 in types) for (t2 in types) if (Pair(t1, t2) !in typeTable) {
            val tpl = template.format("${t1}Var $op ${t2}Var;")
            check(
                tpl,
                "cannot apply operator $op to types $t1 and $t2",
                3, "    ${t1}Var ".length
            )
        }
        for ((operands, res) in typeTable.entries) {
            val (t1, t2) = operands
            val tpl = template.format("${res}Var = ${t1}Var $op ${t2}Var;")
            check(tpl)
        }
    }

    @Test
    fun `5-3 The plus operator must have have the correct operand and result types`() {
        val typeTable = mutableMapOf<Pair<Type, Type>, Type>()
        typeTable[Pair(IntegerType, IntegerType)] = IntegerType
        typeTable[Pair(FloatType, FloatType)] = FloatType
        typeTable[Pair(CharType, CharType)] = CharType
        typeTable[Pair(StringType, StringType)] = StringType

        checkOperator("+", typeTable)
    }

    @Test
    fun `5-4 The minus operator have have the correct operand and result types`() {
        val typeTable = mutableMapOf<Pair<Type, Type>, Type>()
        typeTable[Pair(IntegerType, IntegerType)] = IntegerType
        typeTable[Pair(FloatType, FloatType)] = FloatType
        typeTable[Pair(CharType, CharType)] = CharType

        checkOperator("-", typeTable)
    }

    @Test
    fun `5-5 The multiply operator have have the correct operand and result types`() {
        val typeTable = mutableMapOf<Pair<Type, Type>, Type>()
        typeTable[Pair(IntegerType, IntegerType)] = IntegerType
        typeTable[Pair(FloatType, FloatType)] = FloatType

        checkOperator("*", typeTable)
    }

    @Test
    fun `5-6 The less than operator have have the correct operand and result types`() {
        val typeTable = mutableMapOf<Pair<Type, Type>, Type>()
        typeTable[Pair(IntegerType, IntegerType)] = BooleanType
        typeTable[Pair(FloatType, FloatType)] = BooleanType
        typeTable[Pair(CharType, CharType)] = BooleanType
        typeTable[Pair(StringType, StringType)] = BooleanType
        typeTable[Pair(BooleanType, BooleanType)] = BooleanType

        checkOperator("<", typeTable)
    }

    @Test
    fun `5-7 The equals operator have have the correct operand and result types`() {
        val typeTable = mutableMapOf<Pair<Type, Type>, Type>()
        typeTable[Pair(IntegerType, IntegerType)] = BooleanType
        typeTable[Pair(FloatType, FloatType)] = BooleanType
        typeTable[Pair(CharType, CharType)] = BooleanType
        typeTable[Pair(StringType, StringType)] = BooleanType
        typeTable[Pair(BooleanType, BooleanType)] = BooleanType

        checkOperator("==", typeTable)
    }

    @Test
    fun `5-8 Only functions may be invoked`() {
        check(
            """
            void main() {
                int a;
                a();
            }
            """.trimIndent(), "only functions may be invoked", 3, 5
        )
    }

    @Test
    fun `5-9 Function arguments must be the right length and the correct types`() {
        check(
            """
            void foo(string a, char b) {}
            void main() {
                foo("1", 'c', true);
            }
            """.trimIndent(),
            "function foo called with incorrect number of arguments (expected: string, char - actual: string, char, boolean)",
            3,
            7
        )

        check(
            """
            void foo(string a, char b) {}
            void main() {
                foo("1", true);
            }
            """.trimIndent(),
            "parameter has type char, boolean given",
            3,
            13
        )
    }
}