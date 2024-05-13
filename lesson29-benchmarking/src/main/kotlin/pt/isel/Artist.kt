package pt.isel

class Artist @JvmOverloads constructor (
    val name: String,
    val from: State = State("", ""),
    val tracks: List<Track> = emptyList()
)