package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.user.model.dao.MainDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    // 매퍼 주입
    private final MainDAO mainDAO;


    // 매퍼 주입을 위한 생성자
    @Autowired
    public MainService(MainDAO mainDAO) {
        this.mainDAO = mainDAO;
    }
}
