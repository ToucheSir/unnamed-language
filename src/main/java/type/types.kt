package type

sealed class Type(val name: String) {
    override fun toString() = name
}

data class ArrayType(val elementType: Type, val size: Int) : Type(elementType.name) {
    override fun toString() = "$elementType[$size]"
}

object CharType : Type("char")
object IntegerType : Type("int")
object BooleanType : Type("boolean")
object FloatType : Type("float")
object StringType : Type("string")
object VoidType : Type("void")

data class FunctionType(val returnType: Type, val parameterTypes: List<Type>): Type(returnType.name)