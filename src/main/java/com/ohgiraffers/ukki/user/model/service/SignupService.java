package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.user.model.dao.SignupMapper;
import com.ohgiraffers.ukki.user.model.dto.SignupUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final SignupMapper signupMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SignupService(SignupMapper signupMapper) {
        this.signupMapper = signupMapper;
    }

    public boolean isValidId(String userName) {
        return signupMapper.isValidId(userName);
    }

    public boolean isValidEmail(String email) {
        return signupMapper.isValidEmail(email);
    }

    public boolean isValidNick(String userNick) {
        return signupMapper.isValidNick(userNick);
    }

    public void signup(SignupUserDTO signupUserDTO) {
        signupMapper.signupUser(signupUserDTO);
    }
}
