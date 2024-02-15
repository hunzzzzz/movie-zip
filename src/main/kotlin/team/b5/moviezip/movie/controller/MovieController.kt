package team.b5.moviezip.movie.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.movie.service.MovieService

@RestController
@RequestMapping("/movies")
class MovieController(
    private val movieService: MovieService
) {
    // 영화 데이터 추가
    @GetMapping
    fun addMovies() = ResponseEntity.ok().body(movieService.addMovies())
}