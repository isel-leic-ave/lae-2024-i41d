package pt.isel.annotations

import org.junit.jupiter.api.Test
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

class ColorAnnotationTest {
    @Test fun checkColorOnAccount() {
        Account::class
            .annotations
            .forEach { println(it) }
    }
    @Test fun checkColorAndTagOnAccount() {
        println("Is Account with Color = ${Account::class.hasAnnotation<Color>()}")
        println("Is Account with Tag = ${Account::class.hasAnnotation<Tag>()}")
    }
    @Test fun checkTagOnFunctionsParameters() {
        Account::class
            .members
            .forEach { it.parameters.forEach { param ->
                param.annotations.forEach { annot ->
                    println("${param.name} has $annot")
                }
            } }
    }
    @Test fun checkTagOnProperties() {
        Account::class
            .memberProperties
            .forEach {
                val color = it.findAnnotation<Color>()
                println("${it.name} prop has annotation with ${color?.label}")
            }
    }
    @Test fun checkTagOnCtorParameters() {
        Account::class
            .constructors
            .first()
            .parameters
            .forEach {
                val color = it.findAnnotation<Color>()
                println("${it.name} param has annotation with ${color?.label}")
            }
    }
}