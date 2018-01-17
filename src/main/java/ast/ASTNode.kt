package ast

sealed class Type(val name: String) {
    override fun toString() = name
    override fun equals(other: Any?) = this.javaClass == other?.javaClass
    override fun hashCode() = name.hashCode()
}

data class ArrayType(val elementType: Type, val size: Int) : Type(elementType.name) {
    override fun toString() = "$elementType[$size]"
}

class CharType : Type("char")
class IntegerType : Type("int")
class BooleanType : Type("boolean")
class FloatType : Type("float")
class StringType : Type("string")
class VoidType : Type("void")

class TypeNode(val type: Type)

class Identifier
class FormalParameter(val type: TypeNode, val name: Identifier)
class FormalParameterList {
    val paramList = mutableListOf<FormalParameter>()

    fun add(param: FormalParameter) = paramList.add(param)
}

class Declaration(type: TypeNode, name: Identifier)

sealed class Expression
class EqualityExpression(lhs: Expression, rhs: Expression) : Expression()
class LessThanExpression(lhs: Expression, rhs: Expression) : Expression()
class AddExpression(lhs: Expression, rhs: Expression) : Expression()
class SubtractExpression(lhs: Expression, rhs: Expression) : Expression()
class MultExpression(lhs: Expression, rhs: Expression) : Expression()
//class LessThanExpression(rhs: Expression, lhs: Expression) : Expression()
// TODO make sealed
open class Atom : Expression()

class ArrayReference : Atom()
class FunctionCall : Atom()
class ParenExpression : Atom()
class IdentifierValue : Atom()

sealed class Literal<out T>(val value: T): Atom()
class StringLiteral(value: String) : Literal<String>(value)
class IntegerLiteral(value: Int) : Literal<Int>(value)
class FloatLiteral(value: Float) : Literal<Float>(value)
class CharacterLiteral(value: Char) : Literal<Char>(value)
class BooleanLiteral(value: Boolean) : Literal<Boolean>(value)


class Block

sealed class Statement
class IfStatement(cond: Expression, thenClause: Block, elseClause: Block?) : Statement()
class WhileStatement(cond: Expression, body: Block) : Statement()
class PrintStatement(expr: Expression) : Statement()
class PrintlnStatement(expr: Expression) : Statement()
class ReturnStatement(returnValue: Expression?) : Statement()

sealed class AssignmentType
class VariableAssignment(name: Identifier, value: Expression) : AssignmentType()
class ArrayAssignment(name: Identifier, index: Expression, value: Expression) : AssignmentType()

class AssignmentStatement(type: AssignmentType) : Statement()
class ExpressionStatement(expr: Expression) : Statement()



sealed class ASTNode

class Program: ASTNode() {
    val functionList = mutableListOf<Function>()

    fun addElement(f: Function) = functionList.add(f)
}
data class Function(val declaration: FunctionDeclaration, val body: FunctionBody): ASTNode()
data class FunctionDeclaration(val returnType: TypeNode,
                               val name: Identifier,
                               val parameters: FormalParameterList): ASTNode()
data class FunctionBody(val varDecls: List<Declaration>): ASTNode()