package ir

import type.*
import java.io.PrintStream

class IRPrinter(val out: PrintStream) {
    fun print(program: IRProgram) {
        program.functions.forEach(this::print)
    }

    fun print(func: IRFunction) {
        val sig = func.signature
        out.print("FUNC ${func.name} (")
        sig.parameterTypes.forEach(this::print)
        out.print(')')
        print(sig.returnType)
        out.println("\n{")
        func.temporaries.forEach(this::print)
        func.instructions.forEach(this::print)
        out.println('}')
    }

    fun print(temporary: Temporary) {
        out.print("  TEMP ${temporary.id}:")
        print(temporary.type)
        temporary.name?.run { out.print(" [L(\"$this\")]") }
        out.println(';')
    }

    fun print(instruction: IRInstruction) {
        out.print("  ")
        when (instruction) {
            is IRLoadImmediate<*> -> out.print("${instruction.tmp} := ${instruction.value}")
            is IRInitArray -> {
                val (elemType, size) = instruction.tmp.type as ArrayType
                out.print("${instruction.tmp} := NEWARRAY (")
                print(elemType)
                out.print(") $size")
            }
            is IRFunctionCall -> {
                instruction.retVal?.run { out.print("$this := ") }
                out.print("CALL ${instruction.name}(${instruction.args.joinToString(" ")})")
            }
            is IRArrayReference -> out.print("${instruction.res} := ${instruction.arr}[${instruction.index}]")
            is IRBinaryOp -> {
                out.print("${instruction.res} := ${instruction.lhs} ")
                print(instruction.res.type)
                out.print("${instruction.operator.symbol} ${instruction.rhs}")
            }
            is IRUnaryOp -> {
                out.print("${instruction.res} := ")
                print(instruction.res.type)
                out.print("${instruction.operator.symbol} ${instruction.operand}")
            }
            is IRAssignment -> {
                out.print("${instruction.dest} := ${instruction.src}")
                instruction.index?.run { out.print("[$this]") }
            }
            is IRReturn -> {
                out.print("RETURN")
                instruction.value?.run { out.print(" $this") }
            }
            is IRLabel -> out.print("L${instruction.id}:")
            is IRJump -> out.print("GOTO L${instruction.label.id}")
            is IRConditionalJump -> out.print("IF ${instruction.cond} GOTO ${instruction.label.id}")
            is IRPrint -> {
                out.print("PRINT")
                print(instruction.type)
                out.print(" ${instruction.value}")
            }
            is IRPrintln -> {
                out.print("PRINTLN")
                print(instruction.type)
                out.print(" ${instruction.value}")
            }
        }
        out.println(';')
    }

    fun print(type: Type): Unit = when (type) {
        is ArrayType -> {
            out.print('A')
            print(type.elementType)
        }
        CharType -> out.print('C')
        IntegerType -> out.print('I')
        BooleanType -> out.print('Z')
        FloatType -> out.print('F')
        StringType -> out.print('U')
        VoidType -> out.print('V')
        is FunctionType -> throw NotImplementedError()
    }
}
