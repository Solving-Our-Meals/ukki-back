package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.user.model.dao.SignupMapper;
import com.ohgiraffers.ukki.user.model.dto.SignupUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final SignupMapper signupMapper;

    @Autowired
    public SignupService(SignupMapper signupMapper) {
        this.signupMapper = signupMapper;
    }

    public boolean signupId(SignupUserDTO signupUserDTO) {
        int count = signupMapper.signupId(signupUserDTO.getUserId());
        return count == 0;
    }

    public boolean signupPwd(String password) {
        // 비밀번호가 영문, 숫자, 특수문자 조합으로 8~20자 사이인지 확인하는 정규 표현식
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,20}$";
        return password.matches(passwordRegex);
    }
}
