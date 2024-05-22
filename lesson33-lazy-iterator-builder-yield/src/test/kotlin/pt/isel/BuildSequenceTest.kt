package pt.isel

import kotlin.test.*

class BuildSequenceTest {

    @Test fun simpleSequence() {
        val iter = buildSequence {
            yield(7)
            yield(6)
            yield(5)
        }.iterator()
        assertTrue(iter.hasNext())
        assertEquals(7, iter.next())
        assertEquals(6, iter.next())
        assertEquals(5, iter.next())
        assertFalse(iter.hasNext())
    }

    @Test fun simpleSequenceForeach() {
        val expected = (9 downTo 3).iterator()
        val nrs = buildSequence{
            (9 downTo 3).forEach {
                yield(it)
            }
        }
        nrs.forEach { actual ->
            assertEquals(expected.next(), actual)
        }
    }

    @Test fun testFibonacci() {
        val expected = sequenceOf(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181).iterator()
        var curr = 0;
        var next = 1;
        val fib = buildSequence{
            while(true) {
                yield(curr)
                curr = next.also { next += curr }
            }
        }
        fib.take(20).forEach { actual ->
            assertEquals(expected.next(), actual)
        }
    }

    @Test fun sequenceOfNulls() {
        val expected = listOf<String?>(null, null, null).iterator()
        val actual = buildSequence<String?> {
            (1..3).forEach { _ -> yield(null) }
        }
        actual.forEach {
            assertEquals(expected.next(), it)
        }
    }

    @Test fun emptySequence() {
        val actual = buildSequence<Int> { /* yield nothing */ }.iterator()
        assertFalse(actual.hasNext())
    }
    @Test fun sequenceWithError() {
        val actual = buildSequence<Int> {
            yield(1)
            throw CustomError()
        }.iterator()
        assertTrue(actual.hasNext())
        assertEquals(1, actual.next())
        assertFailsWith<CustomError> { actual.next() }
    }
    class CustomError : RuntimeException()
}