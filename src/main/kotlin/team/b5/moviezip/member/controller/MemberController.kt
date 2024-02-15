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
import team.b5.moviezip.member.dto.request.FindEmailRequest
import team.b5.moviezip.member.dto.request.MemberLoginRequest
import team.b5.moviezip.member.dto.request.MemberRequest
import team.b5.moviezip.member.dto.response.MemberLoginResponse
import team.b5.moviezip.member.service.MemberService
import java.net.URI

@RestController
class MemberController(
    private val memberService: MemberService
) {
    // 회원가입
    @PostMapping("/signup")
    fun signup(@RequestBody memberRequest: MemberRequest) =
        ResponseEntity.created(URI.create("/")).body(memberService.signup(memberRequest))

    // 프로필 조회
    @GetMapping("/members/{memberId}")
    fun findMember(@PathVariable memberId: Long) =
        ResponseEntity.ok().body(memberService.findMember(memberId))
        
    // 프로필 수정
    @PutMapping("/members/{memberId}")
    fun update(@RequestBody memberRequest: MemberRequest, @PathVariable memberId: Long) =
        ResponseEntity.ok().body(memberService.update(memberRequest, memberId))

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