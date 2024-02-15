package team.b5.moviezip.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.util.EmailEncoder
import team.b5.moviezip.member.dto.request.FindEmailRequest
import team.b5.moviezip.member.dto.request.MemberRequest
import team.b5.moviezip.member.model.MemberStatus
import team.b5.moviezip.member.dto.response.MemberResponse
import team.b5.moviezip.member.repository.MemberRepository
import java.time.ZonedDateTime

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    // 회원가입
    fun signup(memberRequest: MemberRequest) =
        memberRequest.let {
            validateRequest(it)
            memberRepository.save(it.to(passwordEncoder))
        }

    // 프로필 조회
    fun findMember(memberId: Long) = MemberResponse.from(getMember(memberId))

    // 프로필 수정
    fun update(memberRequest: MemberRequest, memberId: Long) =
        memberRequest.let {
            validateRequest(it, memberId)
            getMember(memberId).update(it)
        }

    // 이메일 찾기
    fun findEmail(findEmailRequest: FindEmailRequest) =
        EmailEncoder.encode(
            email = getMember(findEmailRequest.name, findEmailRequest.phone).email
        )

    // 회원가입 검증
    private fun validateRequest(memberRequest: MemberRequest) {
        if (memberRepository.existsByNickname(memberRequest.nickname)) throw Exception("") // TODO
        else if (memberRepository.existsByEmail(memberRequest.email)) throw Exception("") // TODO
        else if (memberRequest.password != memberRequest.password2) throw Exception("") // TODO
    }

    // 회원 탈퇴 (신청)
    fun withdrawal(memberId: Long) = getMember(memberId).updateForWithdrawal()

    // 회원 탈퇴 여부를 10초에 한 번씩 확인
    @Scheduled(fixedDelay = 1000 * 10)
    fun checkWithdrawal() =
        memberRepository.findAll()
            .filter {
                it.status == MemberStatus.WITHDRAWN && it.updatedAt.plusDays(90) < ZonedDateTime.now()
            }.map { memberRepository.delete(it) }

    // 프로필 수정 시 검증 (본인이 기존에 사용하던 nickname, email은 검증 대상에서 제외)
    private fun validateRequest(memberRequest: MemberRequest, memberId: Long) {
        if (memberRepository.existsByNickname(memberRequest.nickname) && memberRepository.findByNickname(memberRequest.nickname).id != memberId)
            throw Exception("") // TODO
        else if (memberRepository.existsByEmail(memberRequest.email) && memberRepository.findByEmail(memberRequest.email).id != memberId)
            throw Exception("") // TODO
        else if (memberRequest.password != memberRequest.password2) throw Exception("") // TODO
    }

    // 회원 조회 (memberId)
    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw Exception("") // TODO

    // 회원 조회 (name, phone)
    private fun getMember(name: String, phone: String) =
        memberRepository.findByNameAndPhone(name, phone) ?: throw Exception("") // TODO
}