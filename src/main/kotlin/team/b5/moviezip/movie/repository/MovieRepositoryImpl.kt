package team.b5.moviezip.movie.repository

import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team.b5.moviezip.global.querydsl.QueryDslSupport
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.model.MovieStatus
import team.b5.moviezip.movie.model.QMovie

@Repository
class MovieRepositoryImpl : QueryDslSupport(), CustomMovieRepository {
    private val movie = QMovie.movie

    override fun searchMoviesByName(name: String, status: MovieStatus?, pageable: Pageable): Page<Movie> {
        val whereClaus = BooleanBuilder()
        status?.let { whereClaus.and(movie.status.eq(status)).and(movie.name.eq(name)) }
            ?: whereClaus.and(movie.name.eq(name))

        val totalCount = queryFactory.select(movie.count()).from(movie).where(whereClaus).fetchOne() ?: 0L
        val query = queryFactory.selectFrom(movie)
            .where(whereClaus)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        if (pageable.sort.isSorted) {
            when (pageable.sort.first()?.property) {
                "like" -> query.orderBy(movie.like.size().desc())
                "ratings" -> query.orderBy(movie.ratings.desc())
                else -> query.orderBy(movie.audience.desc())
            }
        } else query.orderBy(movie.audience.desc())

        val contents = query.fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}