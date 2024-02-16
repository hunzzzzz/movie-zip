package team.b5.moviezip.keyword.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.keyword.service.KeywordService

@RestController
class KeywordController(
    private val keywordService: KeywordService,
) {

    @GetMapping
    fun getHotKeywords(
    ):ResponseEntity<String>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(keywordService.getHotKeywords())
    }

}