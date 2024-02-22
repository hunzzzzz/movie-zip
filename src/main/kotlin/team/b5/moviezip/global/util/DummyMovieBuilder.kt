package team.b5.moviezip.global.util

import team.b5.moviezip.global.variables.MovieVariables
import team.b5.moviezip.movie.dto.request.AddMovieRequest
import java.util.*

object DummyMovieBuilder {
    fun getRandomMovies(value: String) = AddMovieRequest(
        name = "더미 ${getMoviesName(value)} 영화 ${Random().nextInt(2_000_000_000)}",
        releaseAt = getMoviesReleaseAt(value),
        sales = "0",
        audience = "0",
        screens = "0",
        nation = MovieVariables.movieNationMap.keys.toList().random(),
        distributor = "더미 배급사",
        ageLimit = arrayOf("12", "15", "18", "전체").random(),
        genre = MovieVariables.movieGenreMap.keys.toList().random(),
        director = "더미 감독",
        actor = "배우1,배우2,배우3,배우4,배우5",
    )

    private fun getMoviesName(value: String) = when (value) {
        "old" -> "고전"
        "latest" -> "개봉전"
        else -> ""
    }

    private fun getMoviesReleaseAt(value: String) = when (value) {
        "old" -> RandomDateBuilder.getRandomStringDate(1990, 2000)
        "latest" -> RandomDateBuilder.getRandomStringDate(2024, 2026)
        else -> ""
    }
}