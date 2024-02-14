package team.b5.moviezip.member.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import team.b5.moviezip.member.dto.request.SignupRequest
import team.b5.moviezip.member.repository.MemberRepository

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    // 회원가입
    fun signup(signupRequest: SignupRequest) =
        signupRequest.let {
            validateSignupRequest(it)
            memberRepository.save(it.to(passwordEncoder))
        }

    // 회원가입 검증
    private fun validateSignupRequest(signupRequest: SignupRequest) {
        if (memberRepository.existsByNickname(signupRequest.nickname)) throw Exception("") // TODO
        else if (memberRepository.existsByEmail(signupRequest.email)) throw Exception("") // TODO
        else if (signupRequest.password != signupRequest.password2) throw Exception("") // TODO
    }
}