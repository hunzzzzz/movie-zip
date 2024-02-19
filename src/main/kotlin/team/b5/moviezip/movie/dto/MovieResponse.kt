package team.b5.moviezip.movie.dto

import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.model.MovieNation
import java.time.ZonedDateTime

data class MovieResponse(
    val id: Long,
    val name: String,
    val description: String?,
//    var moviePicUrl: MutableList<String>,
    val releaseAt: ZonedDateTime?,
    val audience: Long?,
    val ratings: Double?,
    val director: String?,
    val distributor: String?,
    val sales:String?,
    val screen:Int?,
    val nation:MovieNation?,
    val like: Int,
    val dislike: Int,
) {
    companion object {
        fun from(movie: Movie): MovieResponse {
            return MovieResponse(
                id = movie.id!!,
                name = movie.name,
//                moviePicUrl = movie.moviePicUrl,
                description = movie.description,
                releaseAt = movie.releaseAt,
                audience = movie.audience,
                ratings = movie.ratings,
                distributor = movie.distributor,
                director = movie.director,
                sales = movie.sales,
                screen = movie.screens,
                nation = movie.nation,
                like = movie.like.size,
                dislike = movie.dislike.size,

            )
        }
    }
}

