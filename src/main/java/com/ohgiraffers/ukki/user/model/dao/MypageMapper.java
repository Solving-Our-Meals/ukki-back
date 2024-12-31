package com.ohgiraffers.ukki.user.model.dao;

import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageMapper {
    MypageDTO findUserInfoByUserNo(Long userNo);
}
