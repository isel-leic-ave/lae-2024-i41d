fun main() {
    val p = Person("Maria")
    val s = Student("Manel")
    printName(p)
    printName(s)
}

fun printName(o: Person) {
    println(o.name)
    o.print()
}

open class Person(open val name: String?) {
    open fun print() {
        println("I am a Person")
    }
}

class Student(override val name: String) : Person(null) {
    override fun print() {
        println("I am a Student")
    }
}
