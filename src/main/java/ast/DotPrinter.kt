package ast

import java.io.PrintStream

class DotPrinter(private val out: PrintStream, private val graphName: String) : ASTConsumer<Unit> {
    private var counter = 0

    constructor(out: PrintStream) : this(out, "AST")

    override fun process(node: ASTNode) {
        out.println("digraph $graphName {")
        out.println("rankdir=LR")
        node.printTree()
        out.println("}")
    }

    private fun createNode(label: String, parentId: Int? = null, action: ((Int) -> Unit)? = null) {
        val id = allocateNode(label)
        parentId?.let { out.println("$parentId -> $id") }
        return action?.invoke(id) ?: Unit
    }

    private fun allocateNode(label: String): Int {
        val id = counter++
        val escapedLabel = label
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
        out.println("$id [shape=box,label=\"$escapedLabel\"]")
        return id
    }

    private fun ASTNode.printTree(parentId: Int? = null) {
        createNode(javaClass.simpleName, parentId) { id ->
            run {
                when (this) {
                    is Program -> forEach { it.printTree(id) }
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
                    is TypeNode -> {
                        createNode(type.name, id)
                    }
                    is Identifier -> {
                        createNode(name, id)
                    }
                    is FormalParameterList -> forEach { it.printTree(id) }
                    is FormalParameter -> {
                        type.printTree(id)
                        name.printTree(id)
                    }
                    is FunctionBody -> {
                        declarations.printTree(id)
                        statements.printTree(id)
                    }
                    is VariableDeclarationList -> forEach { it.printTree(id) }
                    is StatementList -> forEach { it.printTree(id) }
                    is Statement -> printChildren(id)
                    is Block -> forEach { it.printTree(id) }
                    is Expression -> printChildren(id)
                    is ExpressionList -> forEach { it.printTree(id) }
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

    private fun Expression.printChildren(id: Int) = when (this) {
        is BinaryExpression -> {
            lhs.printTree(id)
            rhs.printTree(id)
        }
        is ArrayReference -> {
            name.printTree(id)
            index.printTree(id)
        }
        is FunctionCall -> {
            name.printTree(id)
            args.printTree(id)
        }
        is ParenExpression -> {
            inner.printTree(id)
        }
        is IdentifierValue -> this.id.printTree(id)
        is Literal<*> -> createNode(value as? String ?: value.toString(), id)
    }
}
