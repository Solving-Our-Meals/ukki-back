package com.ohgiraffers.ukki.user.model.dao;

import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageReservationDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageMapper {
    MypageDTO findUserInfoByUserId(String userId);

    MypageReservationDTO findUserReservationByUserId(String userId);
}
