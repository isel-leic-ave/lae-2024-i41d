package pt.isel

class Artist(
    val name: String,
    val from: State = State("", ""),
    val tracks: List<Track> = emptyList()
)