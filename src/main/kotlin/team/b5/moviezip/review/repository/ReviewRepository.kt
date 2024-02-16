package team.b5.moviezip.review.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.review.model.Review
import team.b5.moviezip.review.model.ReviewStatus

@Repository
interface ReviewRepository : JpaRepository<Review, Long> {

    // 리뷰 페이징을 위한 정보들
    fun findAllByMovieIdAndStatus(movieId: Long, status: ReviewStatus, pageable: Pageable): Page<Review>

    // 영화 별점 계산을 위한 정보들
    fun findAllByMovieIdAndStatus(movieId: Long, status: ReviewStatus): List<Review>

}