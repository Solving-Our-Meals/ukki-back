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
            Map<String, String> params = new HashMap<>();
            params.put("category", category);
            params.put("word", word);
            return adminUserMapper.searchBy(params);
    }

    public AdminUserInfoDTO searchUserInfo(int userNo) {
        return adminUserMapper.searchUserInfo(userNo);
    }

    public String searchSaneName() { return adminUserMapper.searchSaneName();
    }

    public int updateUserName(int userNo, String newSaneName) {
        Map<String, String> params = new HashMap<>();
        params.put("userNo", ""+userNo);
        params.put("newSaneName", newSaneName);

        return adminUserMapper.updateUserName(params);
    }

    public int deleteUserInfo(int userNo) {
        return adminUserMapper.deleteUserInfo(userNo);
    }

    public int totalUser() {
        return adminUserMapper.totalUser();
    }
}
