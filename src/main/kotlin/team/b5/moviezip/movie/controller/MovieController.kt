package team.b5.moviezip.movie.controller

import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.movie.service.MovieService

@RestController
class MovieController(
    private val movieService: MovieService
) {
}