package team.b5.moviezip.keyword.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import team.b5.moviezip.keyword.service.KeywordService

@RestController
class KeywordController(
    private val keywordService: KeywordService,
) {

    @Operation(summary = "인기검색어 목록 조회")
    @GetMapping("/hot-keywords")
    fun getHotKeywords(
    ):ResponseEntity<String>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(keywordService.getHotKeywords())
    }

}