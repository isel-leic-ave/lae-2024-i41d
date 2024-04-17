package pt.isel

import kotlin.reflect.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

/**
 * 3rd version
 */
class NaiveMapper<T : Any> private constructor(val srcType: KClass<*>, val destType:KClass<T>) : Mapper<T> {
    companion object {
        private val mappers = mutableMapOf<Pair<KClass<*>, KClass<*>>, NaiveMapper<*>>()

        fun <T : Any> mapper(srcType: KClass<*>, destType:KClass<T>) : NaiveMapper<T> {
            return mappers.getOrPut(srcType to destType) {
                NaiveMapper(srcType, destType)
            } as NaiveMapper<T>
        }
    }
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

    /**
     * 3rd - Get the values of properties from source and pass them
     * to te constructor through the callBy()
     */
    override fun mapFrom(src: Any) : T {
        val args: Map<KParameter, Any?> = args
            .map { pair ->
                val propVal = pair.value?.call(src)
                pair.key to parse(pair.value!!.returnType, pair.key.type, propVal)
            }
            .associate { it }
        return destCtor.callBy(args)
    }
}

private fun parse(srcType: KType, destType: KType, propVal: Any?): Any? {
    if(propVal == null) return null
    val srcKlass = srcType.classifier as KClass<*>
    val destKlass = destType.classifier as KClass<*>
    if(srcKlass.javaPrimitiveType != null || srcKlass == String::class) {
        return propVal
    }
    if(srcKlass == List::class && destKlass == List::class) {
        val elemSrcKlass = srcType.arguments[0].type?.classifier as KClass<*>
        val elemDestKlass = destType.arguments[0].type?.classifier as KClass<*>
        val mapper = NaiveMapper.mapper(elemSrcKlass, elemDestKlass)
        return (propVal as List<Any>).map {
            mapper.mapFrom(it)
        }
    }
    val mapper = NaiveMapper.mapper(srcKlass, destKlass)
    return mapper.mapFrom(propVal)
}

private fun matchPropWithParam(srcProp: KProperty<*>, param: KParameter) : Boolean {
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
