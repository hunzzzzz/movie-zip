package team.b5.moviezip.member.dto.request

import jakarta.validation.constraints.NotBlank

data class SignupRequest(
    @field:NotBlank(message = "사용자 이름은 필수 항목입니다.")
    val name: String
) {

}