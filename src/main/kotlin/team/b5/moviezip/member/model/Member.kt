package team.b5.moviezip.member.model

import jakarta.persistence.*
import team.b5.moviezip.global.model.BaseEntity
import team.b5.moviezip.member.dto.request.SignupRequest

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

    fun update(signupRequest: SignupRequest) {
        this.name = signupRequest.name
        this.email = signupRequest.email
        this.nickname = signupRequest.nickname
        this.password = signupRequest.password
    }
}