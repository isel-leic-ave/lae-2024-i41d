package pt.isel

import org.junit.jupiter.api.AfterEach
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.Writer
import java.lang.ref.Cleaner
import java.nio.channels.OverlappingFileLockException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private const val output = "out.txt"

class CloseHandles {
    private val newline: String = System.lineSeparator()

    @AfterEach
    fun cleanup() {
        File(output).delete()
    }

    @Test
    fun `check overlapping locks on channel`() {
        /**
         * Both writers write to the same file overwriting each other.
         * TO avoid overwriting and use exclusively, then acquire a lock on the channel
         */
        val w1 = FileOutputStream(output)
            .also { it.channel.lock() }
            .bufferedWriter()
        w1.run { write("ola1"); flush() }
        /**
         * Missing close()
         * May throw Exception because the channel to the file is locked to w1.
         */
        assertFailsWith<OverlappingFileLockException> {
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
        }
    }

    @Test
    fun `check overlapping locks interleaved with gc`() {
        fun writeAndFlush(text: String) {
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
                .also {
                    it.write(text)
                    it.newLine()
                    it.flush()
                }
        }
        writeAndFlush("Ola 1")
        /**
         * DO NOT do this in real code !!!!
         * Running GC identifies the FileOutputStream object as unreachable => garbage.
         * then it may run Finalization and release the lock.
         *
         */
        System.gc()
        writeAndFlush("Ola 2")
        File(output).readText().also {
            assertEquals("Ola 2$newline", it)
        }
    }

    @Test
    fun `check write and close through Closeable`() {
        fun writeAndFlush(text: String) {
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
                .also {
                    it.write(text)
                    it.newLine()
                    it.flush()
                    it.close()
                }
        }
        writeAndFlush("Ola 1")
        File(output).readText().also {
            assertEquals("Ola 1$newline", it)
        }
        writeAndFlush("Ola 2")
        File(output).readText().also {
            assertEquals("Ola 2$newline", it)
        }
    }

    @Test
    fun `check write and close through Kotlin use`() {
        fun writeAndFlush(text: String) {
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
                .use { writer ->
                    writer.write(text)
                    writer.newLine()
                    writer.flush()
                }
        }
        writeAndFlush("Ola 1")
        File(output).readText().also {
            assertEquals("Ola 1$newline", it)
        }
        writeAndFlush("Ola 2")
        File(output).readText().also {
            assertEquals("Ola 2$newline", it)
        }
    }
    @Test
    fun `check write with ExclusiveWriter within USE try with resources`() {
        fun writeAndFlush(text: String) {
            ExclusiveWriter(output)
                .use {
                    it.writer.write(text)
                    it.writer.newLine()
                    it.writer.flush()
                }
        }
        writeAndFlush("Ola 1")
        /**
         * May call close() for the Second Time
         */
        System.gc()
        File(output).readText().also {
            assertEquals("Ola 1$newline", it)
        }
        writeAndFlush("Ola 2")
        File(output).readText().also {
            assertEquals("Ola 2$newline", it)
        }
        /**
         * May call close() for the Second Time
         */
        System.gc()
    }
    @Test
    fun `check write with ExclusiveWriter MISSING close`() {
        fun writeAndFlush(text: String) {
            ExclusiveWriter(output)
                .also {
                    it.writer.write(text)
                    it.writer.newLine()
                    it.writer.flush()
                }
        }
        writeAndFlush("Ola 1")
        File(output).readText().also {
            assertEquals("Ola 1$newline", it)
        }
        /**
         * DO NOT do this in real code !!!!
         * Running GC identifies the ExclusiveWriter object as unreachable => garbage.
         * then it may run Finalization and release the lock.
         *
         */
        System.gc()

        writeAndFlush("Ola 2")
        File(output).readText().also {
            assertEquals("Ola 2$newline", it)
        }
    }
    @Test
    fun `check write with ExclusiveWriterCleanable within USE try with resources`() {
        fun writeAndFlush(text: String) {
            ExclusiveWriterCleanable(output)
                .use {
                    it.writer.write(text)
                    it.writer.newLine()
                    it.writer.flush()
                }
        }
        writeAndFlush("Ola 1")
        /**
         * May call close() for the Second Time
         */
        System.gc()
        File(output).readText().also {
            assertEquals("Ola 1$newline", it)
        }
        writeAndFlush("Ola 2")
        File(output).readText().also {
            assertEquals("Ola 2$newline", it)
        }
        /**
         * May call close() for the Second Time
         */
        System.gc()
    }
    @Test
    fun `check write with ExclusiveWriterCleanable MISSING close`() {
        fun writeAndFlush(text: String) {
            ExclusiveWriterCleanable(output)
                .also {
                    it.writer.write(text)
                    it.writer.newLine()
                    it.writer.flush()
                }
        }
        writeAndFlush("Ola 1")
        File(output).readText().also {
            assertEquals("Ola 1$newline", it)
        }
        /**
         * DO NOT do this in real code !!!!
         * Running GC identifies the ExclusiveWriter object as unreachable => garbage.
         * then it may run Finalization and release the lock.
         *
         */
        System.gc()
        Thread.sleep(100)
        writeAndFlush("Ola 2")
        File(output).readText().also {
            assertEquals("Ola 2$newline", it)
        }
    }

}

/**
 * Like a BufferedWriter but with an additional lock
 * in the internal IO channel
 */
class ExclusiveWriter(path: String) : Closeable {
    val writer = FileOutputStream(path)
        .also { it.channel.lock() }
        .bufferedWriter()

    var closeCount = 1
    override fun close() {
        println("Closing... ${closeCount++}")
        writer.close()
    }
    protected fun finalize() {
        this.close()
    }
}

/**
 * Like a BufferedWriter but with an additional lock
 * in the internal IO channel and with the cleaning action
 * implemented.
 */
class ExclusiveWriterCleanable(path: String) : Closeable {
    companion object {
        val cleaner: Cleaner = Cleaner.create()
    }

    val writer = FileOutputStream(path)
        .also { it.channel.lock() }
        .bufferedWriter()

    class CleanAction(val writer: Writer) : Runnable {
        var closeCount = 1
        override fun run() {
            println("Closing... ${closeCount++}")
            writer.close()
        }
    }
    private val cleanAction = CleanAction(writer)
    private val cleanable = cleaner.register(this, cleanAction)

    override fun close() {
        // <=> cleanAction.run() + Unregisters the cleanable
        cleanable.clean()
    }
}

