package team.b5.moviezip.global.util

import java.util.*

object RandomDateBuilder {
    private fun getDateOfMonth(month: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            2 -> 28
            4, 6, 9, 11 -> 30
            else -> -1
        }
    }

    fun getRandomStringDate(startYear: Int, endYear: Int): String {
        val year = Random().nextInt(startYear, endYear)
        val month = String.format("%2d", Random().nextInt(1, 12)).replace(" ", "0")
        val date = getDateOfMonth(month.toInt())
        return "${year}-${month}-${date}"
    }
}