package team.b5.moviezip.review.dto

import team.b5.moviezip.review.model.Review
import java.time.ZonedDateTime

data class ReviewResponse(
    val name: String,
    val ratings: Int,
    val content: String,
    val createdAt: ZonedDateTime,
) {
    companion object {
        fun from(review: Review) = ReviewResponse(
            name = review.member.name,
            ratings = review.rating,
            content = review.content,
            createdAt = review.createdAt
        )
    }
}
