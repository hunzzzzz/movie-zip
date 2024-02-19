package team.b5.moviezip.movie.dto

import team.b5.moviezip.movie.model.Movie

data class MovieSearchResult(
    val name: String,
    val searchCount: Long
)

fun Movie.toMovieSearchResult(): MovieSearchResult {
    return MovieSearchResult(
        name = this.name,
        searchCount = this.searchCount ?: 0
    )
}

fun List<Movie>.toMovieSearchResultList(): List<MovieSearchResult> {
    return this.map { it.toMovieSearchResult() }
}
