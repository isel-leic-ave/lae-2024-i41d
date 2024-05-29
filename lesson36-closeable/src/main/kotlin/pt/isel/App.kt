package pt.isel

import java.io.File
import java.io.FileOutputStream

fun main() {
    /**
     * Both writers write to the same file overwriting each other.
     * TO avoid overwriting and use exclusively, then acquire a lock on the channel
     */
    val w1 = FileOutputStream("out.txt")
        .also { it.channel.lock() }
        .bufferedWriter()
    w1.run { write("ola1"); flush() }
    /**
     * May throw Exception because the channel to the file is locked to w1.
     */
    val w2 = FileOutputStream("out.txt")
        .also { it.channel.lock() }
        .bufferedWriter()
    w2.run { write("ola2"); flush() }
    w1.run { write(" super1"); flush() }
    w2.run { write(" super2"); flush() }
}