package team.b5.moviezip.movie.dto.response

import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.review.dto.ReviewResponse
import java.time.ZonedDateTime

data class MovieResponse(
    val name: String,
    val description: String?,
    val releaseAt: ZonedDateTime,
    val audience: Long,
    val ratings: Double,
    val nation: String,
    val distributor: String,
    val director: String?,
    val status: String,
    val reviews: List<ReviewResponse>,
    val like: Int,
    val dislike: Int,
    val genre: String
) {
    companion object {
        fun from(movie: Movie, reviews: List<ReviewResponse>) = MovieResponse(
            name = movie.name,
            description = movie.description,
            releaseAt = movie.releaseAt,
            audience = movie.audience,
            ratings = movie.ratings,
            nation = movie.nation.name,
            distributor = movie.distributor,
            director = movie.director,
            status = movie.status.name,
            reviews = reviews,
            like = movie.like.size,
            dislike = movie.dislike.size,
            genre = movie.genre.joinToString(", ")
        )
    }
}