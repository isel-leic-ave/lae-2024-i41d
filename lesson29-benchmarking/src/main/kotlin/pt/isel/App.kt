package pt.isel

import java.lang.System.currentTimeMillis

fun mapArtistSpotity2Artist(from: ArtistSpotify): Artist {
    return Artist(
        from.name,
        State(from.country.name, from.country.idiom),
        from.songs.map { Track(it.name, it.year) }
    )

}

fun main() {
    val from = ArtistSpotify("Muse", Country("UK", "en"), listOf(
        Song("Uprising", 2011),
        Song("Hysteria", 2003)
    ))
    val naiveMaper = NaiveMapper
            .mapper(ArtistSpotify::class, Artist::class)

    repeat(10) { iter ->
        val thr = testPerformanceMapFrom { naiveMaper.mapFrom(from) }
        println("Throughput NaiveMapper $iter: $thr ops/ms")
    }
    repeat(10) { iter ->
        val thr = testPerformanceMapFrom { mapArtistSpotity2Artist(from) }
        println("Baseline mapArtistSpotity2Artist $iter: $thr ops/ms")
    }

}

/**
 * DO NOT USE this approach.
 * Wrong benchmarking methodology!!!
 * Problems:
 * 1. Mixing Domain <> mapFrom()
 * 2. Overhead of NaiveMapper initialization
 * 3. Overhead of IO
 * 4. System call to currentTimeMillis() also induces overhead
 * 5. Jitter overhead => require warmup
 * 6. Missing VM optimizations => require warmup
 * 7. Results variance
 */
fun testPerformanceNaiveMapperReflect() {
    val init = currentTimeMillis()
    val muse = NaiveMapper
        .mapper(ArtistSpotify::class, Artist::class)
        .mapFrom(ArtistSpotify("Muse", Country("UK", "en"), listOf(
            Song("Uprising", 2011),
            Song("Hysteria", 2003)
        )))
    println(muse)
    val dur = currentTimeMillis() - init
    println("NaiveMapper take $dur ms")
}

const val warmupCount = 1000
const val runCount = 100000

/**
 * Returns the throughput in ops/ms
 */
fun testPerformanceMapFrom(op: () -> Unit) : Long {
    // println("#### Warmup")
    repeat(warmupCount) { op() }
    // println("############")
    val init = currentTimeMillis()
    repeat(runCount) { op() }
    val dur = currentTimeMillis() - init
    // println("NaiveMapper take ${dur/1000} us/op")
    return runCount/dur
}