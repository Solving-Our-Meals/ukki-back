package com.ohgiraffers.ukki.config;

import com.ohgiraffers.ukki.auth.Filter.JwtFilter;
import com.ohgiraffers.ukki.auth.Filter.LogoutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final LogoutFilter logoutFilter;

    public SecurityConfig(JwtFilter jwtFilter, LogoutFilter logoutFilter) {
        this.jwtFilter = jwtFilter;
        this.logoutFilter = logoutFilter;
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
                        .requestMatchers("/auth/**", "/users/**", "/admin/**", "/bosses/**", "/**").permitAll()
                        .anyRequest().authenticated()) // 인증된 사용자
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터
                .addFilterBefore(logoutFilter, JwtFilter.class) // 로그아웃 필터 추가 (JwtFilter 전)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                          .logoutSuccessUrl("http://localhost:3000/")
//                      .logoutSuccessUrl("http://3.39.119.249:80")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("authToken", "refreshToken")
                        .permitAll());

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
         configuration.setAllowedOrigins(Arrays.asList("http://localhost", "http://localhost:80", "http://localhost:3000"));
//     configuration.setAllowedOrigins(Arrays.asList("http://3.39.119.249:3000", "http://3.39.119.249", "http://3.39.119.249:80", "http://3.39.119.249:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
