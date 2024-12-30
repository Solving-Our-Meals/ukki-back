package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.user.model.dao.SignupMapper;
import com.ohgiraffers.ukki.user.model.dto.SignupUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final SignupMapper signupMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SignupService(SignupMapper signupMapper) {
        this.signupMapper = signupMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean signupId(SignupUserDTO signupUserDTO) {
        int count = signupMapper.signupId(signupUserDTO.getUserId());
        return count == 0;
    }

    public String validatePassword(String password) {
        if (password.length() < 8) {
            return "ⓘ 비밀번호는 최소 8자 이상이어야 합니다.";
        }

        if (!password.matches(".*[0-9].*")) {
            return "ⓘ 비밀번호에는 최소 하나의 숫자가 포함되어야 합니다.";
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-\\=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            return "ⓘ 비밀번호에는 최소 하나의 특수문자가 포함되어야 합니다.";
        }

        return "ⓘ 비밀번호가 유효합니다.";
    }

    public boolean signupNickname(SignupUserDTO signupUserDTO) {
        int count = signupMapper.signupNickname(signupUserDTO.getUserName());
        return count == 0;
    }

    public boolean realSignup(SignupUserDTO signupUserDTO) {
        try {
            String hashedPassword = passwordEncoder.encode(signupUserDTO.getUserPass());
            signupUserDTO.setUserPass(hashedPassword);

            signupMapper.signup(signupUserDTO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}