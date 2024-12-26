package com.ohgiraffers.ukki.admin.store.model.service;

import com.ohgiraffers.ukki.admin.store.model.dao.AdminStoreMapper;
import com.ohgiraffers.ukki.admin.store.model.dto.MonthlyRegistStoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminStoreService {
    private final AdminStoreMapper adminStoreMapper;

    @Autowired
    public AdminStoreService(AdminStoreMapper adminStoreMapper){
        this.adminStoreMapper = adminStoreMapper;
    }

    public List<MonthlyRegistStoreDTO> monthlyRegistStore() {
        return adminStoreMapper.monthlyRegistStore();
    }

    public int totalRegistStore() {
        return adminStoreMapper.totalRegistStore();
    }
}
