package semantic

import ast.*
import type.*
import util.ignore

class Environment(private val parent: Environment? = null) {
    private val nameMap = mutableMapOf<Identifier, Type>()
    fun add(id: Identifier, value: Type) {
        nameMap[id] = value
    }

    fun lookup(id: Identifier): Type? {
        return nameMap[id] ?: parent?.lookup(id)
    }

    fun lookupOrFail(id: Identifier) = lookup(id) ?: throw TypeCheckException(
        "undeclared identifier ${id.name}", IdentifierValue(id)
    )
}

private val RETURN_TYPE_TOKEN = Identifier("<return>")
private val PRINT_TYPES = arrayOf(IntegerType, FloatType, CharType, StringType, BooleanType)

open class SemanticException(msg: String, val line: Int, val col: Int) : RuntimeException(msg) {
    constructor(msg: String, node: ASTNode) : this(msg, node.line, node.offset)
}

class TypeCheckException(msg: String, line: Int, col: Int) : SemanticException(msg, line, col) {
    constructor(msg: String, node: ASTNode) : this(msg, node.line, node.offset)
}

private fun isVoidType(t: Type): Boolean = t == VoidType || (t is ArrayType && isVoidType(t.elementType))

class TypeChecker : ASTConsumer<Unit> {
    private val rootEnv = Environment()

    override fun process(node: ASTNode) = check(node, rootEnv)

    private fun check(node: ASTNode, env: Environment): Unit = when (node) {
        is Program -> {
            node.forEach { addFunctionType(it.declaration, env) }
            val mainType =
                env.lookup(Identifier("main")) as? FunctionType ?: throw SemanticException(
                    "no main function found",
                    node
                )
            val mainFunc = node.find { it.declaration.name.name == "main" }
            if (mainType.returnType != VoidType) {
                throw SemanticException("main function must return void", mainFunc!!)
            } else if (mainType.parameterTypes.isNotEmpty()) {
                throw SemanticException(
                    "main function must not take any parameters",
                    mainFunc!!.declaration.parameters[0]
                )
            }
            node.forEach { check(it, env) }
        }
        is ast.Function -> {
            val subEnv = Environment()
            check(node.declaration, subEnv)
            check(node.body, subEnv)
        }
        is FunctionDeclaration -> {
            env.add(RETURN_TYPE_TOKEN, node.returnType.type)
            check(node.parameters, env)
        }
        is ASTNodeList<*> -> node.forEach { check(it, env) }
        is Block -> node.forEach { check(it, env) }
        is VariableDeclaration -> {
            val varName = node.name
            val varType = node.type.type
            when {
            // Clause 2.2.2: No two local variables declared in a function may have the same name.
                env.lookup(varName) != null -> throw TypeCheckException(
                    "variable ${varName.name} is already declared",
                    node.name
                )
            // Clause 2.2.4: No local variable may have a "type" of void
                isVoidType(varType) -> throw TypeCheckException(
                    "variable ${varName.name} must not be $varType",
                    node.name
                )
            }
            env.add(varName, node.type.type)
        }
        is FormalParameter -> {
            val paramName = node.name
            val paramType = node.type.type
            when {
            // Clause 2.2.1: No two parameters of a function may have the same name
                env.lookup(paramName) != null -> throw TypeCheckException(
                    "redefinition of parameter ${paramName.name}",
                    node.name
                )
            // Clause 2.2.3: No parameter may have a "type" of void
                isVoidType(paramType) -> throw TypeCheckException(
                    "parameter ${paramName.name} must not be $paramType",
                    node
                )
            }
            env.add(paramName, paramType)
        }
        is FunctionBody -> {
            check(node.declarations, env)
            check(node.statements, env)
        }
        is IfStatement -> {
            val condType = resolveType(node.cond, env)
            if (condType != BooleanType) {
                throw TypeCheckException("if statement condition must be of type boolean, $condType given", node.cond)
            }
            check(node.thenClause, env)
            node.elseClause?.apply { check(this, env) }.ignore()
        }
        is WhileStatement -> {
            val condType = resolveType(node.cond, env)
            if (condType != BooleanType) {
                throw TypeCheckException(
                    "while statement condition must be of type boolean, $condType given",
                    node.cond
                )
            }
            check(node.body, env)
        }
    // TODO de-dupe these
        is PrintStatement -> run {
            val t = resolveType(node.expr, env)
            if (t !in PRINT_TYPES) {
                throw TypeCheckException("print does not support value of type $t", node.expr)
            }
        }
        is PrintlnStatement -> run {
            val t = resolveType(node.expr, env)
            if (t !in PRINT_TYPES) {
                throw TypeCheckException("println does not support value of type $t", node.expr)
            }
        }
        is ReturnStatement -> run {
            val returnType = env.lookupOrFail(RETURN_TYPE_TOKEN)
            if (node.returnValue == null) {
                if (returnType != VoidType) throw TypeCheckException("missing return value", node)
            } else {
                val exprType = resolveType(node.returnValue, env)
                if (exprType != returnType) {
                    throw TypeCheckException(
                        "cannot return value of type $exprType from function of type $returnType",
                        node.returnValue
                    )
                }
            }
        }
        is AssignmentStatement -> run {
            val varType = when (node.type) {
                is VariableAssignment -> env.lookupOrFail(node.type.name)
                is ArrayAssignment -> resolveType(ArrayReference(node.type.name, node.type.index), env)
            }
            val valType = resolveType(node.type.value, env)
            if (varType != valType) {
                throw TypeCheckException(
                    "cannot assign value of type $valType to variable of type $varType",
                    node.type.value
                )
            }
        }
        is ExpressionStatement -> check(node.expr, env)
        is Expression -> resolveType(node, env).ignore()
        else -> {
            // These AST Nodes aren't applicable for semantic analysis
        }
    }

