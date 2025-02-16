package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import com.ohgiraffers.ukki.user.model.service.CookieService;
import com.ohgiraffers.ukki.user.model.service.MypageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private JwtService jwtService;
    private MypageService mypageService;
    private CookieService cookieService;

    @Autowired
    public UserController (MypageService mypageService, CookieService cookieService, JwtService jwtService) {
        this.mypageService = mypageService;
        this.cookieService = cookieService;
        this.jwtService = jwtService;
    }

    //    httpOnly를 사용하면서 이 방식으로 전부 백엔드에서 사용합니다.
    @GetMapping("/info")
    public MypageDTO getUserInfo(HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken != null) {
            String userId = jwtService.getUserInfoFromTokenId(jwtToken);

            if (userId != null) {
                return mypageService.getUserInfoFromToken(jwtToken, userId);
            }
        }

        // 로그인하지 않은 경우 기본 사용자 정보 반환
        return mypageService.getDefaultUserInfo();
    }

}
