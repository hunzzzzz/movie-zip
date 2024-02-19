package team.b5.moviezip.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.exception.case.AlreadyUsedPasswordException
import team.b5.moviezip.global.exception.case.DuplicatedValueException
import team.b5.moviezip.global.exception.case.ModelNotFoundException
import team.b5.moviezip.global.exception.case.PasswordMismatchException
import team.b5.moviezip.global.util.EmailEncoder
import team.b5.moviezip.global.security.jwt.JwtPlugin
import team.b5.moviezip.member.dto.request.*
import team.b5.moviezip.member.dto.response.MemberLoginResponse
import team.b5.moviezip.member.model.MemberStatus
import team.b5.moviezip.member.dto.response.MemberResponse
import team.b5.moviezip.member.repository.MemberRepository
import java.time.ZonedDateTime
import java.util.LinkedList

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) {
    // 회원가입
    fun signup(signupRequest: SignupRequest) =
        signupRequest.let {
            validateRequest(it)
            memberRepository.save(it.to(passwordEncoder))
        }

    // 프로필 조회
    fun findMember(memberId: Long) = MemberResponse.from(getMember(memberId))

    // 이메일 찾기
    fun findEmail(findEmailRequest: FindEmailRequest) =
        EmailEncoder.encode(
            email = getMemberByNameAndPhone(findEmailRequest.name, findEmailRequest.phone).email
        )

    // 프로필 수정
    fun updateProfile(editProfileRequest: EditProfileRequest, memberId: Long) =
        editProfileRequest.let {
            validateRequest(it, memberId)
            getMember(memberId).update(it)
        }

    // 비밀번호 변경
    fun updatePassword(editPasswordRequest: EditPasswordRequest, memberId: Long) =
        editPasswordRequest.let {
            getMember(memberId).update(
                newPassword = it.newPassword,
                passwordHistory = validatePassword(memberId, it.currentPassword, it.newPassword),
                passwordEncoder = passwordEncoder
            )
        }

    // 회원 탈퇴 (신청)
    fun withdrawal(memberId: Long) = getMember(memberId).updateForWithdrawal()

    // 회원 탈퇴 여부를 2시간에 한 번씩 확인
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 2)
    fun checkWithdrawal() =
        memberRepository.findAll()
            .filter {
                it.status == MemberStatus.WITHDRAWN && it.updatedAt.plusDays(90) < ZonedDateTime.now()
            }.map { memberRepository.delete(it) }

    // 로그인
    fun login(memberLoginRequest: MemberLoginRequest): MemberLoginResponse {
        val member =
            memberRepository.findByEmail(memberLoginRequest.email) ?: throw ModelNotFoundException("회원")
        if (!passwordEncoder.matches(
                memberLoginRequest.password,
                member.password
            )
        ) {
            throw PasswordMismatchException()
        }
        return MemberLoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = member.id.toString(),
                email = member.email,
                role = member.role.name
            )
        )
    }

    // 회원가입 시 검증
    private fun validateRequest(signupRequest: SignupRequest) {
        if (memberRepository.existsByNickname(signupRequest.nickname)) throw DuplicatedValueException("닉네임")
        else if (memberRepository.existsByEmail(signupRequest.email)) throw DuplicatedValueException("이메일")
        else if (signupRequest.password != signupRequest.password2) throw PasswordMismatchException()
    }

    // 프로필 수정 시 검증 (본인이 기존에 사용하던 nickname, email은 검증 대상에서 제외)
    private fun validateRequest(editProfileRequest: EditProfileRequest, memberId: Long) {
        if (memberRepository.existsByNickname(editProfileRequest.nickname)
            && getMemberByNickname(editProfileRequest.nickname).id != memberId
        ) throw DuplicatedValueException("닉네임")
        else if (memberRepository.existsByEmail(editProfileRequest.email)
            && getMemberByEmail(editProfileRequest.email).id != memberId
        ) throw DuplicatedValueException("이메일")
        else if (!passwordEncoder.matches(
                editProfileRequest.password, getMember(memberId).password
            )
        ) throw PasswordMismatchException()
    }

    // 비밀번호 변경 시 검증
    private fun validatePassword(memberId: Long, currentPassword: String, newPassword: String) =
        LinkedList<String>().let { queue ->
            (getMember(memberId).passwordHistory.split(" ")).forEach { queue.add(it) }
            if (!passwordEncoder.matches(currentPassword, getMember(memberId).password))
                throw PasswordMismatchException()
            else if (queue.any { encodedPassword ->
                    passwordEncoder.matches(newPassword, encodedPassword)
                }) throw AlreadyUsedPasswordException()
            else if (queue.size < 3) {
                queue.add(passwordEncoder.encode(newPassword))
                queue.joinToString(" ")
            }
            else {
                queue.remove()
                queue.add(passwordEncoder.encode(newPassword))
                queue.joinToString(" ")
            }
        }

    // 회원 조회 (memberId)
    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("회원")

    // 회원 조회 (name, phone)
    private fun getMemberByNameAndPhone(name: String, phone: String) =
        memberRepository.findByNameAndPhone(name, phone) ?: throw ModelNotFoundException("회원")

    // 회원 조회 (nickname)
    private fun getMemberByNickname(nickname: String) =
        memberRepository.findByNickname(nickname) ?: throw ModelNotFoundException("회원")

    // 회원 조회 (email)
    private fun getMemberByEmail(email: String) =
        memberRepository.findByEmail(email) ?: throw ModelNotFoundException("회원")
}