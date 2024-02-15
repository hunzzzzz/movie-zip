package team.b5.moviezip.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.security.jwt.JwtPlugin
import team.b5.moviezip.member.dto.request.MemberLoginRequest
import team.b5.moviezip.member.dto.request.MemberRequest
import team.b5.moviezip.member.dto.response.MemberLoginResponse
import team.b5.moviezip.member.repository.MemberRepository

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) {
    // 회원가입
    fun signup(memberRequest: MemberRequest) =
        memberRequest.let {
            validateRequest(it)
            memberRepository.save(it.to(passwordEncoder))
        }

    // 프로필 수정
    fun update(memberRequest: MemberRequest, memberId: Long) =
        memberRequest.let {
            validateRequest(it, memberId)
            getMember(memberId).update(it)
        }

    // 회원가입 검증
    private fun validateRequest(memberRequest: MemberRequest) {
        if (memberRepository.existsByNickname(memberRequest.nickname)) throw Exception("") // TODO
        else if (memberRepository.existsByEmail(memberRequest.email)) throw Exception("") // TODO
        else if (memberRequest.password != memberRequest.password2) throw Exception("") // TODO
    }

    // 프로필 수정 시 검증 (본인이 기존에 사용하던 nickname, email은 검증 대상에서 제외)
    private fun validateRequest(memberRequest: MemberRequest, memberId: Long) {
        if (memberRepository.existsByNickname(memberRequest.nickname) && memberRepository.findByNickname(memberRequest.nickname).id != memberId)
            throw Exception("") // TODO
        else if (memberRepository.existsByEmail(memberRequest.email) && memberRepository.findByEmail(memberRequest.email)?.id != memberId)
            throw Exception("") // TODO
        else if (memberRequest.password != memberRequest.password2) throw Exception("") // TODO
    }

    // 회원 조회
    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw Exception("") // TODO

    //로그인
    fun login(memberLoginRequest: MemberLoginRequest): MemberLoginResponse {
        val member =
            memberRepository.findByEmail(memberLoginRequest.email) ?: throw Exception("") // TODO
        if (!passwordEncoder.matches(
                memberLoginRequest.password,
                member.password
            )
        ) {
            throw Exception("") // TODO
        }
        return MemberLoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = member.id.toString(),
                email = member.email,
                role = member.role.name
            )
        )
    }
}
//