package team.b5.moviezip.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class EditPasswordRequest(
    @field:NotBlank(message = "현재 비밀번호는 입력해주세요.")
    val currentPassword: String,

    @field:NotBlank(message = "비밀번호는 필수 항목입니다.")
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&+=]).{8,16}$",
        message = "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상 16자 이하여야 합니다"
    )
    val newPassword: String,

    @field:NotBlank(message = "비밀번호를 다시 한 번 입력해주세요.")
    val newPassword2: String,
)