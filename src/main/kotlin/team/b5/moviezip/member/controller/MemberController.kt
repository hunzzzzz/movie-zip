package team.b5.moviezip.member.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.member.dto.request.SignupRequest
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
}