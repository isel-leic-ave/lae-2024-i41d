fun main() {
    var f: (String, Char) -> Int = { s, c -> (s+c).length }
    println(f("isel", 'f'))
}