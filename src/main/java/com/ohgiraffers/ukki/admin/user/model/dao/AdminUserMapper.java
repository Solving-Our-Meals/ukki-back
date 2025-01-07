package com.ohgiraffers.ukki.admin.user.model.dao;

import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserActInfoDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminUserMapper {

    int insertActInfo(List<AdminUserActInfoDTO> actInfoList);

    List<AdminUserDTO> searchAllUsers();

    List<AdminUserDTO> searchBy(Map<String, String> params);

    List<AdminUserDTO> searchByWord(@Param("word") String word);

    AdminUserInfoDTO searchUserInfo(int userNo);

    String searchSaneName();

    int updateUserName(Map<String, String> params);

    int deleteUserInfo(int userNo);
}
