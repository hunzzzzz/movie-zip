package team.b5.moviezip.member.model

import jakarta.persistence.*
import team.b5.moviezip.global.model.BaseEntity

@Entity
@Table(name = "Members")
class Member(
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: MemberRole,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "nickname", nullable = false)
    val nickname: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: MemberStatus
) : BaseEntity() {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}