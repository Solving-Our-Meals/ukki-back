package com.ohgiraffers.ukki.admin.user.model.service;

import com.ohgiraffers.ukki.admin.user.model.dao.AdminUserMapper;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserActInfoDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserInfoDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminUserService {

    private final AdminUserMapper adminUserMapper;

    public AdminUserService(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }


    public int insertActInfo(List<AdminUserActInfoDTO> actInfoList) {
        return adminUserMapper.insertActInfo(actInfoList);
    }

    public List<AdminUserDTO> searchUsers(String category, String word) {
        if (category != null || word != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("category", category);
            params.put("word", word);
            return adminUserMapper.searchBy(params);
        } else {
            return adminUserMapper.searchAllUsers();
        }
    }

    public AdminUserInfoDTO searchUserInfo(int userNo) {
        return adminUserMapper.searchUserInfo(userNo);
    }
}
