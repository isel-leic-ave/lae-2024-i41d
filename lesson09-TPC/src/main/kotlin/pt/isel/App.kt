package pt.isel

import java.io.File
import java.util.Collections.list
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.typeOf


fun main() {
    listClassesInClasspath().forEach { k ->
        k.members.forEach { m ->
            println(m)
            callParameterlessMethod(k, m)
        }
    }
}

fun callParameterlessMethod(owner: KClass<*>, func: KCallable<*>) {
    if (func.parameters.size == 1
        // && func.parameters.first() == func.instanceParameter
        && func.parameters[0].kind == KParameter.Kind.INSTANCE
        // && func.returnType != typeOf<Unit>()
        && func.returnType.classifier != Unit::class)
    {
        val ownerInstance = owner.createInstance()
        val result = func.call(ownerInstance)
        println(" >>>> $result")
    }
}
fun listClassesInClasspath(): List<KClass<*>> {
    val cl = ClassLoader.getSystemClassLoader() // An AppClassLoader instance
    return list(cl.getResources("")) // List
        .filter { url -> url.protocol.equals("file") } // Exclude jar files
        .map { url -> File(url.toURI()) }
        .flatMap { f -> if (f.isDirectory) f.listFiles().asList() else emptyList() }
        .filter { f -> f.name.contains(".class") }
        .map { f -> cl.loadClass(f.qualifiedName()).kotlin }
}

private fun File.qualifiedName(): String {
    return name
        .replace('/', '.')
        .substring(0, name.length - ".class".length)
}