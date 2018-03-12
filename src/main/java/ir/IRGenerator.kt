package ir

import ast.*
import semantic.Environment
import semantic.TypeCheckException
import type.*
import util.ignore
import java.io.PrintStream

data class Temporary(val id: Int, val type: Type, val name: String? = null) {
    override fun toString() = "T$id"
}

private val NullTemporary = Temporary(-1, VoidType)

class LabelFactory {
    private var currentId = 0
    fun createLabel() = IRLabel(currentId++)
}

class TempAllocator {
    private var currentId = 0
    internal val params = mutableListOf<Temporary>()
    internal val locals = mutableListOf<Temporary>()
    internal val temps = mutableListOf<Temporary>()

    fun allocateParam(type: Type, name: String) = Temporary(currentId++, type, name).also(params::plusAssign)
    fun allocateLocal(type: Type, name: String) = Temporary(currentId++, type, name).also(locals::plusAssign)
    fun allocateTemp(type: Type) = Temporary(currentId++, type).also(temps::plusAssign)

    operator fun get(name: String) = params.find { it.name == name } ?: locals.find { it.name == name }
}

data class IRProgram(val functions: MutableList<IRFunction> = mutableListOf()) : MutableList<IRFunction> by functions
data class IRFunctionTemporaries(val params: List<Temporary>, val locals: List<Temporary>, val temps: List<Temporary>) :
    Iterable<Temporary> by (params + locals + temps)

data class IRFunction(
    val instructions: MutableList<IRInstruction> = mutableListOf()
) {
    lateinit var temporaries: IRFunctionTemporaries
    lateinit var name: String
    lateinit var signature: FunctionType
}

sealed class IRInstruction
data class IRLoadImmediate<out T>(val tmp: Temporary, val value: T) : IRInstruction()
data class IRInitArray(val tmp: Temporary) : IRInstruction()
data class IRFunctionCall(val name: String, val args: List<Temporary>, val retVal: Temporary? = null) : IRInstruction()
data class IRArrayReference(val arr: Temporary, val index: Temporary, val res: Temporary) : IRInstruction()
data class IRBinaryOp(val operator: IRBinOp, val lhs: Temporary, val rhs: Temporary, val res: Temporary) :
    IRInstruction()

data class IRUnaryOp(val operator: IRUnOp, val operand: Temporary, val res: Temporary) : IRInstruction()
data class IRAssignment(val dest: Temporary, val src: Temporary, val index: Temporary? = null) : IRInstruction()
data class IRReturn(val value: Temporary? = null) : IRInstruction()
data class IRLabel(val id: Int) : IRInstruction()
data class IRJump(val label: IRLabel) : IRInstruction()
data class IRConditionalJump(val cond: Temporary, val label: IRLabel) : IRInstruction()
data class IRPrint(val type: Type, val value: Temporary) : IRInstruction()
data class IRPrintln(val type: Type, val value: Temporary) : IRInstruction()

enum class IRUnOp(val symbol: String) {
    NOT("!"),
    NEGATE("-")
}

enum class IRBinOp(val symbol: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    REMAINDER("rem"),

    LESSTHAN("<"),
    LESSTHANEQ("<="),
    EQUALS("=="),
    NOTEQUALS("!="),
    GREATERTHANEQ(">="),
    GREATERTHAN(">")
}

private fun BinaryExpression.toIROperator(): IRBinOp = when (this) {
    is EqualityExpression -> IRBinOp.EQUALS
    is LessThanExpression -> IRBinOp.LESSTHAN
    is AddExpression -> IRBinOp.ADD
    is SubtractExpression -> IRBinOp.SUBTRACT
    is MultExpression -> IRBinOp.MULTIPLY
}

class IRGenerator : ASTConsumer<IRProgram> {
    private val rootEnv = Environment()
    private val program = IRProgram()

    override fun process(node: ASTNode): IRProgram {
        generateIR(node)
        return program
    }

    private fun generateIR(node: ASTNode): Unit = when (node) {
        is Program -> {
            node.forEach { addFunctionType(it.declaration, rootEnv) }
            node.forEach(this::generateIR)
        }
        is ast.Function -> run {
            val func = IRFunction()
            val temps = TempAllocator()
            val labelFactory = LabelFactory()
            generateIR(node.declaration, func, temps, labelFactory)
            generateIR(node.body, func, temps, labelFactory)
            if (func.signature.returnType == VoidType && func.instructions.last() !is IRReturn) {
                func.instructions.add(IRReturn())
            }
            func.temporaries = IRFunctionTemporaries(temps.params, temps.locals, temps.temps)
            program += func
        }
        else -> throw IllegalStateException("Can not emit instruction outside of function")
    }

