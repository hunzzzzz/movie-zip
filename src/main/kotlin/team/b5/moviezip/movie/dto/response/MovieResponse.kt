package team.b5.moviezip.movie.dto.response

import team.b5.moviezip.member.model.Member
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.review.model.Review
import java.time.ZonedDateTime

data class MovieResponse(
    val name: String,
    val description: String,
    val releaseAt: ZonedDateTime,
    val audience: Long,
    val ratings: Double,
    val nation: String,
    val distributor: String,
    val director: String,
    val status: String,
    val reviews: List<Review>,
    val like: Int,
    val dislike: Int
) {
    companion object {
        fun from(movie: Movie, like: MutableSet<Member>, dislike: MutableSet<Member>) = MovieResponse(
            name = movie.name,
            description = movie.description,
            releaseAt = movie.releaseAt,
            audience = movie.audience,
            ratings = movie.ratings,
            nation = movie.nation.name,
            distributor = movie.distributor,
            director = movie.director,
            status = movie.status.name,
            reviews = listOf(), // TODO
            like = like.size,
            dislike = dislike.size
        )
    }
}