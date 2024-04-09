package pt.isel

import kotlin.reflect.KClass

class ComparerOrder<T : Any>(klass: KClass<T>) : Comparator<T> {
    override fun compare(o1: T, o2: T): Int {
        TODO("Not yet implemented")
    }

}
