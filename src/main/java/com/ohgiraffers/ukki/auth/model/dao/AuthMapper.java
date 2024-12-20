package com.ohgiraffers.ukki.auth.model.dao;

import com.ohgiraffers.ukki.auth.model.dto.AuthDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    AuthDTO getUserByUserId(String userId);
}
