package team.b5.moviezip.review.service

import org.springframework.stereotype.Service
import team.b5.moviezip.review.repository.ReviewRepository

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository
) {
}