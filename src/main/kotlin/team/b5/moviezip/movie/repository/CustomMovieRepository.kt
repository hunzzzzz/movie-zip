package team.b5.moviezip.movie.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.model.MovieAgeLimit
import team.b5.moviezip.movie.model.MovieNation
import team.b5.moviezip.movie.model.MovieStatus

interface CustomMovieRepository {
    fun searchMovies(
        thing: String,
        status: MovieStatus?,
        nation: MovieNation?,
        ageLimit: MovieAgeLimit?,
        pageable: Pageable
    ): Page<Movie>
}