package team.b5.moviezip.global.exception

import jakarta.servlet.http.HttpServletRequest
import org.hibernate.query.sqm.UnknownPathException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import team.b5.moviezip.global.exception.case.*
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

    // 비밀번호 재사용
    @ExceptionHandler(AlreadyUsedPasswordException::class)
    fun handleAlreadyUsedPasswordException(e: AlreadyUsedPasswordException) =
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

    // 댓글 중복
    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalStateException(e: IllegalStateException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            httpStatus = "400 Bad Request",
            message = e.message.toString(),
            path = httpServletRequest.requestURI
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

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

    // 잘못된 요청
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                httpStatus = "400 Bad Request",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    // 작성자 불일치
    @ExceptionHandler(MemberNotMatchedException::class)
    fun handleMemberNotMatchedException(e: MemberNotMatchedException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                httpStatus = "400 Bad Request",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    // 잘못된 검색 조건 설정
    @ExceptionHandler(UnknownPathException::class)
    fun handleUnknownPathException(e: UnknownPathException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                httpStatus = "400 Bad Request",
                message = "잘못된 검색 조건입니다",
                path = httpServletRequest.requestURI
            )
        )
}