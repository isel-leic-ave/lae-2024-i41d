package pt.isel

import org.cojen.maker.ClassMaker
import org.cojen.maker.Variable
import java.lang.reflect.Constructor
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaGetter

fun <T : Any> dynamicMapper(srcType: KClass<*>, destType: KClass<T>) : Mapper<T> {
    return builDynamicMapper(srcType, destType)
        .finish()
        .kotlin
        .createInstance() as Mapper<T>
}
/*
public class MapperArtistSpotify2Artist : Mapper<Artist> {
    override fun mapFrom(src: Any): Artist {
        // 0. cast receiver to srcType
        val srcArtist = src as ArtistSpotify

        // 1. Read properties from src
        val name = src.name
        val country = src.country

        // 2. Instantiate Artist with the values of former properties
        return Artist(name, State(country.name, country.idiom))
    }
}
*/
fun <T : Any> builDynamicMapper(srcType: KClass<*>, destType: KClass<T>): ClassMaker {
    return ClassMaker
        .begin("Mapper${srcType.simpleName}2${destType.simpleName}")
        .public_()
        .implement(Mapper::class.java)
        .apply {
            addConstructor().public_()
            addMapFromMethod(srcType, destType)
        }
}

private fun <T : Any> ClassMaker.addMapFromMethod(srcType: KClass<*>, destType: KClass<T>) {
    val destInit: Constructor<*> = destType
        .java
        .constructors
        .sortedByDescending { it.parameters.size }
        .first { init -> init.parameters
            .all { param: Parameter -> srcType.memberProperties
                .any {  prop: KProperty<*> ->
                    matchPropWithParam(prop, param)
                }
            }
        }
    val props = destInit.parameters.map { param ->
        srcType.memberProperties.first { prop -> matchPropWithParam(prop, param)}
    }
    /*
    // 0. cast receiver to srcType
    ArtistSpotify srcArtist = (ArtistSpotify) src

    // 1. Read properties from src
    val name = src.getName()
    val country = src.country

    // 2. Instantiate Artist with the values of former properties
    return Artist(name, State(country.name, country.idiom))
    */
    addMethod(Any::class.java, "mapFrom", Any::class.java)
        .public_()
        .apply {
            val src = param(0).cast(srcType.java) // <=> val srcArtist = src as ArtistSpotify
            val propValues: List<Variable> = props.map { prop ->
                src.invoke(prop.javaGetter?.name) // <=> e.g. val name = src.getName()
                // If param dest type is != property src type
                // => get a new Dynamic Mapper and convert the prop value
            }
            return_(new_(destType.java, *propValues.toTypedArray())) // <=> return Artist(name, country)
        }
}


private fun matchPropWithParam(srcProp: KProperty<*>, param: Parameter) : Boolean {
    val fromName = srcProp.findAnnotation<MapProp>()?.paramName ?: srcProp.name
    if(fromName != param.name) {
        return false
    }
    val srcKlass = srcProp.returnType.classifier as KClass<*>
    return if(srcKlass.javaPrimitiveType != null)
        srcProp.returnType == param.type
    else
        true
}
