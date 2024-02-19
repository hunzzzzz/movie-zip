package team.b5.moviezip.movie.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.model.MovieStatus

interface CustomMovieRepository {
    fun searchMoviesByName(name: String, status: MovieStatus?, pageable: Pageable): Page<Movie>
}