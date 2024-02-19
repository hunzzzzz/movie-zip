package team.b5.moviezip.movie.service


import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.exception.case.DuplicatedLikeException
import team.b5.moviezip.global.exception.case.ModelNotFoundException
import team.b5.moviezip.global.security.MemberPrincipal
import team.b5.moviezip.member.repository.MemberRepository
import team.b5.moviezip.movie.repository.MovieRepository
import team.b5.moviezip.movie.dto.MovieResponse
import team.b5.moviezip.movie.dto.MovieSearchResult
import team.b5.moviezip.movie.dto.toMovieSearchResultList
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.repository.MovieSpecifications
import kotlin.math.min

@Service
@Transactional
class MovieService(
    private val movieRepository: MovieRepository,
    private val memberRepository: MemberRepository
) {
    // 영화 전체 조회
    fun getAllMovies(page: Int) =
        PageRequest.of(page, 10).let {
            movieRepository.findAll(it).map { movie -> MovieResponse.from(movie) }
        }

//    Page<Movie>
//    {
//        val pageable = PageRequest.of(page, 10)
//        val start = pageable.offset.toInt()
//        val end = min(start + pageable.pageSize, filteredList.size)
//        return PageImpl()
//    }

    // 영화 단건 조회
    fun getMovies(movieId: Long) = MovieResponse.from(getMovie(movieId))

    fun searchMovies(name: String?, nation: String?, distributor: String?, pageable: Pageable): Page<Movie> {
        val specification = MovieSpecifications.searchMovies(name, nation, distributor)
        val movies: Page<Movie>

        if (name.isNullOrBlank() && nation.isNullOrBlank() && distributor.isNullOrBlank()) {
            movies = movieRepository.findAll(pageable)
        } else {
            movies = movieRepository.findAll(specification, pageable)
        }

        increaseSearchCount(movies.content)
        return movies
    }

    private fun increaseSearchCount(movies: List<Movie>) {
        movies.forEach { movie ->
            movie.searchCount = (movie.searchCount ?: 0) + 1
        }
        movieRepository.saveAll(movies)
    }

    fun getTopAudience(): List<Movie> {
        val topAudiences = movieRepository.findTop20MoviesByAudience()
        return topAudiences.take(20)
    }

    fun getTopSearch(): List<MovieSearchResult> {
        val topSearch = movieRepository.findTop10BySearchCountGreaterThanOrderBySearchCountDesc()
        return topSearch.take(10).toMovieSearchResultList()
    }

    // 좋아요
    fun like(memberPrincipal: MemberPrincipal, movieId: Long) =
        validateLikeOrDislike(memberPrincipal.id, movieId, "like")
            .run { getMovie(movieId).like(getMember(memberPrincipal.id)) }

    // 싫어요
    fun dislike(memberPrincipal: MemberPrincipal, movieId: Long) =
        validateLikeOrDislike(memberPrincipal.id, movieId, "dislike")
            .run { getMovie(movieId).dislike(getMember(memberPrincipal.id)) }


    // 좋아요, 싫어요 중복 검증
    private fun validateLikeOrDislike(memberId: Long, movieId: Long, target: String) {
        when (target) {
            "like" -> {
                if (getMovie(movieId).like.contains(getMember(memberId)))
                    throw DuplicatedLikeException("좋아요")
            }

            "dislike" -> {
                if (getMovie(movieId).dislike.contains(getMember(memberId)))
                    throw DuplicatedLikeException("싫어요")
            }
        }
    }

    // 멤버 조회
    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("회원")

    // 영화 조회 (id)
    private fun getMovie(movieId: Long) =
        movieRepository.findByIdOrNull(movieId) ?: throw ModelNotFoundException("영화")

    // 영화 조회 (name)
    private fun getMovie(name: String) =
        movieRepository.findByName(name) ?: throw ModelNotFoundException("영화")
}