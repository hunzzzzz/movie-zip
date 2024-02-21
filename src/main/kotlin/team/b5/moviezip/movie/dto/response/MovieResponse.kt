package team.b5.moviezip.movie.dto.response

import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.review.dto.ReviewResponse
import java.io.Serializable
import java.text.DecimalFormat
import java.time.LocalDate

data class MovieResponse(
    val name: String,
    val releaseAt: LocalDate,
    val audience: String,
    val ratings: String,
    val nation: String,
    val distributor: String,
    val director: String,
    val status: String,
    val like: String,
    val dislike: String,
    val genre: String,
    val actors: String,
) : Serializable {
    companion object {
        fun from(movie: Movie) = MovieResponse(
            name = movie.name,
            releaseAt = LocalDate.of(movie.releaseAt.year, movie.releaseAt.month, movie.releaseAt.dayOfMonth),
            audience = "${DecimalFormat("#,###").format(movie.audience)}명",
            ratings = "${movie.ratings}★",
            nation = movie.nation.name,
            distributor = movie.distributor,
            director = movie.director,
            status = movie.status.name,
            like = "${movie.like.size}👍",
            dislike = "${movie.dislike.size}👎",
            genre = movie.genre.replace(",", ", "),
            actors = movie.actor.replace(",", ", ")
        )
    }
}