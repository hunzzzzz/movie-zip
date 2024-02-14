package team.b5.moviezip.movie.service

import org.springframework.stereotype.Service
import team.b5.moviezip.movie.repository.MovieRepository

@Service
class MovieService(
    private val movieRepository: MovieRepository
) {
}