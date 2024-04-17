package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class NaiveMapperTest {
    @Test
    fun mapArtistSpotifyToArtistVersion3() {
        val mapper = NaiveMapper.mapper(ArtistSpotify::class, Artist::class)
        val source = ArtistSpotify("Muse", Country("UK", "English"), listOf(
            Song("Resistance", 2010),
            Song("Hysteria", 2003)
        ))
        val dest:Artist = mapper.mapFrom(source)
        assertEquals(source.name, dest.name)
        assertEquals(source.country.name, dest.from.name)
        assertEquals(source.country.idiom, dest.from.idiom)
        val tracks = dest.tracks.iterator()
        source.songs.forEach {
            val actual = tracks.next()
            assertEquals(it.name, actual.name)
            assertEquals(it.year, actual.year)
        }
        assertFalse { tracks.hasNext() }

    }
}