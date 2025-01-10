package com.ohgiraffers.ukki.user.model.dao;

import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageReservationDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MypageMapper {
    MypageDTO findUserInfoByUserId(String userId);

    List<MypageReservationDTO> findUserReservationByUserId(String userId);
}
