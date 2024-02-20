package team.b5.moviezip.movie.model

import com.fasterxml.jackson.annotation.JsonCreator
import org.apache.commons.lang3.EnumUtils

enum class MovieNation {
    KOREA, NETHERLANDS, TAIWAN, GERMANY, DENMARK, RUSSIA, MEXICO,
    USA, BRAZIL, SPAIN, UK, ITALY, INDIA, JAPAN, CHINA, CANADA, FRANCE, HONGKONG;

    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun parse(name: String?): MovieNation? =
            name?.let { EnumUtils.getEnumIgnoreCase(MovieNation::class.java, it.trim()) }
    }
}