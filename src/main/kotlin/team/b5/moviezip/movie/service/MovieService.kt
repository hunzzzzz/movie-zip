package team.b5.moviezip.movie.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.b5.moviezip.global.exception.case.DuplicatedLikeException
import team.b5.moviezip.global.exception.case.ModelNotFoundException
import team.b5.moviezip.global.security.MemberPrincipal
import team.b5.moviezip.member.repository.MemberRepository
import team.b5.moviezip.movie.dto.response.MovieResponse
import team.b5.moviezip.movie.repository.MovieRepository

@Service
@Transactional
class MovieService(
    private val movieRepository: MovieRepository,
    private val memberRepository: MemberRepository
) {
    // 영화 단건 조회 (id)
    fun findMovie(movieId: Long) = MovieResponse.from(getMovie(movieId))

    // 영화 단건 조회 (name)
    fun findMovieByName(name: String) = MovieResponse.from(getMovie(name))

    // 좋아요
    fun like(memberPrincipal: MemberPrincipal, movieId: Long) =
        validateLikeOrDislike(memberPrincipal.id, movieId, "like")
            .run { getMovie(movieId).like(getMember(memberPrincipal.id)) }

    // 싫어요
    fun dislike(memberPrincipal: MemberPrincipal, movieId: Long) =
        validateLikeOrDislike(memberPrincipal.id, movieId, "dislike")
            .run { getMovie(movieId).dislike(getMember(memberPrincipal.id)) }

    // 좋아요, 싫어요 중복 검증
    fun validateLikeOrDislike(memberId: Long, movieId: Long, target: String) {
        when (target) {
            "like" -> {
                if (getMovie(movieId).like.contains(getMember(memberId)))
                    throw DuplicatedLikeException("좋아요")
            }

            "dislike" -> {
                if (getMovie(movieId).dislike.contains(getMember(memberId)))
                    throw DuplicatedLikeException("싫어요")
            }
        }
    }

    // 멤버 조회
    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("회원")

    // 영화 조회 (id)
    private fun getMovie(movieId: Long) =
        movieRepository.findByIdOrNull(movieId) ?: throw ModelNotFoundException("영화")

    // 영화 조회 (name)
    private fun getMovie(name: String) =
        movieRepository.findByName(name) ?: throw ModelNotFoundException("영화")
}