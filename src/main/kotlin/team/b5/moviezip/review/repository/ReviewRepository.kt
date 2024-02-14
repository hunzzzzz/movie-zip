package team.b5.moviezip.review.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b5.moviezip.review.model.Review

@Repository
interface ReviewRepository : JpaRepository<Review, Long>