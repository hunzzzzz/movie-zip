package team.b5.moviezip.movie.dto.request

data class AddMovieRequest(
    val name: String,

    val releaseAt: String,

    val sales: String,

    val audience: String,

    var screens: String,

    val nation: String,

    val distributor: String,

    val ageLimit: String,

    val genre: String,

    val director: String,

    val actor: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "", "", "")

    fun toArray() = arrayOf(
        name, releaseAt, sales, audience, screens, nation,
        distributor, ageLimit, genre, director, actor
    )
}