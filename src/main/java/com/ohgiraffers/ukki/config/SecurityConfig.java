package com.ohgiraffers.ukki.config;

import com.ohgiraffers.ukki.auth.Filter.JwtFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/main", "/", "/store", "/info", "/auth/**", "/user/info", "/image/**", "/css/**", "/**").permitAll()

                        .requestMatchers("/user/**", "/main", "/", "/store/**").hasRole("USER")
                        .requestMatchers("/admin/**", "/boss/**").denyAll()

                        .requestMatchers("/boss/**").hasRole("STORE")

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated())

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터
                .cors(withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // 로그아웃 URL 설정
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 로그아웃 성공 시 쿠키 삭제
                            deleteCookie(response, "authToken"); // authToken 쿠키 삭제
                            deleteCookie(response, "refreshToken"); // refreshToken 쿠키 삭제

                            response.setStatus(HttpServletResponse.SC_OK); // 로그아웃 성공 시 상태 코드 200 OK
                        })
                        .invalidateHttpSession(true)  // 세션 무효화
                        .clearAuthentication(true)    // 인증 정보 초기화
                        .deleteCookies("authToken", "refreshToken")  // 쿠키 삭제
                        .permitAll());
        return http.build();
    }

    private void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain("ukki.site");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//         configuration.setAllowedOrigins(Arrays.asList("http://localhost", "http://localhost:80", "http://localhost:3000"));
       configuration.setAllowedOrigins(Arrays.asList("https://ukki.site"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);  // 쿠키를 포함한 요청을 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
