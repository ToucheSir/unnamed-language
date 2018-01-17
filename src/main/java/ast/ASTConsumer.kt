package ast

interface ASTConsumer<out T> {
    fun process(node: ASTNode): T
}