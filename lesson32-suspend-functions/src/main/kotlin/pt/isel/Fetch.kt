package pt.isel

import java.net.URI

fun fetch(path: String): String {
    println("Fetching $path")
    return URI(path).toURL().readText()
}

fun fetchMany(url1: String, url2: String, url3: String): List<String> {
    val bodies = mutableListOf<String>()
    bodies.add(fetch(url1))
    bodies.add(fetch(url2))
    bodies.add(fetch(url3))
    return bodies
}
