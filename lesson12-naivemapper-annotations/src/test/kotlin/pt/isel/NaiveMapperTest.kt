package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NaiveMapperTest {
    @Test
    fun mapArtistSpotifyToArtist() {
        val source = ArtistSpotify("Muse", "UK")
        val dest:Artist = source.mapTo(Artist::class)
        assertEquals(source.name, dest.name)
        assertEquals("", dest.from)
    }
    @Test
    fun mapArtistSpotifyToArtistVersion3() {
        val mapper = NaiveMapper(ArtistSpotify::class, Artist::class)
        val source = ArtistSpotify("Muse", "UK")
        val dest:Artist = mapper.mapFrom(source)
        assertEquals(source.name, dest.name)
        assertEquals(source.country, dest.from)
    }
}