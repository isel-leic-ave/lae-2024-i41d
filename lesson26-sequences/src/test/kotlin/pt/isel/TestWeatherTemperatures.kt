package pt.isel

import org.junit.jupiter.api.Test
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

}