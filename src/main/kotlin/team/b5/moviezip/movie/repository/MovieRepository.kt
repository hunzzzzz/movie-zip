package team.b5.moviezip.movie.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b5.moviezip.movie.model.Movie

@Repository
interface MovieRepository : JpaRepository<Movie, Long> {
}