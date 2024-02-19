package team.b5.moviezip.global.exception.case

data class MemberNotMatchedException(
    override val message: String? = "작성자만 편집할 수 있어요."
) : RuntimeException()
