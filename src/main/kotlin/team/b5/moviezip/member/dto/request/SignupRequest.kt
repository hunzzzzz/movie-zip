package team.b5.moviezip.member.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.security.crypto.password.PasswordEncoder
import team.b5.moviezip.member.model.Member
import team.b5.moviezip.member.model.MemberRole
import team.b5.moviezip.member.model.MemberStatus

data class SignupRequest(
    @field:NotBlank(message = "사용자 이름은 필수 항목입니다.")
    val name: String,

    @field:NotBlank(message = "닉네임은 필수 항목입니다.")
    val nickname: String,

    @field:NotBlank(message = "이메일은 필수 항목입니다.")
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 항목입니다.")
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&+=]).{8,16}$",
        message = "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상 16자 이하여야 합니다"
    )
    val password: String,

    @field:NotBlank(message = "비밀번호를 다시 한 번 입력해주세요.")
    val password2: String,
) {
    fun to(passwordEncoder: PasswordEncoder) = Member(
        role = MemberRole.USER,
        name = name,
        nickname = nickname,
        email = email,
        password = passwordEncoder.encode(password),
        status = MemberStatus.NORMAL,
    )
}