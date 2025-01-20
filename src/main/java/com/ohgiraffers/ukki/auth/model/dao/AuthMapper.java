package com.ohgiraffers.ukki.auth.model.dao;

import com.ohgiraffers.ukki.auth.model.dto.AuthDTO;
import com.ohgiraffers.ukki.auth.model.dto.ForJwtDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.User;

@Mapper
public interface AuthMapper {
    AuthDTO getUserByUserId(String userId);

    int confirmUserId(String userId);

    ForJwtDTO findUserRoleAndUserNoById(String userId);

    AuthDTO findUserById(String userId);
}
