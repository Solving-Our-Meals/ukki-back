package com.ohgiraffers.ukki.user.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignupMapper {

    int signupId(String userId);

    int signupNickname(String userName);
}
