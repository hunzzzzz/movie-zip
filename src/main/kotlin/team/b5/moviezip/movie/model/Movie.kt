package team.b5.moviezip.movie.model

import jakarta.persistence.*
import team.b5.moviezip.member.model.Member
import java.time.ZonedDateTime

@Entity
@Table(name = "Movies")
class Movie(
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "release_at", nullable = false)
    val releaseAt: ZonedDateTime,

    @Column(name = "sales", nullable = false)
    val sales: Long,

    @Column(name = "audience", nullable = false)
    val audience: Long,

    @Column(name = "screens", nullable = false)
    var screens: Int,

    @Column(name = "nation", nullable = false)
    @Enumerated(EnumType.STRING)
    val nation: MovieNation,

    @Column(name = "distributor", nullable = false)
    val distributor: String,

    @Column(name = "age_limit")
    val ageLimit: String?,

    @Column(name = "genre")
    val genre: String,

    @Column(name = "director", nullable = false)
    val director: String?,

    @Column(name = "actor", columnDefinition = "VARCHAR (2048)")
    val actor: String?,

    @Column(name = "description", nullable = true)
    val description: String?,

    @Column(name = "ratings")
    var ratings: Double = 0.0,

    @Column(name = "search_count")
    var searchCount: Long? = 0,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MovieStatus,

    @ManyToMany
    val like: MutableSet<Member> = mutableSetOf(),

    @ManyToMany
    val dislike: MutableSet<Member> = mutableSetOf()
) {
    @Id
    @Column(name = "movie_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun like(member: Member) {
        this.like.add(member)
    }

    fun dislike(member: Member) {
        this.dislike.add(member)
    }

    fun updateStatus(status: MovieStatus) {
        this.status = status
    }
}