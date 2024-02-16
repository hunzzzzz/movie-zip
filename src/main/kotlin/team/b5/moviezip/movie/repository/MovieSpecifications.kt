package team.b5.moviezip.movie.repository

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import team.b5.moviezip.movie.model.Movie


class MovieSpecifications {
    companion object {
        fun searchMovies(name: String?, nation: String?, distributor: String?): Specification<Movie> {
            return Specification { root, query, criteriaBuilder ->
                val predicates = mutableListOf<Predicate>()

                if (!name.isNullOrBlank()) {
                    predicates.add(like(root, criteriaBuilder, "name", name))
                }
                if (!nation.isNullOrBlank()) {
                    predicates.add(equal(root, criteriaBuilder, "nation", nation))
                }

                if (!distributor.isNullOrBlank()) {
                    predicates.add(like(root, criteriaBuilder, "distributor", distributor))
                }

                criteriaBuilder.and(*predicates.toTypedArray())
            }
        }

        private fun like(root: Root<Movie>, criteriaBuilder: CriteriaBuilder, property: String, value: String): Predicate {
            return criteriaBuilder.like(root.get(property), "%$value%")
        }

        private fun equal(root: Root<Movie>, criteriaBuilder: CriteriaBuilder, property: String, value: String): Predicate {
            return criteriaBuilder.equal(root.get<Any>(property), value)
        }
    }
}