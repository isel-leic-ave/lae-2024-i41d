package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ComparerTest {
    @Test fun testComparerOrder() {
        val s1 = Student(12000, "Ana", "pt")
        val s2 = Student(14000, "Ana", "pt")
        val s3 = Student(11000, "Ana", "en")
        val cmp = ComparerOrder(Student::class)
        assertTrue { cmp.compare(s1, s2) < 0 } // same nationality and 12000 is < 14000
        assertTrue { cmp.compare(s2, s3) > 0 } // “pt” is > “en”
    }

    @Test fun testComparer() {
        val p1 = Person(11000, "Ana", Address("Rua Amarela", 24), Account("FD3R", 9900))
        val p2 = Person(11000, "Ana", Address("Rua Rosa", 24), Account("8YH5", 9900))
        val p3 = Person(11000, "Ana", Address("Rua Rosa", 24), Account("JK2E", 100))
        val p4 = Person(11000, "Ana", Address("Rua Rosa", 97), Account("BFR5", 100))
        val p5 = Person(17000, "Ana", Address("Rua Rosa", 97), Account("BFR5", 100))
        val cmp = Comparer<Person>(Person::class)

        assertTrue { cmp.compare(p1, p2) < 0 } // Rua Amarela is < Rua Rosa
        assertTrue { cmp.compare(p2, p3) > 0 } // 9900 is > 100
        assertEquals(0, cmp.compare(p3, p4))   // All properties are equal
        assertTrue { cmp.compare(p4, p5) < 0 } // 11000 is < 17000
    }
}