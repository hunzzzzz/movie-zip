package team.b5.moviezip.movie.service

import com.opencsv.CSVReader
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.exception.case.ModelNotFoundException
import team.b5.moviezip.global.variables.MovieVariables
import team.b5.moviezip.movie.dto.response.MovieResponse
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.model.MovieNation
import team.b5.moviezip.movie.model.MovieStatus
import team.b5.moviezip.movie.repository.MovieRepository
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Paths
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class MovieService(
    private val movieRepository: MovieRepository
) {
    // 영화 데이터 등록
    fun addMovies() = getMoviesFromCsvFile().forEach { movieRepository.save(it) }

    // 영화 단건 조회
    fun findMovie(movieId: Long) = MovieResponse.from(getMovie(movieId))

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
        status = MovieStatus.NORMAL
    )

    // CSV 데이터 검증
    private fun invalidateCsvData(data: Array<String>) =
        data[0].isEmpty() || data[1].isEmpty() || data[2].isEmpty() || data[2].replace(",", "").toLongOrNull() == null
                || data[3].isEmpty() || !MovieVariables.movieNationMap.containsKey(data[3]) || data[4].isEmpty()

    // 경로
    private fun getPath() = Paths.get(
        System.getProperty("user.dir"), "src/main/resources/static/movie.csv"
    ).toString()

    // 영화 조회
    private fun getMovie(movieId: Long) =
        movieRepository.findByIdOrNull(movieId) ?: throw ModelNotFoundException("Movie")
}