package team.b5.moviezip.movie.model

import com.fasterxml.jackson.annotation.JsonCreator
import org.apache.commons.lang3.EnumUtils

enum class MovieStatus {
    TO_BE_RELEASED, RELEASED, NORMAL;

    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun parse(name: String?): MovieStatus? =
            name?.let { EnumUtils.getEnumIgnoreCase(MovieStatus::class.java, it.trim()) }
    }
}