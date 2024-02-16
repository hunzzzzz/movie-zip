package team.b5.moviezip.movie.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.b5.moviezip.movie.dto.MovieResponse
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.service.MovieService


@RestController
@RequestMapping("/Movies")
class MovieController(
    private val movieService: MovieService
) {

    @GetMapping()
    fun getAllMovies(pageable: Pageable):ResponseEntity<Page<Movie>>{
        val moviesPage: Page<Movie> = movieService.getAllMovies(pageable)
        return ResponseEntity.status(HttpStatus.OK).body(moviesPage)
    }

    @GetMapping("/{movieId}")
    fun getMovie(@PathVariable movieId:Long):ResponseEntity<MovieResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovie(movieId))
    }

    @GetMapping("/search")
    fun searchMovies(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) nation: String?,
        @RequestParam(required = false) distributor: String?,
        pageable: Pageable
    ): ResponseEntity<Page<Movie>> {
        val moviesPage:Page<Movie> = movieService.searchMovies(name, nation, distributor, pageable)
        return ResponseEntity.status(HttpStatus.OK).body(moviesPage)
    }

    @GetMapping("/top-audience")
    fun getTopAudiences(): ResponseEntity<List<Movie>> {
        val topAudiences = movieService.getTopAudience()
        return ResponseEntity.status(HttpStatus.OK).body(topAudiences)
    }

    @GetMapping("/top-search")
    fun getTopSearch(): ResponseEntity<List<Movie>>{
        val topSearch:List<Movie> = movieService.getTopSearch()
        return ResponseEntity.status(HttpStatus.OK).body(topSearch)
    }

/*    @GetMapping("/add-movies")
    fun addMovies() = ResponseEntity.ok().body(movieService.addMovies())*/
}