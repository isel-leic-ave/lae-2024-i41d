package pt.isel

import java.time.LocalDate

data class Weather (
    val date: LocalDate,
    val tempC: Int,
    val windspeedKmph: Int,
    val weatherDesc: String,
    val precipMM: Double,
    val humidity: Int,
    val cloudcover: Int,
)

 fun String.fromCsvToWeather(): Weather {
     val words = this.split(",")
     return Weather(
         LocalDate.parse(words[0]),
         words[2].toInt(),
         words[5].toInt(),
         words[10],
         words[11].toDouble(),
         words[13].toInt(),
         words[18].toInt(),
     )
 }