package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import com.ohgiraffers.ukki.user.model.service.CookieService;
import com.ohgiraffers.ukki.user.model.service.MypageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class MypageController {

    private MypageService mypageService;
    private CookieService cookieService;

    @Autowired
    public MypageController (MypageService mypageService, CookieService cookieService) {
        this.mypageService = mypageService;
        this.cookieService = cookieService;
    }

/*     해당 방법은 헤더로 보냈을 때 사용하는 방식으로 우리는 헤더 방식이 아닙니다. 참고해주세요 !
    @GetMapping("/{userId}")
    public MypageDTO getUserInfo(@PathVariable String userId, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        return mypageService.getUserInfoFromToken(jwtToken, userId);
    }*/

    // 쿠키로만 보냈을 땐 이 방법을 사용해야하고 우리는 쿠키로 검증하는 방식을 사용할겁니다. 쿠키서비스에 getJWTCookie 메소드를 통해 검증하시고 사용하시면 됩니다 !
    @GetMapping("/{userId}")
    public MypageDTO getUserInfo(@PathVariable String userId, HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰 일치 안함");
        }

        return mypageService.getUserInfoFromToken(jwtToken, userId);
    }

}
