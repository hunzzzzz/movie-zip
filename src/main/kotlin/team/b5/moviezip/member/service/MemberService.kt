package team.b5.moviezip.member.service

import org.springframework.stereotype.Service
import team.b5.moviezip.member.repository.MemberRepository

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
}