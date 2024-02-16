package team.b5.moviezip.member.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.member.dto.request.*
import team.b5.moviezip.member.dto.response.MemberLoginResponse
import team.b5.moviezip.member.service.MemberService
import java.net.URI

@RestController
class MemberController(
    private val memberService: MemberService
) {
    // 회원가입
    @PostMapping("/signup")
    fun signup(@RequestBody signupRequest: SignupRequest) =
        ResponseEntity.created(URI.create("/")).body(memberService.signup(signupRequest))

    // 프로필 조회
    @GetMapping("/members/{memberId}")
    fun findMember(@PathVariable memberId: Long) =
        ResponseEntity.ok().body(memberService.findMember(memberId))

    // 프로필 수정
    @PutMapping("/members/edit-profile/{memberId}")
    fun update(@RequestBody editProfileRequest: EditProfileRequest, @PathVariable memberId: Long) =
        ResponseEntity.ok().body(memberService.updateProfile(editProfileRequest, memberId))

    // 비밀번호 변경
    @PutMapping("/members/edit-password/{memberId}")
    fun update(@RequestBody passwordRequest: EditPasswordRequest, @PathVariable memberId: Long) =
        ResponseEntity.ok().body(memberService.updatePassword(passwordRequest, memberId))

    // 로그인
    @PostMapping("/login")
    fun login(@RequestBody memberLoginRequest: MemberLoginRequest): ResponseEntity<MemberLoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.login(memberLoginRequest))
    }

    // 이메일 찾기
    @PostMapping("/members/find-email")
    fun findEmail(@RequestBody findEmailRequest: FindEmailRequest) =
        ResponseEntity.ok().body(memberService.findEmail(findEmailRequest))

    // 회원 탈퇴
    @DeleteMapping("/members/withdrawal/{memberId}")
    fun withdrawal(@PathVariable memberId: Long) =
        ResponseEntity.ok().body(memberService.withdrawal(memberId))
}