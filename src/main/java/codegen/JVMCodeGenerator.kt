package codegen

import ir.*
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.util.CheckClassAdapter
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import type.*
import java.io.OutputStream
import java.io.PrintWriter

class JVMCodeGenerator(private val programName: String, val out: OutputStream) : CodeGenerator {
    private val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)

    override fun generate(program: IRProgram) {
        cw.visit(
            V1_6,
            ACC_PUBLIC,
            programName,
            null,
            getInternalName(Object::class.java),
            emptyArray()
        )
        val constructor = cw.visitMethod(
            ACC_PRIVATE, "<init>", "()V", null, null
        )
        constructor.visitCode()
        constructor.visitVarInsn(ALOAD, 0)
        constructor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        constructor.visitInsn(RETURN)
        constructor.visitMaxs(1, 1)
        constructor.visitEnd()

        val main = cw.visitMethod(
            ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null
        )
        main.visitCode()
        main.visitMethodInsn(INVOKESTATIC, programName, "main", "()V", false)
        main.visitInsn(RETURN)
        main.visitMaxs(1, 1)
        main.visitEnd()

        program.functions.forEach(this::generateFunction)
        cw.visitEnd()
        val bytes = cw.toByteArray()
//        CheckClassAdapter.verify(ClassReader(bytes), true, PrintWriter(System.err))
        out.write(bytes)
        out.close()
    }

    private fun generateFunction(func: IRFunction) {
        val (rType, pTypes) = func.signature
        val mv = cw.visitMethod(
            ACC_PRIVATE or ACC_STATIC,
            func.name,
            "(${pTypes.joinToString("") { it.toJVMStr() }})${rType.toJVMStr()}",
            null,
            null
        )
        func.temporaries.params.forEach {
            mv.visitParameter(it.name, 0)
        }
        mv.visitCode()
        val start = Label()
        val end = Label()
        mv.visitLabel(start)
        func.temporaries.locals.forEach {
            mv.visitLocalVariable(it.name, it.type.toJVMStr(), null, start, end, it.id)
        }
        func.temporaries.temps.forEach {
            mv.visitLocalVariable("T${it.id}", it.type.toJVMStr(), null, start, end, it.id)
        }
        val labels = func.instructions.filter { it is IRLabel }.map { Label() }
        func.instructions.forEachIndexed { line, ins ->
            when (ins) {
                is IRLoadImmediate<*> -> {
                    mv.visitLdcInsn(ins.value)

                    val (loadOp, storeOp) = when (ins.tmp.type) {
                        IntegerType, CharType, BooleanType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(ALOAD, ASTORE)
                    }
                    mv.visitVarInsn(storeOp, ins.tmp.id)
                }
                is IRInitArray -> {
                    val aType = ins.tmp.type as ArrayType
                    mv.visitLdcInsn(aType.size)
                    if (aType.elementType == StringType) {
                        mv.visitTypeInsn(ANEWARRAY, "java/lang/String")
                    } else {
                        mv.visitIntInsn(NEWARRAY, aType.elementType.toJVMInt())
                    }
                    mv.visitVarInsn(ASTORE, ins.tmp.id)
                }
                is IRFunctionCall -> {
                    for (arg in ins.args) mv.visitVarInsn(
                        when (arg.type) {
                            IntegerType, BooleanType, CharType -> ILOAD
                            FloatType -> FLOAD
                            StringType, is ArrayType -> ALOAD
                            else -> throw UnsupportedOperationException()
                        }, arg.id
                    )
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        programName,
                        ins.name,
                        "(${ins.args.joinToString("") { it.type.toJVMStr() }})${(ins.retVal?.type
                                ?: VoidType).toJVMStr()}",
                        false
                    )
                    ins.retVal?.run {
                        mv.visitVarInsn(
                            when (type) {
                                IntegerType, BooleanType, CharType -> ISTORE
                                FloatType -> FSTORE
                                StringType -> ASTORE
                                else -> throw UnsupportedOperationException()
                            }, id
                        )
                    }
                }
                is IRArrayReference -> {
                    val (loadOp, storeOp) = when (ins.res.type) {
                        IntegerType, BooleanType -> Pair(IALOAD, ISTORE)
                        CharType -> Pair(CALOAD, ISTORE)
                        FloatType -> Pair(FALOAD, FSTORE)
                        StringType -> Pair(AALOAD, ASTORE)
                        else -> throw UnsupportedOperationException()
                    }
                    mv.visitVarInsn(ALOAD, ins.arr.id)
                    mv.visitVarInsn(ILOAD, ins.index.id)
                    mv.visitInsn(loadOp)
                    mv.visitVarInsn(storeOp, ins.res.id)
                }
                is IRBinaryOp -> {
                    // FIXME this is horrendous
                    val (loadOp, storeOp) = when (ins.lhs.type) {
                        IntegerType, CharType, BooleanType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        StringType -> Pair(ALOAD, ASTORE)
                        else -> throw UnsupportedOperationException()
                    }
                    val (addOp, subOp, mulOp) = when (ins.lhs.type) {
                        IntegerType, CharType, BooleanType -> Triple(IADD, ISUB, IMUL)
                        FloatType -> Triple(FADD, FSUB, FMUL)
                    // FIXME more terrifying code
                        StringType -> Triple(-1, -1, -1)
                        else -> throw UnsupportedOperationException()
                    }
                    val divOp = when (ins.lhs.type) {
                        IntegerType, CharType, BooleanType -> IDIV
                        FloatType -> FDIV
                    // FIXME continued from above
                        StringType -> -1
                        else -> throw UnsupportedOperationException()
                    }
                    when (ins.operator) {
                        IRBinOp.ADD -> {
                            // FIXME this is even more horrendous
                            if (ins.lhs.type == StringType) {
                                mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
                                mv.visitInsn(DUP)
                                mv.visitMethodInsn(
                                    INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false
                                )

                                mv.visitVarInsn(loadOp, ins.lhs.id)
                                mv.visitMethodInsn(
                                    INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "append",
                                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                                    false
                                )
                                mv.visitVarInsn(loadOp, ins.rhs.id)
                                mv.visitMethodInsn(
                                    INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "append",
                                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                                    false
                                )

                                mv.visitMethodInsn(
                                    INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "toString",
                                    "()Ljava/lang/String;",
                                    false
                                )
                                mv.visitVarInsn(storeOp, ins.res.id)
                            } else {
                                mv.visitVarInsn(loadOp, ins.lhs.id)
                                mv.visitVarInsn(loadOp, ins.rhs.id)
                                mv.visitInsn(addOp)
                                mv.visitVarInsn(storeOp, ins.res.id)
                            }
                        }
                        IRBinOp.SUBTRACT -> {
                            mv.visitVarInsn(loadOp, ins.lhs.id)
                            mv.visitVarInsn(loadOp, ins.rhs.id)
                            mv.visitInsn(subOp)
                            mv.visitVarInsn(storeOp, ins.res.id)
                        }
                        IRBinOp.MULTIPLY -> {
                            mv.visitVarInsn(loadOp, ins.lhs.id)
                            mv.visitVarInsn(loadOp, ins.rhs.id)
                            mv.visitInsn(mulOp)
                            mv.visitVarInsn(storeOp, ins.res.id)
                        }
                        IRBinOp.DIVIDE -> {
                            mv.visitVarInsn(loadOp, ins.lhs.id)
                            mv.visitVarInsn(loadOp, ins.rhs.id)
                            mv.visitInsn(divOp)
                            mv.visitVarInsn(storeOp, ins.res.id)
                        }
                        IRBinOp.REMAINDER -> TODO()
                        IRBinOp.LESSTHAN -> {
                            mv.visitVarInsn(loadOp, ins.lhs.id)
                            mv.visitVarInsn(loadOp, ins.rhs.id)

                            val thenLabel = Label()
                            val endLabel = Label()
                            if (ins.lhs.type == FloatType) {
                                mv.visitInsn(FCMPL)
                                mv.visitJumpInsn(IFLT, thenLabel)
                            } else {
                                mv.visitJumpInsn(IF_ICMPLT, thenLabel)
                            }
                            mv.visitIntInsn(BIPUSH, 0)
                            mv.visitJumpInsn(GOTO, endLabel)
                            mv.visitLabel(thenLabel)
                            mv.visitIntInsn(BIPUSH, 1)
                            mv.visitLabel(endLabel)

                            mv.visitVarInsn(ISTORE, ins.res.id)
                        }
                        IRBinOp.LESSTHANEQ -> TODO()
                        IRBinOp.EQUALS -> {
                            mv.visitVarInsn(loadOp, ins.lhs.id)
                            mv.visitVarInsn(loadOp, ins.rhs.id)

                            val thenLabel = Label()
                            val endLabel = Label()
                            if (ins.lhs.type == FloatType) {
                                mv.visitInsn(FCMPL)
                                mv.visitJumpInsn(IFEQ, thenLabel)
                            } else {
                                mv.visitJumpInsn(IF_ICMPEQ, thenLabel)
                            }
                            mv.visitIntInsn(BIPUSH, 0)
                            mv.visitJumpInsn(GOTO, endLabel)
                            mv.visitLabel(thenLabel)
                            mv.visitIntInsn(BIPUSH, 1)
                            mv.visitLabel(endLabel)

                            mv.visitVarInsn(ISTORE, ins.res.id)
                        }
                        IRBinOp.NOTEQUALS -> TODO()
                        IRBinOp.GREATERTHANEQ -> TODO()
                        IRBinOp.GREATERTHAN -> TODO()
                    }
                }
                is IRUnaryOp -> when (ins.operator) {
                    IRUnOp.NOT -> if (ins.operand.type != BooleanType) throw NotImplementedError() else {
                        val notZeroLabel = Label()
                        val endLabel = Label()
                        mv.visitVarInsn(ILOAD, ins.operand.id)

                        mv.visitJumpInsn(IFNE, notZeroLabel)
                        mv.visitIntInsn(BIPUSH, 1)
                        mv.visitJumpInsn(GOTO, endLabel)
                        mv.visitLabel(notZeroLabel)
                        mv.visitIntInsn(BIPUSH, 0)
                        mv.visitLabel(endLabel)

                        mv.visitVarInsn(ISTORE, ins.res.id)
                    }
                    IRUnOp.NEGATE -> {
                        val (loadOp, storeOp) = when (ins.operand.type) {
                            IntegerType, BooleanType, CharType -> Pair(ILOAD, ISTORE)
                            FloatType -> Pair(FLOAD, FSTORE)
                            else -> throw UnsupportedOperationException()
                        }
                        val (negOp, notOp) = when (ins.operand.type) {
                            IntegerType -> Pair(IADD, ISUB)
                            FloatType -> Pair(FADD, FSUB)
                            else -> throw UnsupportedOperationException()
                        }
                    }
                }
                is IRAssignment -> if (ins.index != null) {
                    val (loadOp, storeOp) = when (ins.src.type) {
                        IntegerType -> Pair(ILOAD, IASTORE)
                        FloatType -> Pair(FLOAD, FASTORE)
                        CharType -> Pair(ILOAD, CASTORE)
                        StringType -> Pair(ALOAD, AASTORE)
                        else -> throw UnsupportedOperationException()
                    }
                    mv.visitVarInsn(ALOAD, ins.dest.id)
                    mv.visitVarInsn(ILOAD, ins.index.id)
                    mv.visitVarInsn(loadOp, ins.src.id)
                    mv.visitInsn(storeOp)
                } else {
                    val (loadOp, storeOp) = when (ins.dest.type) {
                        IntegerType, CharType, BooleanType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        StringType, is ArrayType -> Pair(ALOAD, ASTORE)
                        else -> throw UnsupportedOperationException()
                    }
                    mv.visitVarInsn(loadOp, ins.src.id)
                    mv.visitVarInsn(storeOp, ins.dest.id)
                }
                is IRReturn -> if (ins.value != null) {
                    val (loadOp, retOp) = when (ins.value.type) {
                        CharType, IntegerType, BooleanType -> Pair(ILOAD, IRETURN)
                        FloatType -> Pair(FLOAD, FRETURN)
                        is ArrayType, StringType -> Pair(ALOAD, ARETURN)
                        else -> throw NotImplementedError()
                    }
                    mv.visitVarInsn(loadOp, ins.value.id)
                    mv.visitInsn(retOp)
                } else {
                    mv.visitInsn(RETURN)
                }
                is IRLabel -> mv.visitLabel(labels[ins.id])
                is IRJump -> mv.visitJumpInsn(GOTO, labels[ins.label.id])
                is IRConditionalJump -> {
                    mv.visitVarInsn(ILOAD, ins.cond.id)
                    mv.visitJumpInsn(IFNE, labels[ins.label.id])
                }
                is IRPrintln -> {
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                    val (loadOp, storeOp) = when (ins.type) {
                        IntegerType, BooleanType, CharType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(ALOAD, ASTORE)
                    }
                    mv.visitVarInsn(loadOp, ins.value.id)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/io/PrintStream", "println", "(${ins.type.toJVMStr()})V", false
                    )
                }
                is IRPrint -> {
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                    val (loadOp, storeOp) = when (ins.type) {
                        IntegerType, BooleanType, CharType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(ALOAD, ASTORE)
                    }
                    mv.visitVarInsn(loadOp, ins.value.id)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/io/PrintStream", "print", "(${ins.type.toJVMStr()})V", false
                    )
                }
            }

        }
        mv.visitLabel(end)
        mv.visitMaxs(0, 0)
        mv.visitEnd()
    }

    private fun Type.toJVMInt(): Int = when (this) {
        CharType -> T_CHAR
        IntegerType -> T_INT
        BooleanType -> T_BOOLEAN
        FloatType -> T_FLOAT
        else -> throw NotImplementedError()
    }

    private fun Type.toJVMStr(): String = when (this) {
        is ArrayType -> "[${elementType.toJVMStr()}"
        CharType -> "C"
        IntegerType -> "I"
        BooleanType -> "Z"
        FloatType -> "F"
        StringType -> "Ljava/lang/String;"
        VoidType -> "V"
        is FunctionType -> throw NotImplementedError()
    }
}
