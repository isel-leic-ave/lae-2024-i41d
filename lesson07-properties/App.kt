class Person(var name: String)

class Rectangle(val width: Int, val height: Int) {
    val area = width * height
}

fun main() {
    val p = Person("Maria")
    println(p.name)
    p.name = "Manuela" // => p.setName("Manuela")
}