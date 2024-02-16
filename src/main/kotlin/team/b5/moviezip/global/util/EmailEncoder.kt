package team.b5.moviezip.global.util

object EmailEncoder {
    fun encode(email: String) =
        email.substring(0, email.indexOf('@'))
            .let {
                if (it.length >= 10)
                    email.replaceRange(it.length / 2 - 2, it.length / 2 + 2, "*****")
                else
                    email.replaceRange(it.length / 2 - 1, it.length / 2 + 1, "***")
            }
}