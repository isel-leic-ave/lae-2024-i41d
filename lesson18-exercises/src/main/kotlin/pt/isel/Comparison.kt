package pt.isel

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY)
annotation class Comparison(
    val index: Int = -1,
    val cmp: KClass<*> = Nothing::class
)
