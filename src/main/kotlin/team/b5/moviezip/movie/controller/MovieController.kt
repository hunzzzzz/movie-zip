package team.b5.moviezip.movie.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.b5.moviezip.global.security.MemberPrincipal
import team.b5.moviezip.movie.dto.MovieSearchResult
import team.b5.moviezip.movie.dto.response.MovieResponse
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.model.MovieAgeLimit
import team.b5.moviezip.movie.model.MovieNation
import team.b5.moviezip.movie.model.MovieStatus
import team.b5.moviezip.movie.service.MovieService

@RestController
@RequestMapping("/movies")
class MovieController(
    private val movieService: MovieService
) {
    // 영화 단건 조회
    @Operation(summary = "영화 단건 조회")
    @GetMapping("/api/v1/{movieId}")
    fun getMovies(@PathVariable movieId: Long): ResponseEntity<MovieResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovies(movieId))
    }

    // 좋아요
    @Operation(summary = "영화 좋아요")
    @GetMapping("/{movieId}/like")
    fun like(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @PathVariable movieId: Long
    ) = ResponseEntity.ok().body(movieService.like(memberPrincipal, movieId))

    // 싫어요
    @Operation(summary = "영화 싫어요")
    @GetMapping("/{movieId}/dislike")
    fun dislike(
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @PathVariable movieId: Long
    ) = ResponseEntity.ok().body(movieService.dislike(memberPrincipal, movieId))

    /*
        검색 기능 V1 (QueryDSL 사용)
     */
    @Operation(summary = "관객순위 영화 목록 조회")
    @GetMapping("/api/v1/top-audience")
    fun getTopAudiences(): ResponseEntity<List<Movie>> {
        val topAudiences = movieService.getTopAudience()
        return ResponseEntity.status(HttpStatus.OK).body(topAudiences)
    }

    @Operation(summary = "검색순위 영화 목록 조회")
    @GetMapping("/api/v1/top-search")
    fun getTopSearch(): ResponseEntity<List<MovieSearchResult>> {
        val topSearch: List<MovieSearchResult> = movieService.getTopSearch()
        return ResponseEntity.status(HttpStatus.OK).body(topSearch)
    }

    // 단순 영화 검색
    @Operation(summary = "QueryDSL 영화 검색 (v1)", description = "입력한 글자가 포함된 영화 검색")
    @GetMapping("/api/v1/search")
    fun searchMovies(
        @Parameter(description = "영화 제목")
        @RequestParam thing: String,

        @RequestParam(required = false) status: MovieStatus?,
        @RequestParam(required = false) nation: MovieNation?,
        @RequestParam(required = false) ageLimit: MovieAgeLimit?,

        @Parameter(description = "페이지")
        @RequestParam(value = "page", defaultValue = "1") page: Int,

        @Parameter(description = "정렬 기준 (audience (관객 수), like (좋아요 수), ... etc)")
        @RequestParam(value = "sort", defaultValue = "audience") sort: String
    ) =
        PageRequest.of(page - 1, 5, Sort.by(Sort.Order(Sort.Direction.DESC, sort)))
            .let { ResponseEntity.ok().body(movieService.searchMovies(thing, status, nation, ageLimit, it)) }

    /*
        검색 기능 V2 (Redis 사용)
     */
    @Operation(summary = "QueryDSL 영화 검색 (v2, with REDIS)", description = "입력한 글자가 포함된 영화 검색")
    @GetMapping("/api/v2/search")
//    @Cacheable(value = ["movies"], cacheManager = "redisCacheManager")
    fun searchMoviesByRedis(
        @Parameter(description = "영화 제목")
        @RequestParam thing: String,

        @RequestParam(required = false) status: MovieStatus?,
        @RequestParam(required = false) nation: MovieNation?,
        @RequestParam(required = false) ageLimit: MovieAgeLimit?,

        @Parameter(description = "페이지")
        @RequestParam(value = "page", defaultValue = "1") page: Int,

        @Parameter(description = "정렬 기준 (audience (관객 수), like (좋아요 수), ... etc)")
        @RequestParam(value = "sort", defaultValue = "audience") sort: String
    ): ResponseEntity<Page<MovieResponse>> {
        val pageable: Pageable = PageRequest
            .of(page - 1, 5, Sort.by(Sort.Order(Sort.Direction.DESC, sort)))
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(movieService.searchMoviesByRedis(thing, status, nation, ageLimit, pageable))
    }

    @Operation(summary = "Redis 사용한 단일 영화 조회")
    @GetMapping("/api/v2/{movieId}")
    fun getMoviesByRedis(@PathVariable movieId: Long): ResponseEntity<MovieResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getMoviesByRedis(movieId))
    }


}