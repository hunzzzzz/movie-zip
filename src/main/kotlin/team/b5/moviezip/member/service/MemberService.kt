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
    fun signup(signupRequest: SignupRequest) {
        if (signupRequest.password != signupRequest.password2) throw Exception("") // TODO
        memberRepository.save(signupRequest.to(passwordEncoder))
    }
}