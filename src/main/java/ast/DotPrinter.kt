package ast

import org.apache.commons.text.StringEscapeUtils.escapeJava
import java.io.PrintStream

private fun escapeStringLiteral(s: String) = s
    .replace("\\", "\\\\")
    .replace("\"", "\\\"")

private data class TreeNode(val id: Int, val type: String, var value: String? = null)

class DotPrinter(private val out: PrintStream, private val graphName: String) : ASTConsumer<Unit> {
    private var counter = 0

    constructor(out: PrintStream) : this(out, "AST")

    override fun process(node: ASTNode) {
        out.println("digraph $graphName {")
        out.println("rankdir=LR")
        node.printTree()
        out.println("}")
    }

    private fun createNode(
        label: String,
        parentId: Int? = null,
        action: ((TreeNode) -> Unit)? = null
    ) {
        val node = allocateNode(label)
        action?.invoke(node)
        val escapedType = escapeStringLiteral(node.type)
        out.print("${node.id} [shape=\"record\",label=\"{$escapedType")
        node.value?.let { out.print("|${escapeStringLiteral(it)}") }
        out.println("}\"]")
        parentId?.let { out.println("$parentId -> ${node.id}") }
    }

    private fun allocateNode(type: String) = TreeNode(counter++, type)

    private fun ASTNode.printTree(parentId: Int? = null) {
        createNode(javaClass.simpleName, parentId) { node ->
            val id = node.id
            run {
                when (this) {
                    is Program -> this.forEach { it.printTree(id) }
                    is Function -> {
                        declaration.printTree(id)
                        body.printTree(id)
                    }
                    is FunctionDeclaration -> {
                        returnType.printTree(id)
                        name.printTree(id)
                        parameters.printTree(id)
                    }
                    is VariableDeclaration -> {
                        type.printTree(id)
                        name.printTree(id)
                    }
                    is TypeNode -> node.value = type.name
                    is Identifier -> node.value = name
                    is FormalParameterList -> this.forEach { it.printTree(id) }
                    is FormalParameter -> {
                        type.printTree(id)
                        name.printTree(id)
                    }
                    is FunctionBody -> {
                        declarations.printTree(id)
                        statements.printTree(id)
                    }
                    is VariableDeclarationList -> this.forEach { it.printTree(id) }
                    is StatementList -> this.forEach { it.printTree(id) }
                    is Statement -> printChildren(id)
                    is Block -> this.forEach { it.printTree(id) }
                    is Expression -> printChildren(node)
                    is ExpressionList -> this.forEach { it.printTree(id) }
                }
            }
        }
    }

    private fun Statement.printChildren(id: Int) = when (this) {
        is IfStatement -> {
            cond.printTree(id)
            thenClause.printTree(id)
            elseClause?.printTree(id)
        }
        is WhileStatement -> {
            cond.printTree(id)
            body.printTree(id)
        }
        is PrintStatement -> expr.printTree(id)
        is PrintlnStatement -> expr.printTree(id)
        is ReturnStatement -> {
            returnValue?.printTree(id)
        }
        is AssignmentStatement -> when (type) {
            is VariableAssignment -> {
                type.name.printTree(id)
                type.value.printTree(id)
            }
            is ArrayAssignment -> {
                type.name.printTree(id)
                type.index.printTree(id)
                type.value.printTree(id)
            }
        }
        is ExpressionStatement -> {
            expr.printTree(id)
        }
    }

    private fun Expression.printChildren(node: TreeNode) = when (this) {
        is BinaryExpression -> {
            lhs.printTree(node.id)
            rhs.printTree(node.id)
        }
        is ArrayReference -> {
            name.printTree(node.id)
            index.printTree(node.id)
        }
        is FunctionCall -> {
            name.printTree(node.id)
            args.printTree(node.id)
        }
        is ParenExpression -> inner.printTree(node.id)
        is IdentifierValue -> this.id.printTree(node.id)
        is StringLiteral -> node.value = "\"${escapeJava(value)}\""
        is CharacterLiteral -> {
            val escaped = escapeJava(value.toString())
            node.value = "'${escaped.removeSurrounding("\"")}'"
        }
        is Literal<*> -> node.value = value.toString()
    }
}
