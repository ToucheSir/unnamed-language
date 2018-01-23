package ast.dsl

import ast.*
import ast.Function

fun program(init: Program.() -> Unit): Program {
    val node = Program()
    node.init()
    return node
}

fun Program.function(returnType: Type, name: String, init: Function.() -> Unit): Identifier {
    val f = Function(FunctionDeclaration(returnType.toAST(), name.toId()), FunctionBody())
    f.init()
    add(f)
    return name.toId()
}

fun Function.param(type: Type, name: String): Identifier {
    declaration.parameters.add(FormalParameter(type.toAST(), name.toId()))
    return name.toId()
}

fun Function.varDecl(type: Type, name: String): Identifier {
    body.declarations.add(VariableDeclaration(type.toAST(), name.toId()))
    return name.toId()
}

fun Function.statement(s: Statement) {
    body.statements.add(s)
}

infix fun Expression.eq(rhs: Expression) = EqualityExpression(this, rhs)
infix fun Expression.lt(rhs: Expression) = LessThanExpression(this, rhs)
operator fun Expression.plus(rhs: Expression) = AddExpression(this, rhs)
operator fun Expression.minus(rhs: Expression) = SubtractExpression(this, rhs)
operator fun Expression.times(rhs: Expression) = MultExpression(this, rhs)

fun Identifier.assign(value: Expression) = AssignmentStatement(VariableAssignment(this, value))
fun Identifier.assignIndex(index: Expression, value: Expression) = AssignmentStatement(ArrayAssignment(this, index, value))

fun Type.toAST() = TypeNode(this)
operator fun Type.get(num: Int) = ArrayType(this, num)
fun String.toId() = Identifier(this)

fun String.toLit() = StringLiteral(this)
fun Int.toLit() = IntegerLiteral(this)
fun Float.toLit() = FloatLiteral(this)
fun Char.toLit() = CharacterLiteral(this)
fun Boolean.toLit() = BooleanLiteral(this)

operator fun Identifier.not() = IdentifierValue(this)
operator fun Identifier.get(index: Expression) = ArrayReference(this, index)
operator fun Identifier.invoke(vararg args: Expression) =
    FunctionCall(this, ExpressionList(args.toMutableList()))
