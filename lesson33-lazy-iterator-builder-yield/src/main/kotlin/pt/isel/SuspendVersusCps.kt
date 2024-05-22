package pt.isel

import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED

private val httpClient: HttpClient = HttpClient.newHttpClient()
private val requestBuilder = HttpRequest.newBuilder()
private fun request(url: String) = requestBuilder.uri(URI.create(url)).build()

fun main() {
    val fetchCpsHandle = ::fetchSuspend as ((String, Continuation<String>) -> Any)
    fetchCpsHandle("https://github.com", object : Continuation<String> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<String>) {
            val body = result.getOrThrow()
            println(body
                .split("<title>")[1]
                .split("</title>")[0])
        }
    })
    Thread.sleep(3000)

    val fetchSuspendHandle = ::fetchCps as (suspend (String) -> String)
    runBlocking {
        val body = fetchSuspendHandle("https://stackoverflow.com/")
        println(body
            .split("<title>")[1]
            .split("</title>")[0])
    }
}

suspend fun test() {
    fetchSuspend("https://github.com")
    println()
}


suspend fun fetchSuspend(url: String): String {
    println("Fetching $url")
    return httpClient
        .sendAsync(request(url), HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse<String>::body)
        .await()
}

fun fetchCps(url: String, cont: Continuation<String>) : Any {
    println("Fetching $url")
//    httpClient
//        .sendAsync(request(url), HttpResponse.BodyHandlers.ofString())
//        .thenApply(HttpResponse<String>::body)
//        .whenComplete { body, err ->
//            if (err != null) cont.resumeWithException(err)
//            else cont.resume(body)
//        }
    val body = URI(url).toURL().readText()
    cont.resume(body)

    return COROUTINE_SUSPENDED
}
