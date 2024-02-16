package team.b5.moviezip.global.service

import com.opencsv.CSVReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.genre.model.Genre
import team.b5.moviezip.genre.repository.GenreRepository
import team.b5.moviezip.global.variables.MovieVariables
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
class DataInitService(
    private val genreRepository: GenreRepository,
    private val movieRepository: MovieRepository
) {
    // 장르 추가
    fun addGenre() =
        MovieVariables.movieGenreMap.forEach {
            genreRepository.save(
                Genre(
                    korName = it.key,
                    engName = it.value
                )
            )
        }

    // 영화 추가
    fun addMovies() = getMoviesFromCsvFile().forEach { movieRepository.save(it) }

    // CSV 데이터 불러오기
    private fun getMoviesFromCsvFile(): ArrayList<Movie> {
        val movies = arrayListOf<Movie>()
        val csvReader = CSVReader(
            InputStreamReader(FileInputStream(getPath()))
        )
        csvReader.readNext()

        do {
            val data = csvReader.readNext()
            if (data != null)
                if (invalidateCsvData(data, movies)) continue
                else movies.add(csvToEntity(data))
        } while (data != null)

        return movies
    }

    // CSV 데이터를 엔티티로 변환
    private fun csvToEntity(data: Array<String>) =
        Movie(
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
            dislike = mutableSetOf(),
            genre = getGenreFromCsvData(data[6])
        ).let {
            println(it.name)
            it
        }

    // CSV 데이터 검증
    private fun invalidateCsvData(data: Array<String>, movies: ArrayList<Movie>) =
        data[0].isEmpty() || movies.any { it.name == data[0] }
                || data[1].isEmpty()
                || data[2].isEmpty() || data[2].replace(",", "").toLongOrNull() == null
                || data[3].isEmpty() || !MovieVariables.movieNationMap.containsKey(data[3])
                || data[4].isEmpty()
                || data[6].isEmpty()

    // 경로
    private fun getPath() = Paths.get(
        System.getProperty("user.dir"), "src/main/resources/static/movie.csv"
    ).toString()

    // 장르 대입
    private fun getGenreFromCsvData(genreStr: String) =
        mutableSetOf<Genre>().let {
            genreStr.split(",").forEach { genre ->
                if (getGenreByKorName(genre) != null) it.add(getGenreByKorName(genre)!!)
            }
            it
        }

    // 장르 조회
    private fun getGenreByKorName(korName: String) = genreRepository.findByKorName(korName)
}