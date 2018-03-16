package codegen

import ir.*
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import type.*
import java.io.OutputStream

class JVMCodeGenerator(val programName: String, val out: OutputStream) : CodeGenerator {
    private val cw = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)

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
        main.visitMethodInsn(INVOKESTATIC, "ULMain", "main", "()V", false)
        main.visitInsn(RETURN)
        main.visitMaxs(1, 1)
        main.visitEnd()

        program.functions.forEach(this::generateFunction)
        cw.visitEnd()
        out.write(cw.toByteArray())
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
        func.instructions.forEach {
            when (it) {
                is IRLoadImmediate<*> -> {
                    mv.visitLdcInsn(it.value)

                    val (loadOp, storeOp) = when (it.tmp.type) {
                        IntegerType, CharType, BooleanType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(ALOAD, ASTORE)
                    }
                    mv.visitVarInsn(storeOp, it.tmp.id)
                }
                is IRInitArray -> {
                    val aType = it.tmp.type as ArrayType
                    mv.visitLdcInsn(aType.size)
                    mv.visitIntInsn(NEWARRAY, aType.elementType.toJVMInt())
                    mv.visitVarInsn(ASTORE, it.tmp.id)
                }
                is IRFunctionCall -> {
                    for (arg in it.args) mv.visitVarInsn(
                        when (arg.type) {
                            IntegerType, BooleanType, CharType -> ILOAD
                            FloatType -> FLOAD
                            StringType, is ArrayType -> ALOAD
                            else -> throw UnsupportedOperationException()
                        }, arg.id
                    )
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "ULMain",
                        it.name,
                        "(${it.args.joinToString("") { it.type.toJVMStr() }})${(it.retVal?.type
                                ?: VoidType).toJVMStr()}",
                        false
                    )
                    it.retVal?.run {
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
                    val (loadOp, storeOp) = when (it.res.type) {
                        IntegerType, BooleanType -> Pair(IALOAD, ISTORE)
                        CharType -> Pair(CALOAD, ISTORE)
                        FloatType -> Pair(FALOAD, FSTORE)
                        StringType -> Pair(AALOAD, ASTORE)
                        else -> throw UnsupportedOperationException()
                    }
                    mv.visitVarInsn(ALOAD, it.arr.id)
                    mv.visitVarInsn(ILOAD, it.index.id)
                    mv.visitInsn(loadOp)
                    mv.visitVarInsn(storeOp, it.res.id)
                }
                is IRBinaryOp -> {
                    val (loadOp, storeOp) = when (it.lhs.type) {
                        IntegerType, CharType, BooleanType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        StringType -> Pair(ALOAD, ASTORE)
                        else -> throw UnsupportedOperationException()
                    }
                    val (addOp, subOp, mulOp) = when (it.lhs.type) {
                        IntegerType, CharType, BooleanType -> Triple(IADD, ISUB, IMUL)
                        FloatType -> Triple(FADD, FSUB, FMUL)
                        else -> throw UnsupportedOperationException()
                    }
                    when (it.operator) {
                        IRBinOp.ADD -> {
                            mv.visitVarInsn(loadOp, it.lhs.id)
                            mv.visitVarInsn(loadOp, it.rhs.id)
                            mv.visitInsn(addOp)
                            mv.visitVarInsn(storeOp, it.res.id)
                        }
                        IRBinOp.SUBTRACT -> {
                            mv.visitVarInsn(loadOp, it.lhs.id)
                            mv.visitVarInsn(loadOp, it.rhs.id)
                            mv.visitInsn(subOp)
                            mv.visitVarInsn(storeOp, it.res.id)
                        }
                        IRBinOp.MULTIPLY -> {
                            mv.visitVarInsn(loadOp, it.lhs.id)
                            mv.visitVarInsn(loadOp, it.rhs.id)
                            mv.visitInsn(mulOp)
                            mv.visitVarInsn(storeOp, it.res.id)
                        }
                        IRBinOp.DIVIDE -> TODO()
                        IRBinOp.REMAINDER -> TODO()
                        IRBinOp.LESSTHAN -> {
                            mv.visitVarInsn(loadOp, it.lhs.id)
                            mv.visitVarInsn(loadOp, it.rhs.id)

                            val thenLabel = Label()
                            val endLabel = Label()
                            if (it.lhs.type == FloatType) {
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

                            mv.visitVarInsn(ISTORE, it.res.id)
                        }
                        IRBinOp.LESSTHANEQ -> TODO()
                        IRBinOp.EQUALS -> {
                            mv.visitVarInsn(loadOp, it.lhs.id)
                            mv.visitVarInsn(loadOp, it.rhs.id)

                            val thenLabel = Label()
                            val endLabel = Label()
                            if (it.lhs.type == FloatType) {
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

                            mv.visitVarInsn(ISTORE, it.res.id)
                        }
                        IRBinOp.NOTEQUALS -> TODO()
                        IRBinOp.GREATERTHANEQ -> TODO()
                        IRBinOp.GREATERTHAN -> TODO()
                    }
                }
                is IRUnaryOp -> when (it.operator) {
                    IRUnOp.NOT -> if (it.operand.type != BooleanType) throw NotImplementedError() else {
                        val notZeroLabel = Label()
                        val endLabel = Label()
                        mv.visitVarInsn(ILOAD, it.operand.id)

                        mv.visitJumpInsn(IFNE, notZeroLabel)
                        mv.visitIntInsn(BIPUSH, 1)
                        mv.visitJumpInsn(GOTO, endLabel)
                        mv.visitLabel(notZeroLabel)
                        mv.visitIntInsn(BIPUSH, 0)
                        mv.visitLabel(endLabel)

                        mv.visitVarInsn(ISTORE, it.res.id)
                    }
                    IRUnOp.NEGATE -> {
                        val (loadOp, storeOp) = when (it.operand.type) {
                            IntegerType, BooleanType, CharType -> Pair(ILOAD, ISTORE)
                            FloatType -> Pair(FLOAD, FSTORE)
                            else -> Pair(0, 0)
                        }
                        val (negOp, notOp) = when (it.operand.type) {
                            IntegerType -> Pair(IADD, ISUB)
                            FloatType -> Pair(FADD, FSUB)
                            else -> Pair(0, 0)
                        }
                    }
                }
                is IRAssignment -> if (it.index != null) {
                    val (loadOp, storeOp) = when (it.src.type) {
                        IntegerType -> Pair(ILOAD, IASTORE)
                        FloatType -> Pair(FLOAD, FASTORE)
                        CharType -> Pair(ILOAD, CASTORE)
                        else -> Pair(0, 0)
                    }
                    mv.visitVarInsn(ALOAD, it.dest.id)
                    mv.visitVarInsn(ILOAD, it.index.id)
                    mv.visitVarInsn(loadOp, it.src.id)
                    mv.visitInsn(storeOp)
                } else {
                    val (loadOp, storeOp) = when (it.dest.type) {
                        IntegerType, CharType, BooleanType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(0, 0)
                    }
                    mv.visitVarInsn(loadOp, it.src.id)
                    mv.visitVarInsn(storeOp, it.dest.id)
                }
                is IRReturn -> if (it.value != null) {
                    val (loadOp, retOp) = when (it.value.type) {
                        CharType, IntegerType, BooleanType -> Pair(ILOAD, IRETURN)
                        FloatType -> Pair(FLOAD, FRETURN)
                        is ArrayType, StringType -> Pair(ALOAD, ARETURN)
                        else -> throw NotImplementedError()
                    }
                    mv.visitVarInsn(loadOp, it.value.id)
                    mv.visitInsn(retOp)
                } else {
                    mv.visitInsn(RETURN)
                }
                is IRLabel -> mv.visitLabel(labels[it.id])
                is IRJump -> mv.visitJumpInsn(GOTO, labels[it.label.id])
                is IRConditionalJump -> {
                    mv.visitVarInsn(ILOAD, it.cond.id)
                    mv.visitJumpInsn(IFNE, labels[it.label.id])
                }
                is IRPrintln -> {
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                    val (loadOp, storeOp) = when (it.type) {
                        IntegerType, BooleanType, CharType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(ALOAD, ASTORE)
                    }
                    mv.visitVarInsn(loadOp, it.value.id)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/io/PrintStream", "println", "(${it.type.toJVMStr()})V", false
                    )
                }
                is IRPrint -> {
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                    val (loadOp, storeOp) = when (it.type) {
                        IntegerType, BooleanType, CharType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(ALOAD, ASTORE)
                    }
                    mv.visitVarInsn(loadOp, it.value.id)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/io/PrintStream", "print", "(${it.type.toJVMStr()})V", false
                    )
                }
            }

        }
        mv.visitLabel(end)
        mv.visitMaxs(1000, 100)
        mv.visitEnd()
    }

    private fun generateVariable(temp: Temporary, mv: MethodVisitor) {
//        mv.visitVa
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
