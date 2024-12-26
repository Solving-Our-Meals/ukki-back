package com.ohgiraffers.ukki.admin.store.model.dao;

import com.ohgiraffers.ukki.admin.store.model.dto.MonthlyRegistStoreDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminStoreMapper {
    List<MonthlyRegistStoreDTO> monthlyRegistStore();

    int totalRegistStore();
}
