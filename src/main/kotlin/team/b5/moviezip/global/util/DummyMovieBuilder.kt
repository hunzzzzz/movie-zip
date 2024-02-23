package team.b5.moviezip.global.util

import team.b5.moviezip.global.variables.MovieVariables
import java.util.*

object DummyMovieBuilder {
    fun getRandomMovies(value: String) = arrayOf(
        "더미 ${getMoviesName(value)} 영화 ${Random().nextInt(2_000_000_000)}", // name
        getMoviesReleaseAt(value), // releaseAt
        "0", // sales
        "0", // audience
        "0", // screens
        MovieVariables.movieNationMap.keys.toList().random(), // nation
        "더미 배급사", // distributor
        arrayOf("12", "15", "18", "전체").random(), // ageLimit
        MovieVariables.movieGenreMap.keys.toList().random(), // genre
        "더미 감독", // director
        "배우1,배우2,배우3,배우4,배우5", // actor
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