/* package com.ohgiraffers.ukki.auth.Filter;

import com.ohgiraffers.ukki.auth.model.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;
    
    public JwtFilter(AuthService authService) {
        this.authService = authService;
    }

    // 엑세스 토큰
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 토큰 추출?
        String token = authService.getToken(request);

        // 토큰 유효성 검증
        if (token != null && authService.validateToken(token)) {
            String userId = authService.getUserIdFromToken(token);

            // 사용자 인증 객체 생성 및 SecurityContext에 설정
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
    
    // 리프레시 토큰
}
 */
