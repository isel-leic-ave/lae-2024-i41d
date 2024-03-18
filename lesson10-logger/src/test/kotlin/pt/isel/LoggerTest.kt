package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LoggerTest {
    @Test fun logStudent() {
        val expected = """
            Type of Object is Student
              - from: Portugal
              - name: Maria
              - nr: 749395
        """.trimIndent()
        val s = Student("Maria", 749395, "Portugal")
        // System.out.log(s)
        val actual = StringBuilder().also { out -> out.log(s) }
        assertEquals(expected, actual.toString().trimIndent())
    }

    @Test fun logRectanle() {
        val expected = """
            Type of Object is Rectangle
              - area: 20
              - height: 5
              - width: 4
        """.trimIndent()
        val r = Rectangle(4,5)
        val actual = StringBuilder().also { out -> out.log(r) }
        assertEquals(expected, actual.toString().trimIndent())
    }
    @Test fun logRectJava() {
        val expected = """
            Type of Object is RectJava
              - width: 4
              - height: 5
        """.trimIndent()
        val r = RectJava(4,5)
        val actual = StringBuilder().also { out -> out.log(r) }
        assertEquals(expected, actual.toString().trimIndent())
    }
    @Test fun logRectJavaGetters() {
        val expected = """
            Type of Object is RectJava
              - Width: 4
              - Height: 5
              - Area: 20
        """.trimIndent()
        val r = RectJava(4,5)
        val actual = StringBuilder().also { out -> out.logGetters(r) }
        assertEquals(expected, actual.toString().trimIndent())
    }

}