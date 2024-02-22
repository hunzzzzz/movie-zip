package team.b5.moviezip.keyword.model

import jakarta.persistence.*

@Entity
@Table(name = "hot_keywords")
class HotKeyword(

    @Column(name = "keyword",unique = true, nullable = false)
    val keyword: String,

    @Column(name = "count", nullable = false)
    var count: Long,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}