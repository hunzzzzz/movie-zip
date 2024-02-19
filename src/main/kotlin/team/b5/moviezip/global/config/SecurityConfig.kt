package team.b5.moviezip.global.config



import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .cors { it.disable() }
            .headers { it.frameOptions { foc -> foc.disable() } }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/h2-console/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/signup",
                    "/login",
                    "/members/find-email",
                    "/movies/**",
                    "/movies/{movieId}/review/**",
                    "/**" // TODO : 추후 삭제
                ).permitAll()
                    .anyRequest().authenticated()
            }
//            .logout {
//                it.logoutUrl("/logout")
//                    .logoutSuccessUrl("/login")
//                    .deleteCookies("Set-Cookie")
//                    .invalidateHttpSession(true)
//            }
            .build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}