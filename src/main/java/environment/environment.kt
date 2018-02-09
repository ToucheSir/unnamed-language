package environment

import ast.*
import type.FunctionType
import type.Type
import type.VoidType

public interface Environment<K, V> {
    fun beginScope()
    fun endScope()
    fun inCurrentScope(key: K): Boolean
    fun add(key: K, value: V)
    fun lookup(key: K): V
}

class Env(val parent: Env? = null) {
    private val nameMap = mutableMapOf<String, Type>()
    fun add(key: String, value: Type) = nameMap.put(key, value)
    fun lookup(key: String): Type? {
        return nameMap[key] ?: parent?.lookup(key)
    }
}

class TypeCheckException(val msg: String) : RuntimeException("Type error: $msg")

class TypeChecker : ASTConsumer<Unit> {
    private val rootEnv = Env()

    override fun process(node: ASTNode) {
        check(node, rootEnv)
    }

    private fun check(node: ASTNode, env: Env): Unit = when (node) {
        is Program -> {
            node.forEach { addFunctionType(it.declaration, env) }
            node.forEach { check(it, env) }
        }
        is ast.Function -> {
            val subEnv = Env()
            check(node.declaration, subEnv)
            check(node.body, subEnv)
        }
        is FunctionDeclaration -> check(node.parameters, env)
        is FormalParameterList -> node.forEach {
            val paramName = it.name.name
            val paramType = it.type.type
            val existing = env.lookup(paramName)
            when {
                // Clause 2.2.1: No two parameters of a function may have the same name
                existing != null -> throw TypeCheckException("redefinition of parameter $paramName")
                // Clause 2.2.1: No two parameters of a function may have the same name
                paramType == VoidType -> throw TypeCheckException("function parameters must not have type 'void'")
            }
            env.add(paramName, paramType)
        }
        is VariableDeclarationList -> TODO()
        is StatementList -> TODO()
        is ExpressionList -> TODO()
        is Block -> TODO()
        is VariableDeclaration -> TODO()
        is TypeNode -> TODO()
        is Identifier -> TODO()
        is FormalParameter -> TODO()
        is FunctionBody -> TODO()
        is IfStatement -> TODO()
        is WhileStatement -> TODO()
        is PrintStatement -> TODO()
        is PrintlnStatement -> TODO()
        is ReturnStatement -> TODO()
        is AssignmentStatement -> TODO()
        is ExpressionStatement -> TODO()
        is EqualityExpression -> TODO()
        is LessThanExpression -> TODO()
        is AddExpression -> TODO()
        is SubtractExpression -> TODO()
        is MultExpression -> TODO()
        is ArrayReference -> TODO()
        is FunctionCall -> TODO()
        is ParenExpression -> TODO()
        is IdentifierValue -> TODO()
        is StringLiteral -> TODO()
        is IntegerLiteral -> TODO()
        is FloatLiteral -> TODO()
        is CharacterLiteral -> TODO()
        is BooleanLiteral -> TODO()
    }

    private fun addFunctionType(fd: FunctionDeclaration, env: Env) {
        val returnType = fd.returnType.type
        val parameterTypes = fd.parameters.elements.map { it.type.type }
        env.add(fd.name.name, FunctionType(returnType, parameterTypes))
    }
}