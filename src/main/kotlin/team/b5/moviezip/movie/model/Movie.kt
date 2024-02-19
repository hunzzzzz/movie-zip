package team.b5.moviezip.movie.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import team.b5.moviezip.genre.model.Genre
import team.b5.moviezip.member.model.Member
import team.b5.moviezip.review.model.Review
import java.time.ZonedDateTime

@Entity
@Table(name = "Movies")
class Movie(
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "release_at")
    val releaseAt: ZonedDateTime?,

    @Column(name = "sales")
    val sales: String?,

    @Column(name = "audience", nullable = false)
    val audience: Long?, // TODO : 추후 Long으로 변경

    @Column(name = "screens")
    var screens: Int?, // TODO : 추후 Int로 변경

    @Column(name = "nation")
    @Enumerated(EnumType.STRING)
    val nation: MovieNation, // TODO : 추후 MovieNation으로 변경

    @Column(name = "distributor")
    val distributor: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: MovieStatus,

    @Column(name = "age_limit")
    val ageLimit: String,

    @Column(name = "director")
    val director: String,

    @Column(name = "actor")
    val actor: String,

    @Column(name = "description")
    val description: String,

    @Column(name = "search_count")
    var searchCount: Long? = 0,

    @Column(name = "ratings")
    var ratings: Double? = 0.0, // TODO : 추후 Double로 변경

    @ManyToMany
    val like: MutableSet<Member>,

    @ManyToMany
    val dislike: MutableSet<Member>,

    @ManyToMany
    val genre: MutableSet<Genre> = mutableSetOf(),

    @JsonManagedReference
    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL])
    val reviews: List<Review> = mutableListOf()

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
}