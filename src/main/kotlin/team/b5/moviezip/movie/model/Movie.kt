package team.b5.moviezip.movie.model

import jakarta.persistence.*
import team.b5.moviezip.global.model.BaseEntity
import java.time.ZonedDateTime

@Entity
@Table(name = "Movies")
class Movie(
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "release_at", nullable = false)
    val releaseAt: ZonedDateTime,

    @Column(name = "audience", nullable = false)
    val audience: Long,

    @Column(name = "ratings", nullable = false)
    val ratings: Double,

    @Column(name = "nation", nullable = false)
    @Enumerated(EnumType.STRING)
    val nation: MovieNation,

    @Column(name = "distributor", nullable = false)
    val distributor: String,

    @Column(name = "director", nullable = false)
    val director: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: MovieStatus,
) : BaseEntity() {
    @Id
    @Column(name = "movie_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}