    private fun resolveType(exp: Expression, env: Environment): Type = when (exp) {
        is EqualityExpression -> checkBinaryOperator(exp, env) { lType, rType ->
            if (lType in arrayOf(IntegerType, FloatType, CharType, StringType, BooleanType) && lType == rType) {
                BooleanType
            } else throw TypeCheckException("cannot apply operator == to types $lType and $rType", exp)
        }
        is LessThanExpression -> checkBinaryOperator(exp, env) { lType, rType ->
            if (lType in arrayOf(IntegerType, FloatType, CharType, StringType, BooleanType) && lType == rType) {
                BooleanType
            } else throw TypeCheckException("cannot apply operator < to types $lType and $rType", exp)
        }
        is AddExpression -> checkBinaryOperator(exp, env) { lType, rType ->
            if (lType in arrayOf(IntegerType, FloatType, CharType, StringType) && lType == rType) {
                lType
            } else throw TypeCheckException("cannot apply operator + to types $lType and $rType", exp)
        }
        is SubtractExpression -> checkBinaryOperator(exp, env) { lType, rType ->
            if (lType in arrayOf(IntegerType, FloatType, CharType) && lType == rType) {
                lType
            } else throw TypeCheckException("cannot apply operator - to types $lType and $rType", exp)
        }
        is MultExpression -> checkBinaryOperator(exp, env) { lType, rType ->
            if (lType in arrayOf(IntegerType, FloatType) && lType == rType) {
                lType
            } else throw TypeCheckException("cannot apply operator * to types $lType and $rType", exp)
        }
        is ArrayReference -> {
            val t = env.lookupOrFail(exp.name)
            if (t !is ArrayType) throw TypeCheckException("cannot index non-array value of type $t", exp)

            val indexType = resolveType(exp.index, env)
            if (indexType != IntegerType) throw TypeCheckException(
                "cannot index array with expression of type $indexType", exp.index
            )
            t.elementType
        }
        is FunctionCall -> {
            val t = rootEnv.lookup(exp.name)
            val ft = when (t) {
                is FunctionType -> t
                else -> throw TypeCheckException("only functions may be invoked", exp)
            }
            val argTypes = exp.args.map { resolveType(it, env) }
            if (argTypes.size != ft.parameterTypes.size) {
                throw TypeCheckException(
                    "function ${exp.name.name} called with incorrect number of arguments " +
                            "(expected: ${ft.parameterTypes.joinToString()} - actual: ${argTypes.joinToString()})", exp
                )
            }
            ft.parameterTypes.zip(argTypes).forEachIndexed { i, (param, arg) ->
                if (param != arg) {
                    throw TypeCheckException("parameter has type $param, $arg given", exp.args[i])
                }
            }
            ft.returnType
        }
        is ParenExpression -> resolveType(exp.inner, env)
        is IdentifierValue -> env.lookupOrFail(exp.id)
        is StringLiteral -> StringType
        is IntegerLiteral -> IntegerType
        is FloatLiteral -> FloatType
        is CharacterLiteral -> CharType
        is BooleanLiteral -> BooleanType
    }

    private fun checkBinaryOperator(exp: BinaryExpression, env: Environment, resolver: (Type, Type) -> Type): Type {
        val lType = resolveType(exp.lhs, env)
        val rType = resolveType(exp.rhs, env)
        return resolver(lType, rType)
    }

    private fun addFunctionType(fd: FunctionDeclaration, env: Environment) {
        val returnType = fd.returnType.type
        val parameterTypes = fd.parameters.map { it.type.type }
        val foundType = env.lookup(fd.name)
        if (foundType != null) throw TypeCheckException("function with name '${fd.name.name}' already exists", fd.name)
        env.add(fd.name, FunctionType(returnType, parameterTypes))
    }
}