package ast

import org.apache.commons.text.StringEscapeUtils.escapeJava
import java.io.PrintStream

class PrettyPrinter(private val out: PrintStream) : ASTConsumer<Unit> {
    override fun process(node: ASTNode) = printTree(node)

    private inline fun <reified T> emit(msg: T, nestingLevel: Int = 0) =
        out.print("    ".repeat(nestingLevel) + msg)

    private inline fun <reified T> emitln(msg: T, nestingLevel: Int = 0) =
        out.println("    ".repeat(nestingLevel) + msg)

    private fun emitln() = out.println()

    private fun printTree(node: ASTNode, nestingLevel: Int = 0): Unit = when (node) {
        is Program -> node.forEach {
            printTree(it)
            emitln()
        }
        is Function -> {
            printTree(node.declaration)
            emitln()
            printTree(node.body)
        }
        is FunctionDeclaration -> {
            printTree(node.returnType)
            emit(' ')
            printTree(node.name)
            emit(' ')
            printTree(node.parameters)
        }
        is VariableDeclaration -> {
            printTree(node.type, nestingLevel)
            emit(' ')
            printTree(node.name)
            emitln(';')
        }
        is TypeNode -> emit(node.type, nestingLevel)
        is Identifier -> emit(node.name, nestingLevel)
        is FormalParameterList -> {
            emit('(')
            node.forEachIndexed { i, p ->
                printTree(p)
                if (i < node.size - 1) {
                    emit(", ")
                }
            }
            emit(')')
        }
        is FormalParameter -> {
            printTree(node.type)
            emit(' ', nestingLevel)
            printTree(node.name)
        }
        is FunctionBody -> {
            emitln('{')
            printTree(node.declarations, nestingLevel + 1)
            if (node.declarations.size > 0 && node.statements.size > 0) emitln()
            printTree(node.statements, nestingLevel + 1)
            emitln('}')
        }
        is Statement -> printStatement(node, nestingLevel)
        is Block -> {
            emitln('{', nestingLevel)
            node.forEach { printStatement(it, nestingLevel + 1) }
            emitln('}', nestingLevel)
        }
        is Expression -> printExpression(node, nestingLevel)
        is ExpressionList -> {
            node.forEachIndexed { i, e ->
                printExpression(e)
                if (i < node.size - 1) {
                    emit(", ")
                }
            }
        }
        is VariableDeclarationList -> node.forEach { printTree(it, nestingLevel) }
        is StatementList -> node.forEach { printTree(it, nestingLevel) }
    }

    private fun printStatement(stmt: Statement, nestingLevel: Int = 0): Unit = when (stmt) {
        is IfStatement -> run {
            emit("if (", nestingLevel)
            printExpression(stmt.cond)
            emitln(")")
            printTree(stmt.thenClause, nestingLevel)
            if (stmt.elseClause != null) {
                emitln("else", nestingLevel)
                printTree(stmt.elseClause, nestingLevel)
            }
        }
        is WhileStatement -> {
            emit("while (", nestingLevel)
            printExpression(stmt.cond)
            emitln(")")
            printTree(stmt.body, nestingLevel)
        }
        is PrintStatement -> {
            emit("print ", nestingLevel)
            printExpression(stmt.expr)
            emitln(';')
        }
        is PrintlnStatement -> {
            emit("println ", nestingLevel)
            printExpression(stmt.expr)
            emitln(';')
        }
        is ReturnStatement -> {
            emit("return ", nestingLevel)
            stmt.returnValue?.let { printExpression(it) }
            emitln(';')
        }
        is AssignmentStatement -> when (stmt.type) {
            is VariableAssignment -> {
                val (name, value) = stmt.type
                printTree(name, nestingLevel)
                emit('=')
                printExpression(value)
                emitln(';')
            }
            is ArrayAssignment -> {
                val (name, index, value) = stmt.type
                printTree(name, nestingLevel)
                emit('[')
                printExpression(index)
                emit(']')
                emit('=')
                printExpression(value)
                emitln(';')
            }
        }
        is ExpressionStatement -> {
            printExpression(stmt.expr, nestingLevel)
            emitln(';')
        }
    }

    private fun printExpression(expr: Expression, nestingLevel: Int = 0): Unit = when (expr) {
        is EqualityExpression -> {
            printExpression(expr.lhs, nestingLevel)
            emit("==")
            printExpression(expr.rhs)
        }
        is LessThanExpression -> {
            printExpression(expr.lhs, nestingLevel)
            emit('<')
            printExpression(expr.rhs)
        }
        is AddExpression -> {
            printExpression(expr.lhs, nestingLevel)
            emit('+')
            printExpression(expr.rhs)
        }
        is SubtractExpression -> {
            printExpression(expr.lhs, nestingLevel)
            emit('-')
            printExpression(expr.rhs)
        }
        is MultExpression -> {
            printExpression(expr.lhs, nestingLevel)
            emit('*')
            printExpression(expr.rhs)
        }
        is ArrayReference -> {
            printTree(expr.name, nestingLevel)
            emit('[')
            printExpression(expr.index)
            emit(']')
        }
        is FunctionCall -> {
            printTree(expr.name, nestingLevel)
            emit('(')
            printTree(expr.args)
            emit(')')
        }
        is ParenExpression -> {
            emit('(', nestingLevel)
            printExpression(expr.inner)
            emit(')')
        }
        is IdentifierValue -> printTree(expr.id, nestingLevel)
        is StringLiteral -> {
            emit('"')
            emit(escapeJava(expr.value))
            emit('"')
        }
        is CharacterLiteral -> {
            val escaped = escapeJava(expr.value.toString())
            emit('\'')
            emit(escaped.removeSurrounding("\""))
            emit('\'')
        }
        is Literal<*> -> emit(expr.value, nestingLevel)
    }
}