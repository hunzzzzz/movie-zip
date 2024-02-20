package team.b5.moviezip.global.service

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class DataInitController(
    private val dateInitService: DataInitService
) {
    // 영화 데이터 추가
    @Operation(summary = "영화 데이터 추가")
    @GetMapping("/movies")
    fun addMovies() = ResponseEntity.ok().body(dateInitService.addMovies())

    // 영화 장르 추가
    @Operation(summary = "영화 장르 추가")
    @GetMapping("/genre")
    fun addGenre() = ResponseEntity.ok().body(dateInitService.addGenre())
}