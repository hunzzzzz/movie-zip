package team.b5.moviezip.review.model

import jakarta.persistence.*
import team.b5.moviezip.global.model.BaseEntity
import team.b5.moviezip.member.model.Member
import team.b5.moviezip.movie.model.Movie

@Entity
@Table(name = "reviews")
class Review(
    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "rating", nullable = false)
    var rating: Int,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ReviewStatus,

    @ManyToOne
    @JoinColumn(name = "movie_id")
    val movie: Movie,

    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member

) : BaseEntity() {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}