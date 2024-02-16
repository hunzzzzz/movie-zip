package team.b5.moviezip.review.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.security.UserPrincipal
import team.b5.moviezip.member.repository.MemberRepository
import team.b5.moviezip.movie.repository.MovieRepository
import team.b5.moviezip.review.dto.ReviewRequest
import team.b5.moviezip.review.dto.ReviewResponse
import team.b5.moviezip.review.model.Review
import team.b5.moviezip.review.model.ReviewStatus
import team.b5.moviezip.review.repository.ReviewRepository

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository,
    private val movieRepository: MovieRepository
) {

    // 리뷰 목록 조회
    fun getReviews(movieId: Long, page: Int, sort: String, order: String): Page<ReviewResponse> {
        if (page <= 0) throw IllegalArgumentException("페이지는 1부터 시작해요.")
        if (sort != "created_at" && sort != "rating") throw IllegalArgumentException("sort 기준은 created_at/ rating 중 하나로 입력해주세요.")
        val pageable: Pageable = PageRequest.of(
            page - 1,
            15,
            getSort(sort, order)
        )
        val reviewPage: Page<Review> =
            reviewRepository.findAllByMovieIdAndStatus(movieId, ReviewStatus.NORMAL, pageable)
//        if (reviewPage.isEmpty)
        return reviewPage
            .map {
                ReviewResponse(
                    id = it.id!!,
                    name = it.member.name,
                    ratings = it.rating,
                    content = it.content,
                    createdAt = it.createdAt
                )
            }
    }

    // 리뷰 생성
    @Transactional
    fun createReview(
        movieId: Long,
        memberPrincipal: UserPrincipal,
        request: ReviewRequest
    ) {
        val member = getMemberInfo(memberPrincipal.id)
        val movie = getMovieInfo(movieId)

        reviewRepository.save(
            Review(
                content = request.content,
                rating = request.rating,
                status = ReviewStatus.NORMAL,
                movie = movie,
                member = member
            )
        )
    }

    //리뷰 수정
    @Transactional
    fun updateReview(
        reviewId: Long,
        memberPrincipal: UserPrincipal,
        request: ReviewRequest,
    ) {
        val member = getMemberInfo(memberPrincipal.id)
        val review = getReviewInfo(reviewId)

        if (review.member != member) {
            throw IllegalArgumentException("작성자만 수정이 가능해요.")
        } else {
            review.content = request.content
            review.rating = request.rating
            reviewRepository.save(review)
        }
    }

    // 리뷰 삭제
    @Transactional
    fun deleteReview(
        reviewId: Long,
        memberPrincipal: UserPrincipal,
    ) {
        val member = getMemberInfo(memberPrincipal.id)
        val review = getReviewInfo(reviewId)

        if (review.member != member) {
            throw IllegalArgumentException("작성자만 삭제가 가능해요.")
        } else {
            review.status = ReviewStatus.WAIT_FOR_DELETE
            reviewRepository.save(review)
        }
    }


    // 멤버 정보
    private fun getMemberInfo(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("계정 정보가 존재하지 않아요.")

    // 영화 정보
    private fun getMovieInfo(movieId: Long) =
        movieRepository.findByIdOrNull(movieId) ?: throw IllegalArgumentException("영화 정보가 존재하지 않아요.")

    // 리뷰 정보
    private fun getReviewInfo(reviewId: Long) =
        reviewRepository.findByIdOrNull(reviewId) ?: throw IllegalArgumentException("리뷰 정보가 존재하지 않아요.")

    // 리뷰 목록 조회시 정렬 기준
    private fun getSort(sort: String, order: String) =
        when (order) {
            "ascending" -> Sort.by(sort).ascending()
            "descending" -> Sort.by(sort).descending()
            else -> throw IllegalArgumentException("order는 ascending/ descending 중 하나로 입력해주세요.")
        }

    // 평균 별점 (소수 둘째 자리 반올림)
    private fun averageStar(movieId: Long):Double{
        val reviews= reviewRepository.findAllByMovieIdAndStatus(movieId, ReviewStatus.NORMAL)
        var star:Int= 0
        if (reviews.isEmpty()) throw IllegalStateException("등록된 별점이 없어요.")
        for (review in reviews){
            star+= review.rating*10
        }
        star /= reviews.size
        return star.toDouble()/10
//        return String.format("%.1f",star/reviews.size)
    }

}