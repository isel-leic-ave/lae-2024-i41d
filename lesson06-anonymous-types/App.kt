fun main() {
    KA(7).makeB().foo();
}

class KA(val nr: Int) {

    fun makeB() : B {
        return object : B() {
            override fun foo() {
                println("Foo from B on KOtlin KA class with nr $nr")
            }
        }
    }
}