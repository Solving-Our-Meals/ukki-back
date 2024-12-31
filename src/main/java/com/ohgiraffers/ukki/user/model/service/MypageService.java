package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.user.model.dao.MypageMapper;
import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MypageService {

    private final JwtService jwtService;
    private final MypageMapper mypageMapper;

    @Autowired
    public MypageService(JwtService jwtService, MypageMapper mypageMapper) {
        this.jwtService = jwtService;
        this.mypageMapper = mypageMapper;
    }

    public MypageDTO getUserInfoFromToken(String jwtToken, Long userNo) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        Long extractedUserNo = (Long) userInfo.get("userNo");

        if (!extractedUserNo.equals(userNo)) {
            throw new IllegalArgumentException("User ID mismatch");
        }

        MypageDTO mypageDTO = mypageMapper.findUserInfoByUserNo(userNo);

        if (mypageDTO == null) {
            throw new IllegalArgumentException("User not found");
        }

        return mypageDTO;
    }
}
