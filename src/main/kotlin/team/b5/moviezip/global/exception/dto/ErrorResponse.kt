package team.b5.moviezip.global.exception.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ErrorResponse(
    @JsonProperty("http_status")
    val httpStatus: String,
    val message: String,
    val path: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val time: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("error_content")
    val errorContent: String? = null
)