    private fun generateIR(
        node: ASTNode,
        func: IRFunction,
        temps: TempAllocator,
        labelFactory: LabelFactory
    ): Unit = when (node) {
        is FunctionDeclaration -> {
            func.name = node.name.name
            func.signature = rootEnv.lookupOrFail(node.name) as FunctionType
            generateIR(node.parameters, func, temps, labelFactory)
        }
        is FormalParameterList -> node.forEach { generateIR(it, func, temps, labelFactory) }
        is FormalParameter -> temps.allocateParam(node.type.type, node.name.name).ignore()
        is FunctionBody -> {
            generateIR(node.declarations, func, temps, labelFactory)
            generateIR(node.statements, func, temps, labelFactory)
        }
        is VariableDeclarationList -> node.forEach { generateIR(it, func, temps, labelFactory) }
        is VariableDeclaration -> temps.allocateLocal(node.type.type, node.name.name).run {
            if (node.type.type is ArrayType) {
                func.instructions += IRInitArray(this)
            }
        }
        is Block -> node.forEach { generateIR(it, func, temps, labelFactory) }
        is StatementList -> node.forEach { generateIR(it, func, temps, labelFactory) }
        is IfStatement -> {
            val cond = generateExpressionIR(node.cond, func, temps)
            val cmp = temps.allocateTemp(BooleanType)
            func.instructions += IRAssignment(cmp, cond)
            func.instructions += IRUnaryOp(IRUnOp.NOT, cmp, cmp)

            val elseLabel = labelFactory.createLabel()
            val endLabel = labelFactory.createLabel()
            func.instructions += IRConditionalJump(cmp, elseLabel)
            generateIR(node.thenClause, func, temps, labelFactory)
            func.instructions += IRJump(endLabel)
            func.instructions += elseLabel
            node.elseClause?.also { generateIR(it, func, temps, labelFactory) }
            func.instructions += endLabel
        }
        is WhileStatement -> {
            val startLabel = labelFactory.createLabel()
            func.instructions += startLabel

            val cond = generateExpressionIR(node.cond, func, temps)
            val cmp = temps.allocateTemp(BooleanType)
            func.instructions += IRAssignment(cmp, cond)
            func.instructions += IRUnaryOp(IRUnOp.NOT, cmp, cmp)

            val endLabel = labelFactory.createLabel()
            func.instructions += IRConditionalJump(cmp, endLabel)
            generateIR(node.body, func, temps, labelFactory)
            func.instructions += IRJump(startLabel)

            func.instructions += endLabel
        }
        is PrintStatement -> generateExpressionIR(node.expr, func, temps).run {
            func.instructions += IRPrint(type, this)
        }
        is PrintlnStatement -> generateExpressionIR(node.expr, func, temps).run {
            func.instructions += IRPrintln(type, this)
        }
        is ReturnStatement -> func.instructions += IRReturn(node.returnValue?.let {
            generateExpressionIR(
                it,
                func,
                temps
            )
        })
        is AssignmentStatement -> {
            val index = (node.type as? ArrayAssignment)?.let { generateExpressionIR(it.index, func, temps) }
            func.instructions += IRAssignment(
                temps[node.type.name.name]!!,
                generateExpressionIR(node.type.value, func, temps),
                index
            )
        }
        is ExpressionStatement -> generateExpressionIR(node.expr, func, temps).ignore()
        else -> {
        }
    }

    private fun generateExpressionIR(exp: Expression, func: IRFunction, temps: TempAllocator): Temporary = when (exp) {
        is BinaryExpression -> {
            val lhs = generateExpressionIR(exp.lhs, func, temps)
            val rhs = generateExpressionIR(exp.rhs, func, temps)
            val resType = if (exp is LessThanExpression || exp is EqualityExpression) BooleanType else lhs.type
            temps.allocateTemp(resType).also {
                func.instructions.add(IRBinaryOp(exp.toIROperator(), lhs, rhs, it))
            }
        }
        is ArrayReference -> {
            // FIXME we can't index the result of a function call?!?!
            val arr = temps[exp.name.name] ?: generateExpressionIR(IdentifierValue(exp.name), func, temps)
            temps.allocateTemp((arr.type as ArrayType).elementType).also {
                func.instructions += IRArrayReference(arr, generateExpressionIR(exp.index, func, temps), it)
            }
        }
        is FunctionCall -> {
            val args = exp.args.map { generateExpressionIR(it, func, temps) }
            val returnType = (rootEnv.lookupOrFail(exp.name) as FunctionType).returnType
            if (returnType == VoidType) {
                func.instructions += IRFunctionCall(exp.name.name, args)
                NullTemporary
            } else {
                val retVal = temps.allocateTemp(returnType)
                func.instructions += IRFunctionCall(exp.name.name, args, retVal)
                retVal
            }
        }
        is ParenExpression -> generateExpressionIR(exp.inner, func, temps)
        is IdentifierValue -> temps[exp.id.name]!!
        is StringLiteral -> temps.allocateTemp(StringType).also {
            func.instructions.add(IRLoadImmediate(it, exp.value))
        }
        is IntegerLiteral -> temps.allocateTemp(IntegerType).also {
            func.instructions.add(IRLoadImmediate(it, exp.value))
        }
        is FloatLiteral -> temps.allocateTemp(FloatType).also {
            func.instructions.add(IRLoadImmediate(it, exp.value))
        }
        is CharacterLiteral -> temps.allocateTemp(CharType).also {
            func.instructions.add(IRLoadImmediate(it, exp.value))
        }
        is BooleanLiteral -> temps.allocateTemp(BooleanType).also {
            func.instructions.add(IRLoadImmediate(it, exp.value))
        }
    }

    private fun addFunctionType(fd: FunctionDeclaration, env: Environment) {
        val returnType = fd.returnType.type
        val parameterTypes = fd.parameters.map { it.type.type }
        val foundType = env.lookup(fd.name)
        if (foundType != null) throw TypeCheckException("function with name '${fd.name.name}' already exists", fd.name)
        env.add(fd.name, FunctionType(returnType, parameterTypes))
    }
}
