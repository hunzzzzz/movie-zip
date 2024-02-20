package team.b5.moviezip.movie.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team.b5.moviezip.global.querydsl.QueryDslSupport
import team.b5.moviezip.movie.model.Movie
import team.b5.moviezip.movie.model.MovieNation
import team.b5.moviezip.movie.model.MovieStatus
import team.b5.moviezip.movie.model.QMovie

@Repository
class MovieRepositoryImpl : QueryDslSupport(), CustomMovieRepository {
    private val movie = QMovie.movie

    override fun searchMovies(
        thing: String,
        status: MovieStatus?,
        nation: MovieNation?,
        pageable: Pageable
    ): Page<Movie> {
        val whereClaus = BooleanBuilder().and(movie.name.containsIgnoreCase(thing))
        status?.let { whereClaus.and(movie.status.eq(status)) }
        nation?.let { whereClaus.and(movie.nation.eq(nation)) }

        val totalCount = queryFactory.select(movie.count()).from(movie).where(whereClaus).fetchOne() ?: 0L

        val contents = queryFactory.selectFrom(movie)
            .where(whereClaus)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*orderConditions(pageable, movie))
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }

    // 정렬 조건 추출
    private fun orderConditions(pageable: Pageable, path: EntityPathBase<*>) =
        PathBuilder(path.type, path.metadata)
            .let {
                pageable.sort.toList().map { order ->
                    OrderSpecifier(
                        Order.DESC, it.get(order.property) as Expression<Comparable<*>>
                    )
                }.toTypedArray()
            }
}