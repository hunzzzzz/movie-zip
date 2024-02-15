package team.b5.moviezip.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class FindEmailRequest(
    @field:NotBlank(message = "사용자 이름은 필수 항목입니다.")
    val name: String,

    @field:NotBlank(message = "핸드폰 번호는 필수 항목입니다.")
    @field:Pattern(
        regexp = "^010-?([0-9]{3,4})-?([0-9]{4})$",
        message = "올바른 휴대폰 번호 형식이 아닙니다."
    )
    val phone: String
)