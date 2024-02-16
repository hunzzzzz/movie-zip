package team.b5.moviezip.movie.model

import jakarta.persistence.*
import team.b5.moviezip.genre.model.Genre
import team.b5.moviezip.global.model.BaseEntity
import team.b5.moviezip.member.model.Member
import java.time.ZonedDateTime

@Entity
@Table(name = "Movies")
class Movie(
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "release_at")
    val releaseAt: ZonedDateTime?,

    @Column(name = "sales")
    val sales: String,

    @Column(name = "audience", nullable = false)
    val audience: String,

    @Column(name = "screens")
    var screens: String,

    @Column(name = "ratings")
    val ratings: String,

    @Column(name = "nation")
    @Enumerated(EnumType.STRING)
    val nation: String,

    @Column(name = "distributor")
    val distributor: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: MovieStatus,

    @Column(name = "description")
    val description: String,

    @Column(name = "director")
    val director: String,

    @Column(name = "search_count")
    var searchCount: Long? = 0,

    @ManyToMany
    val like: MutableSet<Member>,

    @ManyToMany
    val dislike: MutableSet<Member>,

    @ManyToMany
    val genre: MutableSet<Genre>
) : BaseEntity() {
    val dislike: MutableSet<Member>
){
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
}