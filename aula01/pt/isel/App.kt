package pt.isel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.Temporal;

fun main() {
    reverse(LocalDate.now());
    reverse(Instant.now());
}

fun reverse(milestone: Temporal) : Temporal? {
    return null;
}

const val BITS_OF_10KB = 8*10*1024;