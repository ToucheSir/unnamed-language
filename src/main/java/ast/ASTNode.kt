package ast

import type.Type

sealed class ASTNode
sealed class ASTNodeList<E : ASTNode>(val elements: MutableList<E>) : ASTNode() {
    val size get() = elements.size
    fun add(node: E) = elements.add(node)
    inline fun forEach(action: (E) -> Unit) = elements.forEach(action)
    inline fun forEachIndexed(action: (Int, E) -> Unit) = elements.forEachIndexed(action)
}

data class Program(private val functions: MutableList<Function> = mutableListOf()) : ASTNodeList<Function>(functions)

data class Function(val declaration: FunctionDeclaration, val body: FunctionBody) : ASTNode()

sealed class Declaration : ASTNode()
data class FunctionDeclaration(val returnType: TypeNode,
                               val name: Identifier,
                               val parameters: FormalParameterList = FormalParameterList()) : Declaration()

data class TypeNode(val type: Type) : ASTNode()
data class Identifier(val name: String) : ASTNode()

data class FormalParameter(val type: TypeNode, val name: Identifier) : ASTNode()
data class FormalParameterList(private val parameters: MutableList<FormalParameter> = mutableListOf()) : ASTNodeList<FormalParameter>(parameters)

data class FunctionBody(val declarations: VariableDeclarationList = VariableDeclarationList(),
                        val statements: StatementList = StatementList()) : ASTNode()

data class VariableDeclarationList(private val declarations: MutableList<VariableDeclaration> = mutableListOf()) : ASTNodeList<VariableDeclaration>(declarations)
data class VariableDeclaration(val type: TypeNode, val name: Identifier) : Declaration()

data class StatementList(private val statements: MutableList<Statement> = mutableListOf()) : ASTNodeList<Statement>(statements)
sealed class Statement : ASTNode()

data class IfStatement(val cond: Expression, val thenClause: Block, val elseClause: Block? = null) : Statement()
data class WhileStatement(val cond: Expression, val body: Block) : Statement()
data class PrintStatement(val expr: Expression) : Statement()
data class PrintlnStatement(val expr: Expression) : Statement()
data class ReturnStatement(val returnValue: Expression?) : Statement()

data class AssignmentStatement(val type: AssignmentType) : Statement()

sealed class AssignmentType
data class VariableAssignment(val name: Identifier, val value: Expression) : AssignmentType()
data class ArrayAssignment(val name: Identifier,
                           val index: Expression,
                           val value: Expression) : AssignmentType()

data class ExpressionStatement(val expr: Expression) : Statement()

sealed class Expression : ASTNode()

sealed class BinaryExpression(open val lhs: Expression, open val rhs: Expression) : Expression()
data class EqualityExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)
data class LessThanExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)
data class AddExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)
data class SubtractExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)
data class MultExpression(override val lhs: Expression, override val rhs: Expression) : BinaryExpression(lhs, rhs)

data class ArrayReference(val name: Identifier, val index: Expression) : Expression()
data class FunctionCall(val name: Identifier, val args: ExpressionList = ExpressionList()) : Expression()
data class ExpressionList(private val expressions: MutableList<Expression> = mutableListOf()) : ASTNodeList<Expression>(expressions)
data class ParenExpression(val inner: Expression) : Expression()
data class IdentifierValue(val id: Identifier) : Expression()

sealed class Literal<out T>(open val value: T) : Expression()
data class StringLiteral(override val value: String) : Literal<String>(value)
data class IntegerLiteral(override val value: Int) : Literal<Int>(value)
data class FloatLiteral(override val value: Float) : Literal<Float>(value)
data class CharacterLiteral(override val value: Char) : Literal<Char>(value)
data class BooleanLiteral(override val value: Boolean) : Literal<Boolean>(value)

data class Block(private val statements: MutableList<Statement> = mutableListOf()) : ASTNodeList<Statement>(statements)

