package codegen

import ir.*
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import type.*
import java.io.OutputStream
import java.lang.RuntimeException

class JVMCodeGenerator(val out: OutputStream) : CodeGenerator {
    private val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)

    override fun generate(program: IRProgram) {
        cw.visit(
            V1_6,
            ACC_PUBLIC,
            "ULMain",
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

        program.functions.filter { it.name in arrayOf("main", "populateCharTable") }.forEach(this::generateFunction)
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
        func.instructions.forEach {
            when (it) {
                is IRLoadImmediate<*> -> {
                    mv.visitLdcInsn(it.value)
                    val (loadOp, storeOp) = when (it.tmp.type) {
                        IntegerType, CharType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(ALOAD, ASTORE)
                    }
                    System.err.println(it.tmp.type)
                    mv.visitVarInsn(storeOp, it.tmp.id)
//                    mv.visitVarInsn(ISTORE, it.tmp.id)
                }
                is IRInitArray -> {
                    val aType = it.tmp.type as ArrayType
                    mv.visitLdcInsn(aType.size)
                    mv.visitIntInsn(NEWARRAY, aType.elementType.toJVMInt())
                    mv.visitVarInsn(ASTORE, it.tmp.id)
                }
//                is IRFunctionCall -> TODO()
//                is IRArrayReference -> TODO()
                is IRBinaryOp -> {
                    val (loadOp, storeOp) = when (it.lhs.type) {
                        IntegerType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(0, 0)
                    }
                    val (addOp, subOp, mulOp) = when (it.lhs.type) {
                        IntegerType -> Triple(IADD, ISUB, IMUL)
                        FloatType -> Triple(FADD, FSUB, FMUL)
                        else -> Triple(0, 0, 0)
                    }
                    if (it.lhs.type in arrayOf(IntegerType, FloatType)) {
                        System.err.println("${it.lhs.id} ${it.rhs.id}")
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
                        }
                    }
                }
//                is IRUnaryOp -> TODO()
                is IRAssignment -> if (it.index != null) {
                    val (loadOp, storeOp) = when (it.src.type) {
                        IntegerType -> Pair(ILOAD, IASTORE)
                        FloatType -> Pair(FLOAD, FASTORE)
                        CharType -> Pair(ILOAD, CASTORE)
                        else -> Pair(0, 0)
                    }
                    System.err.println(it)
//                    mv.visitVarInsn(ALOAD, 0)
//                    mv.visitVarInsn(ILOAD, it.index.id)
//                    mv.visitVarInsn(loadOp, it.src.id)
//                    mv.visitVarInsn(ALOAD, it.dest.id)
//                    mv.visitVarInsn(ILOAD, it.index.id)
//                    mv.visitVarInsn(loadOp, it.src.id)
//                    mv.visitInsn(storeOp)
                } else {
                    val (loadOp, storeOp) = when (it.dest.type) {
                        IntegerType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(0, 0)
                    }
                    mv.visitVarInsn(loadOp, it.src.id)
                    mv.visitVarInsn(storeOp, it.dest.id)
                }
//                is IRReturn -> TODO()
//                is IRLabel -> TODO()
//                is IRJump -> TODO()
//                is IRConditionalJump -> TODO()
                is IRPrintln -> {
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                    val (loadOp, storeOp) = when (it.type) {
                        IntegerType -> Pair(ILOAD, ISTORE)
                        FloatType -> Pair(FLOAD, FSTORE)
                        else -> Pair(ALOAD, ASTORE)
                    }
                    mv.visitVarInsn(loadOp, it.value.id)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/io/PrintStream", "println", "(${it.type.toJVMStr()})V", false
                    )
                }
//                is IRPrint -> TODO()
            }

        }
        mv.visitLabel(end)
        mv.visitInsn(RETURN)
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