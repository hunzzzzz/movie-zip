package team.b5.moviezip.movie.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team.b5.moviezip.global.querydsl.QueryDslSupport
import team.b5.moviezip.movie.model.MovieAgeLimit
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
        ageLimit: MovieAgeLimit?,
        pageable: Pageable
    ) =
        BooleanBuilder().let {
            it.and(movie.name.containsIgnoreCase(thing))
            status?.let { status -> it.and(movie.status.eq(status)) }
            nation?.let { nation -> it.and(movie.nation.eq(nation)) }
            ageLimit?.let { ageLimit -> it.and(movie.ageLimit.eq(ageLimit)) }

            PageImpl(
                getContents(it, pageable),
                pageable,
                queryFactory.select(movie.count()).from(movie).where(it).fetchOne() ?: 0L
            )
        }

    // 다중 정렬 조건 추출
    private fun getOrderConditions(pageable: Pageable, path: EntityPathBase<*>) =
        PathBuilder(path.type, path.metadata)
            .let {
                pageable.sort.toList().map { order ->
                    OrderSpecifier(
                        Order.DESC, it.get(order.property) as Expression<Comparable<*>>
                    )
                }.toTypedArray()
            }

    // contents 추출
    private fun getContents(whereClaus: BooleanBuilder, pageable: Pageable) = queryFactory.selectFrom(movie)
        .where(whereClaus)
        .offset(pageable.offset)
        .limit(pageable.pageSize.toLong())
        .orderBy(*getOrderConditions(pageable, movie))
        .fetch()
}