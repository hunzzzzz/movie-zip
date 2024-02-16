package team.b5.moviezip.review.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ReviewRequest(

    @field:NotBlank(message = "별점을 매겨주세요.")
    @field:Pattern(regexp = "^(10|[0-9])\$", message = "별점은 0~10점으로 입력해주세요.")
    val rating: Int,

    @field:Pattern(regexp = "^(?=.*.{0,500}$)", message = "최대 500자까지만 입력할 수 있어요.")
    val content: String,
)
