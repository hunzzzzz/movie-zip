package team.b5.moviezip.member.model

import jakarta.persistence.*
import team.b5.moviezip.global.model.BaseEntity
import team.b5.moviezip.member.dto.request.MemberRequest

@Entity
@Table(name = "Members")
class Member(
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: MemberRole,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "email", nullable = false)
    var email: String,

    @Column(name = "phone", nullable = false)
    var phone: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: MemberStatus
) : BaseEntity() {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun update(memberRequest: MemberRequest) {
        this.name = memberRequest.name
        this.email = memberRequest.email
        this.nickname = memberRequest.nickname
        this.password = memberRequest.password
    }
}