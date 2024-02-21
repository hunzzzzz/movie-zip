package team.b5.moviezip.global.service

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.b5.moviezip.movie.dto.request.AddMovieRequest

@RestController
@RequestMapping("/admin")
class DataInitController(
    private val dateInitService: DataInitService
) {
    // 영화 데이터 추가 (더미 데이터)
    @Operation(summary = "영화 더미 데이터 추가")
    @PostMapping("/dummy-movies")
    fun addMovie(@RequestBody addMovieRequest: AddMovieRequest) =
        ResponseEntity.ok().body(dateInitService.addMovies(addMovieRequest))

    // 영화 데이터 추가 (CSV 파일)
    @Operation(summary = "영화 데이터 추가")
    @GetMapping("/movies")
    fun addMovies() = ResponseEntity.ok().body(dateInitService.addMovies())
}