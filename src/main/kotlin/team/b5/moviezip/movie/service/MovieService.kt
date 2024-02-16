package team.b5.moviezip.movie.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.exception.case.DuplicatedLikeException
import team.b5.moviezip.global.exception.case.ModelNotFoundException
import team.b5.moviezip.global.security.MemberPrincipal
import team.b5.moviezip.member.repository.MemberRepository
import team.b5.moviezip.movie.dto.MovieResponse
import team.b5.moviezip.movie.dto.MovieSearchResult
import team.b5.moviezip.movie.dto.toMovieSearchResultList
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.repository.MovieRepository
import team.b5.moviezip.movie.repository.MovieSpecifications

@Service
@Transactional
class MovieService(
    private val movieRepository: MovieRepository,
    private val memberRepository: MemberRepository

    ) {
    fun getAllMovies(pageable: Pageable): Page<Movie> {
        return movieRepository.findAll(pageable)
    }

    fun getMovies(movieId: Long): MovieResponse {
        val movieOptional = movieRepository.findById(movieId)
        val movie = movieOptional.orElseThrow { ModelNotFoundException("movie") }
        return MovieResponse.from(movie)

    }

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


    fun validateLikeOrDislike(memberId: Long, movieId: Long, target: String) {
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

    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("회원")

    private fun getMovie(movieId: Long) =
        movieRepository.findByIdOrNull(movieId) ?: throw ModelNotFoundException("영화")



/*    fun addMovies() =
        getMoviesFromCsvFile().forEach { movieRepository.save(it) }
    private val movieRepository: MovieRepository,
    private val memberRepository: MemberRepository
) {
    // 영화 데이터 등록
    fun addMovies() = getMoviesFromCsvFile().forEach { movieRepository.save(it) }

    // 영화 단건 조회
    fun findMovie(movieId: Long) = MovieResponse.from(getMovie(movieId))

    // 좋아요
    fun like(memberPrincipal: MemberPrincipal, movieId: Long) =
        validateLikeOrDislike(memberPrincipal.id, movieId, "like")
            .run { getMovie(movieId).like(getMember(memberPrincipal.id)) }

    // 싫어요
    fun dislike(memberPrincipal: MemberPrincipal, movieId: Long) =
        validateLikeOrDislike(memberPrincipal.id, movieId, "dislike")
            .run { getMovie(movieId).dislike(getMember(memberPrincipal.id)) }

    // CSV 데이터 불러오기
    fun getMoviesFromCsvFile(): ArrayList<Movie> {
        val movies = arrayListOf<Movie>()
        val csvReader = CSVReader(
            InputStreamReader(FileInputStream(getPath()))
        )
        csvReader.readNext()

        do {
            val data = csvReader.readNext()
            if (data != null)
                if (invalidateCsvData(data)) continue else movies.add(csvToEntity(data))
        } while (data != null)

        return movies
    }

    // CSV 데이터를 엔티티로 변환
    private fun csvToEntity(data: Array<String>) = Movie(
        name = data[0],
        description = "",
        releaseAt = ZonedDateTime.of(
            LocalDate.parse(
                data[1],
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ).atStartOfDay(),
            ZoneId.of("Asia/Seoul")
        ),
        audience = data[2].replace(",", "").toLong(),
        ratings = 0.0,
        nation = MovieNation.valueOf(
            MovieVariables.movieNationMap[data[3]]!!
        ),
        distributor = data[4],
        director = "",
        status = MovieStatus.NORMAL,
        like = mutableSetOf(),
        dislike = mutableSetOf()
    )

    // CSV 데이터 검증
    private fun invalidateCsvData(data: Array<String>) =
        data[0].isEmpty() || data[1].isEmpty() || data[2].isEmpty() || data[2].replace(",", "").toLongOrNull() == null
                || data[3].isEmpty() || !MovieVariables.movieNationMap.containsKey(data[3]) || data[4].isEmpty()

    // 경로
    private fun getPath() = Paths.get(
        System.getProperty("user.dir"), "movie-zip/src/main/resources/static/test1.csv"
    ).toString()
        System.getProperty("user.dir"), "src/main/resources/static/movie.csv"
    ).toString()

    // 좋아요, 싫어요 중복 검증
    fun validateLikeOrDislike(memberId: Long, movieId: Long, target: String) {
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

    // 영화 조회
    private fun getMovie(movieId: Long) =
        movieRepository.findByIdOrNull(movieId) ?: throw ModelNotFoundException("영화")
    */
}