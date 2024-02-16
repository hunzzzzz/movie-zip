package team.b5.moviezip.genre.model

import jakarta.persistence.*

@Entity
@Table(name = "Genre")
class Genre(
    @Column(name = "kor_name", nullable = false, unique = true)
    val korName: String,

    @Column(name = "eng_name", nullable = false, unique = true)
    val engName: String
) {
    @Id
    @Column(name = "genre_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}