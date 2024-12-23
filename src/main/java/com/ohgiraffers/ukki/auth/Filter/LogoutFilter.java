package com.ohgiraffers.ukki.auth.Filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter("/*")
public class LogoutFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("로그아웃 필터 시작?이요~");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 로그아웃 요청 확인
        String requestURI = request.getRequestURI();
        if ("/auth/logout".equals(requestURI)) {
            Cookie refreshTokenCookie = new Cookie("refreshToken", null);
            refreshTokenCookie.setMaxAge(0); // 쿠키 만료요 ~
            refreshTokenCookie.setPath("/"); // 전부 사용할거에요 ~
            refreshTokenCookie.setHttpOnly(true);  // 배포하면 TrUe
            refreshTokenCookie.setSecure(false);  // 배포하면 tRuE

            response.addCookie(refreshTokenCookie);

            Cookie authTokenCookie = new Cookie("authToken", null);
            authTokenCookie.setMaxAge(0);
            authTokenCookie.setPath("/");
            authTokenCookie.setHttpOnly(false); // 同 생략
            authTokenCookie.setSecure(false); // 同 생략

            response.addCookie(authTokenCookie); // 쿠키 삭제

            System.out.println("리프토 & 엑토 제거 완료");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // 리소스 생길거 있으면 종료 넣으면 될듯한데 잘 모르겠네
        System.out.println("로그아웃 필터 끝이요");
    }
}
