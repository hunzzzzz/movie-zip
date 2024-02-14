package team.b5.moviezip.review.controller

import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.review.service.ReviewService

@RestController
class ReviewController(
    private val reviewService: ReviewService
) {
}