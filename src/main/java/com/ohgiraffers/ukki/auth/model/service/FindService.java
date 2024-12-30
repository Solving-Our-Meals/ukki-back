package com.ohgiraffers.ukki.auth.model.service;

import com.ohgiraffers.ukki.auth.model.dao.FindMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FindService {

    private final FindMapper findMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    public FindService(FindMapper findMapper, BCryptPasswordEncoder passwordEncoder) {
        this.findMapper = findMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public String findUserIdByEmail(String email) {
        return findMapper.findUserIdByEmail(email);
    }

    public boolean changePassword(String email, String userPass) {
        String encryptedPassword = passwordEncoder.encode(userPass);
        return findMapper.changePwd(email, encryptedPassword);
    }

    // 비밀번호 확인 절차 -> 쓰지는 않고 있는데 백엔드로 뺄 경우 쓰면 될듯 함 -> 현재 비밀번호 찾기에선 프론트로 해놓음 (회원가입은 백엔드)
    public String validatePassword(String newPassword) {
        if (newPassword.length() < 8) {
            return "ⓘ 비밀번호는 최소 8자 이상이어야 합니다.";
        }

        if (!newPassword.matches(".*[0-9].*")) {
            return "ⓘ 비밀번호에는 최소 하나의 숫자가 포함되어야 합니다.";
        }

        if (!newPassword.matches(".*[!@#$%^&*()_+\\-\\=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            return "ⓘ 비밀번호에는 최소 하나의 특수문자가 포함되어야 합니다.";
        }

        return "ⓘ 비밀번호가 유효합니다.";
    }
}
