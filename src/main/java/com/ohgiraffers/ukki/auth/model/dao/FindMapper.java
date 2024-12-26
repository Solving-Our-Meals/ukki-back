package com.ohgiraffers.ukki.auth.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FindMapper {
    String findUserIdByEmail(String email);

    String findUserPasswordByEmail(String email);
}
