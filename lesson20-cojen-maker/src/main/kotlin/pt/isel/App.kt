package pt.isel

import org.cojen.maker.ClassMaker
import org.cojen.maker.Variable
import java.io.FileOutputStream
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@JvmOverloads
fun foo(label: String, nr: Int = 7) {
}


class Student(val name: String, val nr: Int)

fun main() {
    foo("ola")
    foo("ola", 11)

    // switchJavaKotlinReflect(Student("Maria", 73641))

    ClassMaker
        .begin("MyClassName")
        .public_()
        .also { it
            .addMethod(Int::class.java, "foo")
            .return_(47)
        }
        .finishTo(FileOutputStream("MyClassName.class"))

    // buildDummyDynamic().finishTo(FileOutputStream("Dummy.class"))
    val receiver: Multiply = buildDummyDynamic()
        .finish()
        .kotlin
        .constructors
        .first()
        .call(7) as Multiply
    /*
    val res = receiver::class
        .memberFunctions
        .first { it.name == "mul" }
        .call(receiver, 3)
     */
    val res = receiver.mul(4)
    println(res)
}

fun switchJavaKotlinReflect(obj: Any) {
    println("######## Kotlin Reflect API on properties")
    obj::class.memberProperties.forEach { println(it.name) }
    println("######## Java  Reflect API on methos")
    obj::class.java.declaredMethods.forEach { println(it.name) }
    println("######## Kotlin Reflect API on properties")
    obj::class.java.kotlin.memberProperties.forEach { println(it.name) }
}

/**
 * GEnerates dynamically somthing like:
 *
    class Dummy {
        private final int nr;
        public Dummy(int nr) {
            this.nr = nr;
        }
        public int mul(int other) {
            return this.nr * other;
        }
    }
 */

interface Multiply{
    fun mul(nr: Int): Int
}

fun buildDummyDynamic() : ClassMaker {
    val cm = ClassMaker
        .begin("Dummy")
        .public_()
        .implement(Multiply::class.java)
    //
    // Field
    //
    val fieldNr = cm.addField(Int::class.java, "nr").private_().final_()
    //
    // Constructor
    //
    val init = cm
        .addConstructor(Int::class.java)
        .public_()
    init.invokeSuperConstructor()                 // super()
    init.field(fieldNr.name()).set(init.param(0)) // this.nr = nr
    //
    // Method mul
    //
    val methodMul = cm
        .addMethod(Int::class.java, "mul", Int::class.java)
        .public_()
    val res: Variable = methodMul.field(fieldNr.name()).mul(methodMul.param(0))
    methodMul.return_(res)
    return cm
}