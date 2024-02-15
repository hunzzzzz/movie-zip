package team.b5.moviezip.member.dto.response

import team.b5.moviezip.member.model.Member
import java.time.ZonedDateTime

data class MemberResponse(
    val name: String,
    val nickname: String,
    val email: String,
    val createdAt: ZonedDateTime
) {
    companion object {
        fun from(member: Member) = MemberResponse(
            name = member.name,
            nickname = member.nickname,
            email = member.email,
            createdAt = member.createdAt
        )
    }
}