package com.ohgiraffers.ukki.user.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {
    Integer countByEmail(String email);

    Integer getNoshowCountByEmail(String email);
}
