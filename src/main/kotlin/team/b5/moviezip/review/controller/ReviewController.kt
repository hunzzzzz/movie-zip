package team.b5.moviezip.review.controller

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.b5.moviezip.global.security.MemberPrincipal
import team.b5.moviezip.review.dto.ReviewRequest
import team.b5.moviezip.review.dto.ReviewResponse
import team.b5.moviezip.review.service.ReviewService

@RestController
@RequestMapping("/{movieId}/review")
class ReviewController(
    private val reviewService: ReviewService
) {

    // 리뷰 목록 조회
    @GetMapping
    fun getReviews(
        @PathVariable movieId: Long,
        @RequestParam(value = "page", defaultValue = "1") page: Int,
        @Parameter(description = "created_at/rating")
        @RequestParam(value = "sort", defaultValue = "created_at") sort: String,
        @Parameter(description = "ascending/descending")
        @RequestParam(value = "order", defaultValue = "descending") order: String,
    ): ResponseEntity<Page<ReviewResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.getReviews(movieId, page, sort, order))
    }

    // 리뷰 작성
    @PostMapping
    fun createReview(
        @PathVariable movieId: Long,
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @RequestBody reviewRequest: ReviewRequest,
    ): ResponseEntity<String> {
        reviewService.createReview(movieId, memberPrincipal, reviewRequest)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("리뷰가 작성되었습니다.")
    }

    // 리뷰 수정
    @PatchMapping("/{reviewId}")
    fun updateReview(
//        @PathVariable movieId: Long,
        @PathVariable reviewId: Long,
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
        @RequestBody reviewRequest: ReviewRequest,
    ): ResponseEntity<String> {
        reviewService.updateReview(reviewId, memberPrincipal, reviewRequest)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("리뷰가 수정되었습니다.")
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    fun deleteReview(
//        @PathVariable movieId: Long,
        @PathVariable reviewId: Long,
        @AuthenticationPrincipal memberPrincipal: MemberPrincipal,
    ): ResponseEntity<String> {
        reviewService.deleteReview(reviewId, memberPrincipal)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("리뷰가 삭제되었습니다.")
    }

}