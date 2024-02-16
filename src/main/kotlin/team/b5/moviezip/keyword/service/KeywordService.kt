package team.b5.moviezip.keyword.service

import org.springframework.stereotype.Service
import team.b5.moviezip.keyword.model.HotKeyword
import team.b5.moviezip.keyword.repository.KeywordRepository

@Service
class KeywordService(
    private val keywordRepository: KeywordRepository
) {

    fun getHotKeywords():String{
        val rank= StringBuilder()
        val keywordRanking= keywordRepository.findAll()
                .sortedByDescending { it.count }.take(5)

        rank.append("인기 검색어\n")
        keywordRanking.forEachIndexed { i, hotKeyword ->
            rank.append("${i+1}. ${hotKeyword.keyword}\n")
        }
        return rank.toString()
    }

    fun countKeywords(keyword: String){
        when (keywordRepository.existsByKeyword(keyword)){
            true -> {
                val keywords= getKeywordInfo(keyword)
                keywords.count++
                keywordRepository.save(keywords)
            }
            false -> {
                keywordRepository.save(
                    HotKeyword(
                        keyword= keyword,
                        count = 1
                    )
                )
            }
        }
    }

    private fun getKeywordInfo(keyword:String) =
        keywordRepository.findByKeyword(keyword)

}