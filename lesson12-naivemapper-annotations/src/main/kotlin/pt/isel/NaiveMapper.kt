package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

/**
 * 3rd version
 */
class NaiveMapper<T : Any>(val srcType: KClass<*>, val destType:KClass<T>) {
    /**
     * 1st - selects the constructor with all mandatory parameters
     * in the properties of the source.
     */
    private val destCtor = destType
        .constructors
        .first { ctor ->
            ctor
                .parameters
                .filter { !it.isOptional }
                .all { param -> srcType
                    .memberProperties
                    .any { matchPropWithParam(it, param) }
                }
        }
    /**
     * 2nd - look for matching properties with ctor parameters
     */
    private val args: Map<KParameter, KProperty<*>?> = destCtor
        .parameters
        .associateWith { param -> srcType
            .memberProperties
            .firstOrNull() { matchPropWithParam(it, param) }
        }
        .filter { it.value != null }

    private fun matchPropWithParam(srcProp: KProperty<*>, param: KParameter) : Boolean {
        if(srcProp.name == param.name) {
            return srcProp.returnType == param.type
        }
        val annot = srcProp.findAnnotation<MapProp>() ?: return false
        return annot.paramName == param.name && srcProp.returnType == param.type
    }

    /**
     * 3rd - Get the values of properties from source and pass them
     * to te constructor through the callBy()
     */
    fun mapFrom(src: Any) : T {
        val args: Map<KParameter, Any?> = args
            .map { pair -> pair.key to pair.value?.call(src) }
            .associate { it }
        return destCtor.callBy(args)
    }
}

/**
 * 2nd Version of NaiveMapper
 * Copy properties to the parameters of the constructor of dest.
 * It selects the constructor with all mandatory parameters
 * in the properties of the source.
 */
fun <T : Any> Any.mapTo(dest: KClass<T>) : T {
    /**
     * 1st - selects the constructor with all mandatory parameters
     * in the properties of the source.
     */
    val destCtor = dest
        .constructors
        .first { ctor ->
            ctor
                .parameters
                .filter { !it.isOptional }
                .all { param -> this::class
                    .memberProperties
                    .any { it.name == param.name && it.returnType == param.type}
                }
        }
    val args: Map<KParameter, Any?> = destCtor
        .parameters
        .associateWith { param -> this::class
            .memberProperties
            .firstOrNull() { it.name == param.name && it.returnType == param.type }
            ?.call(this)
        }
        .filter { it.value != null }
    /**
     * 3rd create the instance of dest via constructor using
     * the callBy<Map<KParameter, Any>>
     */
    return destCtor.callBy(args)
}

/**
 * 1st Version of NaiveMapper
 * Copy properties to mutable properties in dest.
 * And, dest must have a parameterless constructor
 */
fun <T : Any> Any.mapToProps(dest: KClass<T>) : T {
    /**
     * 1st - Create instance of dest KClass (i.e. target)
     * dest should have a parameterless constructor or with
     * all parameters optional.
     * Otherwise, it will throw an Exception.
     */
    val target: T = dest.createInstance()
    /**
     * 2nd - Look for matching properties (i.e. same name and type)
     * between the source (i.e. Receiver) and the dest type.
     */
    this::class
        .memberProperties
        .forEach { srcProp ->
            val destProp = dest.memberProperties.firstOrNull {
                it.name == srcProp.name && it.returnType == srcProp.returnType
            }
            /*
            * 3rd - Copy the property value from the Receiver to the target (i.e. object 1).
            */
            if(destProp != null && destProp is KMutableProperty<*>) {
                val srcValue = srcProp.call(this)
                destProp.setter.call(target, srcValue)
            }
        }
    return target
}