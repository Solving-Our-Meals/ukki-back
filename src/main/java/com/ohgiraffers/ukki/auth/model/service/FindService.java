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
}
