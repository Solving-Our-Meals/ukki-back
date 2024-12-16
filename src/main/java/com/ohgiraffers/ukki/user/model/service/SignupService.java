package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.user.model.dao.SignupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final SignupMapper signupMapper;

    @Autowired
    public SignupService(SignupMapper signupMapper) {
        this.signupMapper = signupMapper;
    }

    public boolean signupId(String userId) {
        return signupMapper.signupId(userId) == 0;
    }
}
