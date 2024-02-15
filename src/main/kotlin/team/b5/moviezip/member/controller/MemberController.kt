package team.b5.moviezip.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.member.dto.request.FindEmailRequest
import team.b5.moviezip.member.dto.request.MemberRequest
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

    // 프로필 수정
    @PutMapping("/members/{memberId}")
    fun update(@RequestBody memberRequest: MemberRequest, @PathVariable memberId: Long) =
        ResponseEntity.ok().body(memberService.update(memberRequest, memberId))

    // 이메일 찾기
    @PostMapping("/members/find-email")
    fun findEmail(@RequestBody findEmailRequest: FindEmailRequest) =
        ResponseEntity.ok().body(memberService.findEmail(findEmailRequest))
}