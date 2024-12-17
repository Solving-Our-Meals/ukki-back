package com.ohgiraffers.ukki.config;

import com.ohgiraffers.ukki.auth.Filter.JwtFilter;
import com.ohgiraffers.ukki.common.UserRole;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 비밀번호 암호화 기능
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    // 정적 리소스 무시로 css, js, 이미지 보안적용 X (백엔드 static과 프론트 public 무시용도?)
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    // 권한 주기 !
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests( auth -> {
            auth.requestMatchers( "/auth/**", "/css/**", "/img/**", "/error/**", "/find/**").permitAll();
            auth.requestMatchers("/admin/**").hasAnyAuthority(UserRole.ADMIN.getRole());
            auth.requestMatchers("/bosses/**").hasAnyAuthority(UserRole.STORE.getRole());
            auth.requestMatchers("/users/**").hasAnyAuthority(UserRole.USER.getRole());
            auth.anyRequest().authenticated();

        }).formLogin( login -> {
            login.loginPage("/auth/login");
            login.usernameParameter("userId");
            login.passwordParameter("userPass");
            login.defaultSuccessUrl("/main", true);
//            login.successHandler(new SavedRequestAwareAuthenticationSuccessHandler());  // 일단 보류 -> 로그인하면 이전에 접근하려던 페이지로 이동
        })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .logout( logout -> {
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"));
            logout.deleteCookies("JSESSIONID");
            logout.invalidateHttpSession(true);
            logout.logoutSuccessUrl("/");

        }).sessionManagement( session -> {
            session.maximumSessions(1);
            session.invalidSessionUrl("/auth/login");

        }).csrf( csrf -> csrf.disable());

        return http.build();
    }

}
