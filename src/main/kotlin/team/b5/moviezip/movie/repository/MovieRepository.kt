package team.b5.moviezip.movie.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team.b5.moviezip.movie.model.Movie

@Repository
interface MovieRepository : JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    @Query("SELECT m FROM Movie m ORDER BY m.audience DESC")
     fun findTop20MoviesByAudience(): List<Movie>

    @Query("SELECT m FROM Movie m WHERE m.searchCount > 0 ORDER BY m.searchCount DESC")
    fun findTop10BySearchCountGreaterThanOrderBySearchCountDesc(): List<Movie>

    fun findByName(name: String): Movie?
}