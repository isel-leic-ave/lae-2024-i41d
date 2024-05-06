package pt.isel

fun <T, R> Iterable<T>.eagerMap(transform: (T) -> R) : Iterable<R> {
    val destination = mutableListOf<R>()
    for (item in this)
        destination.add(transform(item))
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
    TODO()
}