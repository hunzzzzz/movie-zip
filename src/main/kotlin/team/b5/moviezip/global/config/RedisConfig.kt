package team.b5.moviezip.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import team.b5.moviezip.movie.dto.response.MovieResponse
import java.time.Duration

@Configuration
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    private lateinit var redisHost: String

    @Value("\${spring.data.redis.port}")
    private val redisPort: Int = 0

//    @Value("\${spring.data.redis.password}")
//    private lateinit var redisPassword: String

    private val redisDefaultTime: Long = 1000 * 60 * 60 // 1시간

    @Bean
    fun redisConnectionFactory() =
        LettuceConnectionFactory(
            RedisStandaloneConfiguration()
                .let {
                    it.hostName = redisHost
                    it.port = redisPort
//                    it.password = RedisPassword.of(redisPassword)
                    it
                }
        )

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory, objectMapper: ObjectMapper): RedisTemplate<String, MovieResponse> {
        val template = RedisTemplate<String, MovieResponse>()
        template.setConnectionFactory(redisConnectionFactory)
        template.keySerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        val jsonRedisSerializer = Jackson2JsonRedisSerializer(objectMapper,MovieResponse::class.java)
        template.valueSerializer = jsonRedisSerializer
        return template
    }


    @Bean
    fun redisCacheManager(
        redisConnectionFactory: RedisConnectionFactory,
        objectMapper: ObjectMapper
    ): CacheManager =
        RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(
                RedisCacheConfiguration.defaultCacheConfig()
                    .disableCachingNullValues()
                    .entryTtl(Duration.ofMillis(redisDefaultTime))
                    .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                            .fromSerializer(StringRedisSerializer())
                    ).serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                            .fromSerializer(JdkSerializationRedisSerializer())
                    )
            ).build()

}
