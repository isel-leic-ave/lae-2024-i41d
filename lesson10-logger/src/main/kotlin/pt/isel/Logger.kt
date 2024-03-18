package pt.isel

import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

fun Appendable.log(obj: Any) : Unit{
    this.appendLine("Type of Object is ${obj::class.simpleName}")
    obj::class
        .memberProperties
        .forEach { prop ->
            prop.isAccessible = true
            val propValue = prop.call(obj)
            appendLine("  - ${prop.name}: $propValue")
        }
}
fun Appendable.logGetters(obj: Any) : Unit{
    this.appendLine("Type of Object is ${obj::class.simpleName}")
    obj::class
        .members
        .forEach { prop ->
            prop.isAccessible = true
            // For members with prefix get...
            // return type different from Unit
            // and a single parameter corresponding to the instance parameter

        }
}