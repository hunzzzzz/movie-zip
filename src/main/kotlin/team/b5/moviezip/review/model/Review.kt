package team.b5.moviezip.review.model

import jakarta.persistence.*
import team.b5.moviezip.global.model.BaseEntity

@Entity
@Table(name = "Reviews")
class Review(
    @Column(name = "content", nullable = false)
    val content: String,

    @Column(name = "rating", nullable = false)
    val rating: Int,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: ReviewStatus,
) : BaseEntity() {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}