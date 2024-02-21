package team.b5.moviezip.movie.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
    @Operation(summary = "영화 단건 조회")
    @GetMapping("/{movieId}")
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
    @Operation(summary = "QueryDSL 영화 검색", description = "입력한 글자가 포함된 영화 검색")
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
    @Operation(summary = "QueryDSL 영화 검색 with REDIS", description = "입력한 글자가 포함된 영화 검색")
    @GetMapping("/api/v2/search")
//    @Cacheable(value = ["movies"], cacheManager = "redisCacheManager")
    fun searchMoviesByRedis(
        @Parameter(description = "제목")
        @RequestParam thing: String,
        @RequestParam(required = false) status: MovieStatus?,
        @RequestParam(required = false) nation: MovieNation?,
        @Parameter(description = "페이지")
        @RequestParam(value = "page", defaultValue = "1") page: Int,
        @Parameter(description = "페이지당 조회 자료 수")
        @RequestParam(value = "size", defaultValue = "5") size: Int,
        @Parameter(description = "정렬 기준 id, audience, name ... etc")
        @RequestParam(value = "sort", defaultValue = "audience") sort: String,
//        @Parameter(description = "오름/내림차순 asc/desc")
//        @RequestParam(value = "order", defaultValue = "desc") order: String,
    ):ResponseEntity<Page<MovieResponse>> {
        val pageable: Pageable= PageRequest
            .of(page-1, size, Sort.by(Sort.Order(Sort.Direction.DESC, sort)))
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(movieService.searchMoviesByRedis(thing, status, nation, pageable))
    }
}