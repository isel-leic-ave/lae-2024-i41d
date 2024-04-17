package pt.isel

import org.junit.jupiter.api.Test
import java.io.FileOutputStream
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class MapperArtistSpotify2Artist : Mapper<Artist> {
    override fun mapFrom(src: Any): Artist {
        // 0. cast receiver to srcType
        val srcArtist = src as ArtistSpotify

        // 1. Read properties from src
        val name = src.name
        val country = src.country

        // 2. Instantiate Artist with the values of former properties
        return Artist(name, State(country.name, country.idiom))
    }
}

class NaiveMapperTest {
    @Test
    fun mapArtistSpotifyToArtistDynamic() {
        // val cm = builDynamicMapper(ArtistSpotify::class, Artist::class)
        // cm.finishTo(FileOutputStream(cm.name() + ".class"))
        val mapper: Mapper<Artist> = dynamicMapper(ArtistSpotify::class, Artist::class)
        val source = ArtistSpotify("Muse", Country("UK", "English"), listOf(
            Song("Resistance", 2010),
            Song("Hysteria", 2003)
        ))
        val dest = mapper.mapFrom(source)
        assertNull(dest)
    }

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