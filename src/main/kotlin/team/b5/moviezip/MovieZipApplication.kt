package team.b5.moviezip

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class MovieZipApplication

fun main(args: Array<String>) {
    runApplication<MovieZipApplication>(*args)
}