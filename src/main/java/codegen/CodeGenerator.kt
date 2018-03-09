package codegen

import ir.IRProgram

interface CodeGenerator {
    fun generate(program: IRProgram)
}