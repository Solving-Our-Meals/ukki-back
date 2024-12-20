package com.ohgiraffers.ukki.user.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {
    int countByEmail(String email);
}
