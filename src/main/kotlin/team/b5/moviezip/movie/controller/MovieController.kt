package team.b5.moviezip.movie.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.global.security.MemberPrincipal
import team.b5.moviezip.movie.dto.MovieSearchResult
import team.b5.moviezip.movie.dto.response.MovieResponse
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.model.MovieNation
import team.b5.moviezip.movie.model.MovieStatus
import team.b5.moviezip.movie.service.MovieService

@RestController
@RequestMapping("/movies")
class MovieController(
    private val movieService: MovieService
) {
    // 영화 단건 조회
    @GetMapping("/{movieId}")
    fun getMovies(@PathVariable movieId: Long): ResponseEntity<MovieResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovies(movieId))
    }

    // 좋아요
    @GetMapping("/{movieId}/like")
    fun like(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @PathVariable movieId: Long
    ) = ResponseEntity.ok().body(movieService.like(memberPrincipal, movieId))

    // 싫어요
    @GetMapping("/{movieId}/dislike")
    fun dislike(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @PathVariable movieId: Long
    ) = ResponseEntity.ok().body(movieService.dislike(memberPrincipal, movieId))

    /*
        검색 기능 V1 (QueryDSL 사용)
     */
//    @GetMapping("/api/v1/search")
//    fun searchMovies(
//        @RequestParam(required = false) name: String?,
//        @RequestParam(required = false) nation: String?,
//        @RequestParam(required = false) distributor: String?,
//        pageable: Pageable
//    ): ResponseEntity<Page<Movie>> {
//        val moviesPage: Page<Movie> = movieService.searchMovies(name, nation, distributor, pageable)
//        return ResponseEntity.status(HttpStatus.OK).body(moviesPage)
//    }

    @GetMapping("/api/v1/top-audience")
    fun getTopAudiences(): ResponseEntity<List<Movie>> {
        val topAudiences = movieService.getTopAudience()
        return ResponseEntity.status(HttpStatus.OK).body(topAudiences)
    }

    @GetMapping("/api/v1/top-search")
    fun getTopSearch(): ResponseEntity<List<MovieSearchResult>> {
        val topSearch: List<MovieSearchResult> = movieService.getTopSearch()
        return ResponseEntity.status(HttpStatus.OK).body(topSearch)
    }

    // 영화 검색
    @GetMapping("/api/v1/search")
    fun searchMovies(
        @RequestParam thing: String,
        @RequestParam(required = false) status: MovieStatus?,
        @RequestParam(required = false) nation: MovieNation?,
        @PageableDefault(page = 0, size = 10, sort = ["audience"]) pageable: Pageable
    ) =
        ResponseEntity.ok().body(movieService.searchMovies(thing, status, nation, pageable))

    /*
        검색 기능 V2 (Redis 사용)
     */
}