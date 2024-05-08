package pt.isel

import java.util.NoSuchElementException

fun <T, R> Iterable<T>.eagerMap(transform: (T) -> R) : Iterable<R> {
    val destination = mutableListOf<R>()
    // for (item in this)
    //    destination.add(transform(item))
    val iter = this.iterator()
    while(iter.hasNext()) {
        destination.add(transform(iter.next()))
    }
    return destination
}

fun <T> Iterable<T>.eagerFilter(predicate: (T) -> Boolean) : Iterable<T> {
    val destination = mutableListOf<T>()
    for (item in this)
        if(predicate(item))
            destination.add(item)
    return destination
}

fun <T> Iterable<T>.eagerDistinct() : Iterable<T> {
    val destination = mutableSetOf<T>()
    for (item in this)
        destination.add(item)
    return destination
}

fun <T, R> Sequence<T>.lazyMap(transform: (T) -> R) : Sequence<R> {
    return object : Sequence<R> {
        override fun iterator(): Iterator<R> {
            return object : Iterator<R> {
                val iter = this@lazyMap.iterator()
                override fun hasNext() = iter.hasNext()
                override fun next() = transform(iter.next())
            }
        }
    }
}

/**
 * DOES NOT SUPPORT Sequences with null elements
 */
fun <T> Sequence<T>.lazyDistinct() : Sequence<T> {
    return object : Sequence<T> {
        override fun iterator(): Iterator<T> {
            return object : Iterator<T> {
                val iter = this@lazyDistinct.iterator()
                val selected = mutableSetOf<T>()
                var nextItem: T? = null

                private fun advance() {
                    if(!iter.hasNext()) return
                    val item = iter.next()
                    if(!selected.contains(item)) {
                        selected.add(item)
                        nextItem = item
                    } else {
                      advance()
                    }
                }

                override fun hasNext(): Boolean {
                    if(nextItem == null) advance()
                    return nextItem != null
                }

                override fun next(): T {
                    if(!hasNext()) throw NoSuchElementException()
                    val curr = nextItem !!
                    nextItem = null
                    return curr
                }
            }
        }
    }
}

