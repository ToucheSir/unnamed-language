package ast

import java.io.PrintStream

class PrettyPrinter(val out: PrintStream): ASTConsumer<Unit> {
    override fun process(node: ASTNode) = printTree(node)
    private fun printTree(node: ASTNode, nestingLevel: Int = 0): Unit = when (node) {
        is Program -> node.functionList.forEach { printTree(it) }
        is Function -> {
            printTree(node.body)
            printTree(node.declaration)
        }
        is FunctionBody -> { }
        is FunctionDeclaration -> {}
    }
}