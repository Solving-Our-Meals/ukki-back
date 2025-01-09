package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.user.model.dao.MypageMapper;
import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public MypageDTO getUserInfoFromToken(String jwtToken, String userId) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }

        MypageDTO mypageDTO = mypageMapper.findUserInfoByUserId(userId);

        if (mypageDTO == null) {
            throw new IllegalArgumentException("사용자 찾을 수 없음");
        }
        return mypageDTO;
    }

    public List<MypageReservationDTO> getUserReservationFromToken(String jwtToken, String userId) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }


        List<MypageReservationDTO> reservations = mypageMapper.findUserReservationByUserId(userId);

        if (reservations == null || reservations.isEmpty()) {
            throw new IllegalArgumentException("예약 정보를 찾을 수 없음");
        }

        return reservations;
    }

}
