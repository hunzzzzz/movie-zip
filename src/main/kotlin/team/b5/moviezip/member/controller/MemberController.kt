package team.b5.moviezip.member.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.member.dto.request.SignupRequest
import team.b5.moviezip.member.service.MemberService

@RestController
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/signup")
    fun signup(@RequestBody signupRequest: SignupRequest) {
        memberService.signup(signupRequest)
    }
}