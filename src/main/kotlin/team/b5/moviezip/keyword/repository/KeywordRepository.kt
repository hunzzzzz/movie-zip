package team.b5.moviezip.keyword.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.b5.moviezip.keyword.model.HotKeyword

interface KeywordRepository:JpaRepository<HotKeyword, Long> {

    fun existsByKeyword(keyword: String):Boolean

    fun findByKeyword(keyword: String):HotKeyword

}