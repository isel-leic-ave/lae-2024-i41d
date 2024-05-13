package pt.isel

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@BenchmarkMode(Mode.Throughput)
@org.openjdk.jmh.annotations.State(Scope.Benchmark)
open class MappperBenchmark {
    val from = ArtistSpotify("Muse", Country("UK", "en"), listOf(
        Song("Uprising", 2011),
        Song("Hysteria", 2003)
    ))
    val naiveMaper = NaiveMapper
        .mapper(ArtistSpotify::class, Artist::class)

    @Benchmark
    fun benchNaiveMapper(hole: Blackhole) {
        hole.consume(naiveMaper.mapFrom(from))
    }

    @Benchmark
    fun benchBaselineMapper(hole: Blackhole) {
        hole.consume(mapArtistSpotity2Artist(from))
    }
}