package team.b5.moviezip.global.service

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CacheEvict(
    private val cacheManager: CacheManager
) {

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    @CacheEvict(allEntries = true , value = ["movies"], cacheManager = "redisCacheManager")
    fun cacheEvictWithRenewable(){
//        cacheManager.cacheNames.forEach(movie_thing)
//        cacheManager.getCache(movie_thing).clear()
        // 새벽 6시에 시네마나 진흥원 사이트의 csv를 가져올 수 있는 로직 (희망사항)
    }

}