package team.b5.moviezip.member.model

import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder
import team.b5.moviezip.global.model.BaseEntity
import team.b5.moviezip.member.dto.request.EditProfileRequest

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

    @Column(name = "password_history", nullable = false)
    var passwordHistory: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MemberStatus
) : BaseEntity() {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    // 프로필 수정
    fun update(editProfileRequest: EditProfileRequest) {
        this.name = editProfileRequest.name
        this.email = editProfileRequest.email
        this.nickname = editProfileRequest.nickname
        this.phone = editProfileRequest.phone
    }

    // 비밀번호 변경
    fun update(newPassword: String, passwordHistory: String, passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(newPassword)
        this.passwordHistory = passwordHistory
    }

    // 회원 탈퇴를 위한 상태 변경
    fun updateForWithdrawal() {
        this.nickname = "탈퇴한 회원"
        this.status = MemberStatus.WITHDRAWN
    }
}