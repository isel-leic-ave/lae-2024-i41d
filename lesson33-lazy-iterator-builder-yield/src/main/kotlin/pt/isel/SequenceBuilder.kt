package pt.isel

import pt.isel.SequenceState.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.resume

fun main() {
    val seq = buildSequence {
        val body1 = fetch("https://github.com")
        yield(body1)
        val body2 = fetch("https://stackoverflow.com/")
        yield(body2)
        val body3 = fetch("https://developer.mozilla.org/")
        yield(body3)
    }
    val iter = seq.iterator()
    iter.next()
    iter.next()
}

/**
 * <=> Existing sequence { ... } of Kotlin
 */
fun <T> buildSequence(block: suspend Yieldable<T>.() -> Unit) :Sequence<T> {
    return object : Sequence<T> {
        override fun iterator(): Iterator<T> {
            return SequenceBuilderIterator(block)
        }

    }
}

interface Yieldable<T> {
    suspend fun yield(item: T)
}

enum class SequenceState {Ready, NotReady, Done, Failed }

class SequenceBuilderIterator<T>(val block: suspend Yieldable<T>.() -> Unit)
    : Iterator<T>, Continuation<Unit>, Yieldable<T>
{
    private var finish: Result<Unit>? = null
    var nextItem: T? = null
    var nextCont: Continuation<Unit>? = null
    val blockHandle = block as (Yieldable<T>.(Continuation<Unit>) -> Any)
    var state = NotReady

    override suspend fun yield(item: T) = yieldHandle(item)

    val yieldHandle = ::yieldCps as (suspend (T) -> Unit)

    fun yieldCps(item: T, cont: Continuation<Unit>) : Any {
        nextItem = item
        nextCont = cont
        state = Ready
        return COROUTINE_SUSPENDED
    }

    private fun advance() {
        if(state == NotReady) {
            nextCont
                ?.resume(Unit)
                ?: blockHandle(this)
        }
    }

    override fun hasNext() : Boolean {
        advance()
        return state == Ready
    }

    override fun next(): T {
        finish?.getOrThrow()
        if(finish != null) throw NoSuchElementException()
        state = NotReady
        advance()
        return nextItem as T
    }

    override val context = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        finish = result
        if(result.isSuccess) state = Done
        else state = Failed
    }
}
