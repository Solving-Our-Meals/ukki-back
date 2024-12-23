 package com.ohgiraffers.ukki.auth.Filter;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
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
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더에서 JWT 토큰 추출
        String token = getTokenFromRequest(request);

        // 토큰 유효성 검증
        if (token != null && jwtService.validateToken(token)) {
            Map<String, Object> userInfo = jwtService.getUserInfoFromToken(token);

            String userId = (String) userInfo.get("userId");  // userId 추출
            String userRole = (String) userInfo.get("userRole");  // userRole 추출


            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, null);  // 사용자 정보는 userId만 사용

            // 인증 객체에 userRole 추가
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    // HTTP 요청에서 Authorization 헤더를 통해 토큰 추출
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰만 추출
        }
        return null;
    }
}
