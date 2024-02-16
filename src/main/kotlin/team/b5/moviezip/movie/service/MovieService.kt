package team.b5.moviezip.movie.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.exception.case.ModelNotFoundException
import team.b5.moviezip.movie.dto.MovieResponse
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.repository.MovieRepository
import team.b5.moviezip.movie.repository.MovieSpecifications

@Service
@Transactional
class MovieService(
    private val movieRepository: MovieRepository,

    ) {
    fun getAllMovies(pageable: Pageable): Page<Movie> {
        return movieRepository.findAll(pageable)
    }

    fun getMovie(movieId: Long): MovieResponse {
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

    fun getTopSearch(): List<Movie> {
        val topSearch = movieRepository.findTop10BySearchCountGreaterThanOrderBySearchCountDesc()
        return topSearch.take(10)
    }

/*    fun addMovies() =
        getMoviesFromCsvFile().forEach { movieRepository.save(it) }

    // CSV 데이터 불러오기
    private fun getMoviesFromCsvFile(): ArrayList<Movie> {
        val movies = arrayListOf<Movie>()
        val csvReader = CSVReader(
            InputStreamReader(FileInputStream(getPath()),"UTF-8")
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
        audience = data[2].replace(",", "").toLong().toString(),
        ratings = 0.0.toString(),
        nation = "".toString(),
        distributor = data[4],
        director = "",
        status = MovieStatus.NORMAL,
        sales = 0.toString(),
        screens = 0.toString(),
    )

    // CSV 데이터 검증
    private fun invalidateCsvData(data: Array<String>) =
        data[0].isEmpty() || data[1].isEmpty() || data[2].isEmpty() || data[2].replace(",", "")
            .toLongOrNull() == null
                || data[3].isEmpty() || !MovieVariables.movieNationMap.containsKey(data[3]) || data[4].isEmpty()

    // 경로
    private fun getPath() = Paths.get(
        System.getProperty("user.dir"), "movie-zip/src/main/resources/static/test1.csv"
    ).toString()*/
}
