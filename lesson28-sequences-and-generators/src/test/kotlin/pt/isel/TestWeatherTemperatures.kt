package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

/*
 * Located on testes resources
 */
private const val weatherPath = "q-Lisbon_format-csv_date-2020-05-08_enddate-2020-06-11.csv"

class TestWeatherTemperatures {

    fun loadNaiveCsv(): List<Weather> {
        return ClassLoader
            .getSystemResource(weatherPath)
            .openStream()
            .reader()
            .readLines()
            .filter { !it.startsWith('#') } // Filter comments
            .drop(1) // Skip line: Not available
            .filterIndexed { index, _ ->  index % 2 != 0} // Filter hourly info
            .map { it.fromCsvToWeather() }
    }

    private val weatherData = loadNaiveCsv()

    @Test fun checkData() {
        weatherData
            .forEach { println(it) }
    }

    @Test fun countDistinctDescriptionsInRainyDaysMapFilter() {
        var iters = 0
        val size = weatherData
            .eagerMap { iters++; it.weatherDesc }
            .eagerFilter { iters++; it.lowercase().contains("rain") }
            .eagerDistinct()
            .count()
        assertEquals(5, size)
        assertEquals(70, iters)
    }

    @Test fun countDistinctDescriptionsInRainyDaysFilterMap() {
        var iters = 0
        val size = weatherData
            .eagerFilter { iters++; it.weatherDesc.lowercase().contains("rain") }
            .eagerMap { iters++; it.weatherDesc }
            .distinct()
            .count()
        assertEquals(5, size)
        assertEquals(48, iters)
    }
    @Test fun firstDescriptionInWindyDays() {
        var iters = 0
        val desc = weatherData
            .filter { iters++; it.windspeedKmph > 22 }
            .map { iters++; it.weatherDesc }
            //.first()
        //assertEquals("Light rain shower", desc)
        assertEquals(37, iters)
    }
    @Test fun firstDescriptionInWindyDaysAsSequenceWithoutTerminalOperation() {
        var iters = 0
        val desc = weatherData
            .asSequence()
            .filter { iters++; it.windspeedKmph > 22 }
            .lazyMap { iters++; it.weatherDesc }
            // .first()
        // assertEquals("Light rain shower", desc)
        /**
         * NO Processing WITHOUT a terminal operation
         */
        assertEquals(0, iters)
    }
    @Test fun firstDescriptionInWindyDaysAsSequence() {
        var iters = 0
        val desc = weatherData
            .asSequence()
            .filter { iters++; it.windspeedKmph > 22 }
            .lazyMap { iters++; it.weatherDesc }
            .first()
        assertEquals("Light rain shower", desc)
        assertEquals(5, iters)
    }
    @Test fun countDistinctDescriptionsInRainyDaysFilterMapLazy() {
        var iters = 0
        val size = weatherData
            .asSequence()
            .filter { iters++; it.weatherDesc.lowercase().contains("rain") }
            .lazyMap { iters++; it.weatherDesc }
            .lazyDistinct()
            .onEach { println(it) }
            .count()
        assertEquals(5, size)
        assertEquals(48, iters)
    }
    @Test fun testDistinctOnNulls() {
        val actual = sequenceOf(1, 2, 4, 2, 2, 5, null, null, 3, 7, 9)
            .lazyDistinct()

        assertContentEquals(
            sequenceOf(1,2,4,5,null,3,7,9),
            actual.onEach { println(it) }
        )
    }
    @Test fun checkRandomSequence() {
        val nrs = randInts()
        nrs
            .take(10)
            .forEach { println(it) }
    }

    @Test fun checkBuildSequence() {
        val nrs: Sequence<Int> = sequence<Int> {
            println("Starting")
            yield(7)
            println("Step 1")
            yield(11)
            println("Step 2")
            yield(33)
            println("Step 3")
        }
        val iter = nrs.iterator()
        iter.next()
        iter.next()
    }

    private fun <T> buildSequence(block: suspend MyScope<T>.() -> Unit) {
        TODO("Not yet implemented")
    }
    class MyScope<T> {
        suspend fun yield(item: T) {}
    }

    @Test fun countDistinctDescriptionsInRainyDaysFilterMapLazySuspend() {
        var iters = 0
        val size = weatherData
            .asSequence()
            .filter { iters++; it.weatherDesc.lowercase().contains("rain") }
            .suspendMap { iters++; it.weatherDesc }
            .suspendDistinct()
            .onEach { println(it) }
            .count()
        assertEquals(5, size)
        assertEquals(48, iters)
    }

    @Test fun checkCollapse() {

        val res = sequenceOf(7, 6, 5, 5, 6, 6, 7, 5)
            .collapse() // Merges series of adjacent elements
        assertContentEquals(
            sequenceOf(7,6,5,6,7,5),
            res
        )
    }

    @Test fun checkConcat() {
        // <=> sequenceOf(7,6,5) + sequenceOf(3,4)
        val res = sequenceOf(7,6,5).concat(sequenceOf(3,4))
        assertContentEquals(
            sequenceOf(7,6,5,3,4),
            res
        )
    }
}

