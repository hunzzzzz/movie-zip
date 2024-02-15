package team.b5.moviezip.review.dto

import java.time.ZonedDateTime

data class ReviewResponse(
    val id: Long,
    val name: String,
    val ratings: Int,
    val content: String,
    val createdAt: ZonedDateTime,
)
