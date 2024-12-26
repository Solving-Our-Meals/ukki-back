package com.ohgiraffers.ukki.auth.model.service;

import com.ohgiraffers.ukki.auth.model.dao.FindMapper;
import org.springframework.stereotype.Service;

@Service
public class FindService {

    private final FindMapper findMapper;

    public FindService(FindMapper findMapper) {
        this.findMapper = findMapper;
    }

    public boolean findUserId(String email) {
        return findMapper.findUserIdByEmail(email) != null;
    }

    public boolean findUserPassword(String email) {
        return findMapper.findUserPasswordByEmail(email) != null;
    }
}
