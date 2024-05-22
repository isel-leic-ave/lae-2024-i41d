package pt.isel

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.resume

fun main() {
    val seq = fetchManySuspendLazy(
        "https://github.com",
        "https://stackoverflow.com/",
        "https://developer.mozilla.org/",
    )
    val iter = seq.iterator()
    iter.next()
    iter.next()
}

fun fetchManySuspendLazy(url1: String, url2: String, url3: String) :Sequence<String> {
    return object : Sequence<String> {
        override fun iterator(): Iterator<String> {
            return FetchSuspendManyIterator(url1, url2, url3)
        }

    }
}

class FetchSuspendManyIterator(val url1: String, val url2: String, val url3: String)
    : Iterator<String>, Continuation<Unit>
{
    private var finish: Result<Unit>? = null
    lateinit var nextItem: String
    var nextCont: Continuation<Unit>? = null

    suspend fun block() {
        val body1 = fetch(url1)
        yield(body1)
        val body2 = fetch(url2)
        yield(body2)
        val body3 = fetch(url3)
        yield(body3)
    }

    suspend fun yield(item: String) = yieldHandle(item)

    val yieldHandle = ::yieldCps as (suspend (String) -> Unit)

    fun yieldCps(item: String, cont: Continuation<Unit>) : Any {
        nextItem = item
        nextCont = cont
        return COROUTINE_SUSPENDED
    }

    val blockHandle = ::block as ((Continuation<Unit>) -> Any)

    override fun hasNext() = finish == null

    override fun next(): String {
        finish?.getOrThrow()
        if(finish != null) throw NoSuchElementException()
        nextCont
            ?.resume(Unit)
            ?: blockHandle(this)
        return nextItem
    }

    override val context = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        finish = result
    }
}
