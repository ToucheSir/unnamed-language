package ast

import type.Type

sealed class ASTNode {
    var line: Int = -1
    var offset: Int = -1
}

sealed class ASTNodeList<E : ASTNode>(private val elements: MutableList<E>) : ASTNode(), MutableList<E> by elements

data class Program(private val functions: MutableList<Function> = mutableListOf()) : ASTNodeList<Function>(functions) {
    init {
        line = 0
        offset = 0
    }
}

data class Function(val declaration: FunctionDeclaration, val body: FunctionBody) : ASTNode() {
    init {
        line = declaration.line
        offset = declaration.offset
    }
}

sealed class Declaration : ASTNode()
data class FunctionDeclaration(
    val returnType: TypeNode,
    val name: Identifier,
    val parameters: FormalParameterList = FormalParameterList()
) : Declaration() {
    init {
        line = returnType.line
        offset = returnType.offset
    }
}

data class TypeNode(val type: Type) : ASTNode()
data class Identifier(val name: String) : ASTNode()

data class FormalParameter(val type: TypeNode, val name: Identifier) : ASTNode() {
    init {
        line = type.line
        offset = type.offset
    }
}

data class FormalParameterList(private val parameters: MutableList<FormalParameter> = mutableListOf()) :
    ASTNodeList<FormalParameter>(parameters)

data class FunctionBody(
    val declarations: VariableDeclarationList = VariableDeclarationList(),
    val statements: StatementList = StatementList()
) : ASTNode()

data class VariableDeclarationList(private val declarations: MutableList<VariableDeclaration> = mutableListOf()) :
    ASTNodeList<VariableDeclaration>(declarations)

data class VariableDeclaration(val type: TypeNode, val name: Identifier) : Declaration()

data class StatementList(private val statements: MutableList<Statement> = mutableListOf()) :
    ASTNodeList<Statement>(statements)

sealed class Statement : ASTNode()

data class IfStatement(val cond: Expression, val thenClause: Block, val elseClause: Block? = null) : Statement()
data class WhileStatement(val cond: Expression, val body: Block) : Statement()
data class PrintStatement(val expr: Expression) : Statement()
data class PrintlnStatement(val expr: Expression) : Statement()
data class ReturnStatement(val returnValue: Expression?) : Statement()

data class AssignmentStatement(val type: AssignmentType) : Statement()

sealed class AssignmentType(open val name: Identifier, open val value: Expression)
data class VariableAssignment(override val name: Identifier, override val value: Expression) :
    AssignmentType(name, value)

data class ArrayAssignment(
    override val name: Identifier,
    val index: Expression,
    override val value: Expression
) : AssignmentType(name, value)

data class ExpressionStatement(val expr: Expression) : Statement()

sealed class Expression : ASTNode()

sealed class BinaryExpression(open val lhs: Expression, open val rhs: Expression) : Expression()
data class EqualityExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)
data class LessThanExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)
data class AddExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)
data class SubtractExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)
data class MultExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)

data class ArrayReference(val name: Identifier, val index: Expression) : Expression()
data class FunctionCall(val name: Identifier, val args: ExpressionList = ExpressionList()) : Expression() {
    init {
        line = name.line
        offset = name.offset
    }
}
data class ExpressionList(private val expressions: MutableList<Expression> = mutableListOf()) :
    ASTNodeList<Expression>(expressions)

data class ParenExpression(val inner: Expression) : Expression()
data class IdentifierValue(val id: Identifier) : Expression() {
    init {
        line = id.line
        offset = id.offset
    }
}

sealed class Literal<out T>(open val value: T) : Expression()
data class StringLiteral(override val value: String) : Literal<String>(value)
data class IntegerLiteral(override val value: Int) : Literal<Int>(value)
data class FloatLiteral(override val value: Float) : Literal<Float>(value)
data class CharacterLiteral(override val value: Char) : Literal<Char>(value)
data class BooleanLiteral(override val value: Boolean) : Literal<Boolean>(value)

data class Block(private val statements: MutableList<Statement> = mutableListOf()) : ASTNodeList<Statement>(statements)

