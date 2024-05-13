package pt.isel

import java.util.NoSuchElementException
import kotlin.random.Random

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


fun randInts() : Sequence<Int> {
    return object : Sequence<Int> {
        override fun iterator(): Iterator<Int> {
            val rand = Random(100)
            return object : Iterator<Int> {
                override fun hasNext() = true
                override fun next() = rand.nextInt()
            }
        }

    }
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

fun <T, R> Sequence<T>.suspendMap(transform: (T) -> R) : Sequence<R> {
    // for (item in this)
    //    destination.add(transform(item))
    return sequence {
        val iter = this@suspendMap.iterator()
        while(iter.hasNext()) {
            val item = transform(iter.next())
            yield(item)
        }
    }
}

fun <T> Sequence<T>.suspendDistinct() : Sequence<T> {
    return sequence {
        val destination = mutableSetOf<T>()
        for (item in this@suspendDistinct) {
            if(destination.add(item)) {
                yield(item)
            }
        }
    }
}

fun <T> Sequence<T>.concat(other: Sequence<T>) : Sequence<T> {
    return sequence {
        for (item in this@concat) {
                yield(item)
        }
        for (item in other){
                yield(item)
        }

    }
}

fun <T> Sequence<T>.collapse() : Sequence<T> {
    return sequence {
        val iter = this@collapse.iterator()
        var curr: T? = null
        while (iter.hasNext()){
            val item = iter.next()
            if(item != curr) {
                curr = item
                yield(item)
            }
        }
    }
}