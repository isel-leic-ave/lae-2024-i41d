package pt.isel

interface Mapper<T> {
    fun mapFrom(src: Any) : T
}