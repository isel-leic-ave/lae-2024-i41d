package pt.isel

import java.lang.Thread.sleep
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume

fun main() {
    val seq = fetchManyCpsLazy(
        "https://github.com",
        "https://stackoverflow.com/",
        "https://developer.mozilla.org/",
    )
    val iter = seq.iterator()
    iter.next()
    iter.next()
}

fun fetchManyCpsLazy(url1: String, url2: String, url3: String) :Sequence<String> {
    return object : Sequence<String> {
        override fun iterator(): Iterator<String> {
            return FetchCpsManyIterator(url1, url2, url3)
        }

    }
}

class FetchCpsManyIterator(val url1: String, val url2: String, val url3: String)
    : Continuation<String?>, Iterator<String>
{
    var label = 0
    lateinit var nextItem: String

    override fun resumeWith(result: Result<String?>) {
        result.getOrThrow()?.let {
            nextItem = it
        }
    }
    fun block() {
        when (label++) {
            0 -> fetchCps(url1, this)
            1 -> fetchCps(url2, this)
            2 -> fetchCps(url3, this)
            3 -> throw NoSuchElementException()
        }
    }

    override val context = EmptyCoroutineContext
    override fun hasNext() = label < 3

    override fun next(): String {
        block()
        return nextItem
    }
}
