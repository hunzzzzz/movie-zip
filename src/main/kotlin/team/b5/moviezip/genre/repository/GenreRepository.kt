package team.b5.moviezip.genre.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b5.moviezip.genre.model.Genre

@Repository
interface GenreRepository : JpaRepository<Genre, Long> {
    fun findByKorName(korName: String): Genre?
}