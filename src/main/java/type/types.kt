package type

sealed class Type(val name: String) {
    override fun toString() = name
}

data class ArrayType(private val elementType: Type, private val size: Int) : Type(elementType.name) {
    override fun toString() = "$elementType[$size]"
}

object CharType : Type("char")
object IntegerType : Type("int")
object BooleanType : Type("boolean")
object FloatType : Type("float")
object StringType : Type("string")
object VoidType : Type("void")