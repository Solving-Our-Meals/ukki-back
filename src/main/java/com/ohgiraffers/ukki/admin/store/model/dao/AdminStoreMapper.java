package com.ohgiraffers.ukki.admin.store.model.dao;

import com.ohgiraffers.ukki.admin.store.model.dto.AdminStoreListDTO;
import com.ohgiraffers.ukki.admin.store.model.dto.MonthlyRegistStoreDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminStoreMapper {
    List<MonthlyRegistStoreDTO> monthlyRegistStore();

    int totalRegistStore();

    List<AdminStoreListDTO> searchBy(Map<String, String> params);
}
