package team.b5.moviezip.keyword.service

import org.hibernate.NonUniqueResultException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
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
            rank.append("${i+1}. ${hotKeyword.keyword}   +${hotKeyword.count}\n")
        }
        return rank.toString()
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun countKeywords(keyword: String){
        when (keywordRepository.existsByKeyword(keyword)){
            true -> {
                val keywords= getKeywordInfo(keyword)
                keywords.count++
                keywordRepository.save(keywords)
            }
            false -> {
//                try {
                    keywordRepository.save(
                        HotKeyword(
                            keyword= keyword,
                            count = 1
                        )
                    )
//                } catch (e: DataIntegrityViolationException){
//                    val keywords= getKeywordInfo(keyword)
//                    keywords.count++
//                    keywordRepository.save(keywords)
//                }
            }
        }
    }

    private fun getKeywordInfo(keyword:String) =
        keywordRepository.findByKeyword(keyword)

}