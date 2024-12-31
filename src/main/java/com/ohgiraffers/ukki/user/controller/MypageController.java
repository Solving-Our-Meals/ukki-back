package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import com.ohgiraffers.ukki.user.model.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class MypageController {

    @Autowired
    private MypageService mypageService;

    @GetMapping("/{userNo}")
    public MypageDTO getUserInfo(@PathVariable int userNo, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

        System.out.println("Received JWT Token: " + jwtToken);
        return mypageService.getUserInfoFromToken(jwtToken, userNo);
    }

}
