package team.b5.moviezip.global.service

import com.opencsv.CSVReader
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.variables.MovieVariables
import team.b5.moviezip.movie.dto.request.AddMovieRequest
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
    private val movieRepository: MovieRepository
) {
    // CSV 파일 경로
    private final val csvPath =
        Paths.get(System.getProperty("user.dir"), "src/main/resources/static/movie.csv").toString()

    // 영화 추가 (더미 데이터)
    fun addMovies(request: AddMovieRequest) =
        request.let {
            if (!invalidateData(request.toArray(), null))
                movieRepository.save(dataToEntity(request.toArray()))
        }

    // 영화 추가 (CSV 데이터)
    fun addMovies() = getMoviesFromCsvFile().forEach { movieRepository.save(it) }

    // CSV 데이터 불러오기
    private fun getMoviesFromCsvFile(): ArrayList<Movie> {
        val movies = arrayListOf<Movie>()
        val csvReader = CSVReader(InputStreamReader(FileInputStream(csvPath)))
        csvReader.readNext()

        do {
            val data = csvReader.readNext()
            if (data != null)
                if (invalidateData(data, movies)) continue
                else movies.add(dataToEntity(data))
        } while (data != null)

        return movies
    }

    // CSV 데이터를 엔티티로 변환
    // data = {영화명(0), 개봉일(1), 누적매출액(2), 누적관객수(3), 스크린수(4), 대표국적(5), 배급사(6), 등급(7), 장르(8), 감독(9), 배우(10)}
    private fun dataToEntity(data: Array<String>) =
        Movie(
            name = data[0],
            releaseAt = ZonedDateTime.of(
                LocalDate.parse(
                    data[1],
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ).atStartOfDay(),
                ZoneId.of("Asia/Seoul")
            ),
            status = getMovieStatus(data[1]),
            sales = data[2].replace(",", "").toLong(),
            audience = data[3].replace(",", "").toLong(),
            screens = data[4].replace(",", "").toInt(),
            nation = MovieNation.valueOf(MovieVariables.movieNationMap[data[5]]!!),
            distributor = data[6],
            ageLimit = data[7], // TODO
            genre = data[8],
            director = data[9],
            actor = data[10], // TODO

            description = "",
            ratings = 0.0,
            like = mutableSetOf(),
            dislike = mutableSetOf(),
        )

    // 데이터 검증
    private fun invalidateData(data: Array<String>, movies: ArrayList<Movie>?) =
        movies?.any { it.name == data[0] && it.releaseAt == convertStringDateFromZonedDateTime(data[1]) } ?: false
                || data[0].isEmpty()
                || data[1].isEmpty()
                || data[2].isEmpty() || data[2].replace(",", "").toLongOrNull() == null
                || data[3].isEmpty() || data[3].replace(",", "").toLongOrNull() == null
                || data[4].isEmpty() || data[4].replace(",", "").toIntOrNull() == null
                || data[5].isEmpty() || !MovieVariables.movieNationMap.containsKey(data[5])
                || data[6].isEmpty()
                || data[8].isEmpty()

    // 문자열 형식의 날짜 데이터를 ZonedDateTime 으로 변환
    private fun convertStringDateFromZonedDateTime(date: String) =
        ZonedDateTime.of(
            LocalDate.parse(
                date, DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ).atStartOfDay(),
            ZoneId.of("Asia/Seoul")
        )

    // status 조회
    private fun getMovieStatus(releaseAt: String) =
        convertStringDateFromZonedDateTime(releaseAt)
            .let {
                if (ZonedDateTime.now() > it) MovieStatus.TO_BE_RELEASED
                else if (ZonedDateTime.now() > it.plusDays(45)) MovieStatus.RELEASED
                else MovieStatus.NORMAL
            }

    // 개봉 철회 여부를 하루에 한 번씩 확인 (개봉일 기준 45일이 지난 영화의 status를 NORMAL로 변경)
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    fun checkMovieStatus() =
        movieRepository.findAll()
            .filter { it.status == MovieStatus.RELEASED && it.releaseAt.plusDays(45) < ZonedDateTime.now() }
            .map { it.updateStatus(MovieStatus.NORMAL) }
}