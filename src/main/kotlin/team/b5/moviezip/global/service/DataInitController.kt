package team.b5.moviezip.global.service

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class DataInitController(
    private val dateInitService: DataInitService
) {
    // 영화 데이터 추가 (더미 데이터, 미개봉 영화 43578건)
    @Operation(summary = "영화 더미 데이터 추가 (미개봉 영화)")
    @PostMapping("/dummy-movies/latest")
    fun addLatestMovies() =
        ResponseEntity.ok().body(dateInitService.addDummyMovies("latest"))

    // 영화 데이터 추가 (더미 데이터, 고전 영화 43578건)
    @Operation(summary = "영화 더미 데이터 추가 (고전 영화)")
    @PostMapping("/dummy-movies/old")
    fun addMovie() =
        ResponseEntity.ok().body(dateInitService.addDummyMovies("old"))

    // 영화 데이터 추가 (CSV 파일)
    @Operation(summary = "영화 데이터 추가")
    @GetMapping("/movies")
    fun addMovies() = ResponseEntity.ok().body(dateInitService.addMovies())
}