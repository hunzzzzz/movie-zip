package team.b5.moviezip.member.controller

import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.member.service.MemberService

@RestController
class MemberController(
    private val memberService: MemberService
) {
}