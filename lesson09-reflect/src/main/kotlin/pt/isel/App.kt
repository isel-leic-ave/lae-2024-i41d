package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.memberProperties

/**
 * Represents the Property country in class Student.
 * Not the value.
 */
val propCountry = Student::class
    .memberProperties
    .first { it.name == "country" }

fun main() {
    val klass: KClass<Person> = Person::class
    val maria = Student("Maria", "Portugal")
    checkProperties(maria)
    checkCountry(maria)
    checkCountry(Person("Jose", "Brasil"))

}
fun checkMembers(obj: Any) {
    val klass: KClass<*> = obj::class
    klass.members.forEach { println(it) }
}
fun checkProperties(obj: Any) {
    val klass: KClass<*> = obj::class
    klass.memberProperties.forEach { println(it) }
}
fun checkCountry(obj: Any) {
    obj::class
        .memberProperties
        .first { it.name == "country" }
        .let { prop -> println(prop.call(obj)) }
}
