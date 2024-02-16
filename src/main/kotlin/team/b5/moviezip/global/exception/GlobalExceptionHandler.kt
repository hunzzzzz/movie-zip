package team.b5.moviezip.global.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import team.b5.moviezip.global.exception.case.DuplicatedLikeException
import team.b5.moviezip.global.exception.case.DuplicatedValueException
import team.b5.moviezip.global.exception.case.ModelNotFoundException
import team.b5.moviezip.global.exception.case.PasswordMismatchException
import team.b5.moviezip.global.exception.dto.ErrorResponse

@RestControllerAdvice
class GlobalExceptionHandler(
    private val httpServletRequest: HttpServletRequest
) {
    // 특정 엔티티 조회 실패
    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e: ModelNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                httpStatus = "404 Not Found",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    // 중복값 존재
    @ExceptionHandler(DuplicatedValueException::class)
    fun handleDuplicatedValueException(e: DuplicatedValueException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                httpStatus = "400 Bad Request",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    // 비밀번호 불일치
    @ExceptionHandler(PasswordMismatchException::class)
    fun handlePasswordMismatchException(e: PasswordMismatchException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                httpStatus = "400 Bad Request",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    // 좋아요, 싫어요 중복
    @ExceptionHandler(DuplicatedLikeException::class)
    fun handleDuplicatedLikeException(e: DuplicatedLikeException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                httpStatus = "400 Bad Request",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    // Validation 미통과
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException) =
        ResponseEntity.badRequest().body(
            ErrorResponse(
                httpStatus = "400 Bad Request",
                message = e.bindingResult.allErrors.toMutableList().first().defaultMessage!!,
                path = httpServletRequest.requestURI.toString(),
                errorContent = e.bindingResult.allErrors.toMutableList().first().let {
                    it as FieldError
                    it.field
                }
            )
        )
}