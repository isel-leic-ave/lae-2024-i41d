object B {
    fun foo() {
        println("I am B")
    }
}
fun main() {
    // B() // Not possible because B is a singleton 
    B.foo()
}