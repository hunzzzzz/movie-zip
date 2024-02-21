package team.b5.moviezip.movie.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.exception.case.DuplicatedLikeException
import team.b5.moviezip.global.exception.case.ModelNotFoundException
import team.b5.moviezip.global.security.MemberPrincipal
import team.b5.moviezip.keyword.service.KeywordService
import team.b5.moviezip.member.repository.MemberRepository
import team.b5.moviezip.movie.dto.MovieSearchResult
import team.b5.moviezip.movie.dto.response.MovieResponse
import team.b5.moviezip.movie.dto.toMovieSearchResultList
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.model.MovieNation
import team.b5.moviezip.movie.model.MovieStatus
import team.b5.moviezip.movie.repository.MovieRepository
import team.b5.moviezip.movie.repository.MovieSpecifications
import team.b5.moviezip.review.dto.ReviewResponse
import team.b5.moviezip.review.model.ReviewStatus
import team.b5.moviezip.review.repository.ReviewRepository

@Service
@Transactional
class MovieService(
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository,
    private val keywordService: KeywordService
) {
    // 영화 단건 조회
    fun getMovies(movieId: Long) =
        MovieResponse.from(getMovie(movieId), getAllReviews(movieId))

    fun searchMovies(name: String?, nation: String?, distributor: String?, pageable: Pageable): Page<Movie> {
        val specification = MovieSpecifications.searchMovies(name, nation, distributor)
        val movies: Page<Movie>

        if (name.isNullOrBlank() && nation.isNullOrBlank() && distributor.isNullOrBlank()) {
            movies = movieRepository.findAll(pageable)
        } else {
            movies = movieRepository.findAll(specification, pageable)
            increaseSearchCount(movies.content)
        }

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

    // 영화 검색 (페이징 적용)
    fun searchMovies(
        thing: String,
        status: MovieStatus?,
        nation: MovieNation?,
        pageable: Pageable
    ) = movieRepository.searchMovies(thing, status, nation, pageable)
        .map { MovieResponse.from(it, getAllReviews(it.id!!)) }

    // 영화 검색 (페이징 적용+ 레디스 캐싱)
    @Cacheable(value = ["movies"], cacheManager = "redisCacheManager")
    fun searchMoviesByRedis(
        thing: String,
        status: MovieStatus?,
        nation: MovieNation?,
        pageable: Pageable
    ):Page<MovieResponse>{
        keywordService.countKeywords(thing)

        return movieRepository.searchMovies(thing, status, nation, pageable)
            .map { MovieResponse.from(it, getAllReviews(it.id!!)) }
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

    // Movie 하위의 모든 Review 목록을 출력
    private fun getAllReviews(movieId: Long) =
        reviewRepository.findAllByMovieIdAndStatus(movieId, ReviewStatus.NORMAL).map { ReviewResponse.from(it) }

    // 멤버 조회
    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("회원")

    // 영화 조회 (id)
    private fun getMovie(movieId: Long) =
        movieRepository.findByIdOrNull(movieId) ?: throw ModelNotFoundException("영화")
}