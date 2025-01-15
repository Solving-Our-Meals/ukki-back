package com.ohgiraffers.ukki.admin.store.model.service;

import com.ohgiraffers.ukki.admin.store.model.dao.AdminStoreMapper;
import com.ohgiraffers.ukki.admin.store.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<AdminStoreListDTO> searchStores(String category, String word) {
        Map<String, String> params = new HashMap<>();
        params.put("category", category);
        params.put("word", word);
        return adminStoreMapper.searchBy(params);
    }

    public AdminStoreInfoDTO searchStoreInfo(int storeNo) {
        return adminStoreMapper.searchStoreInfo(storeNo);
    }

    public KeywordDTO getKeyword(long storeNo) {
        return adminStoreMapper.getKeyword(storeNo);
    }

    public OperationDTO getOperation(long storeNo) {
        return adminStoreMapper.getOperation(storeNo);
    }

    public List<CategoryDTO> getCategory() { return adminStoreMapper.getCategory();
    }

    public void editOperationTime(OperationDTO operationTime) {
            adminStoreMapper.editOperationTime(operationTime);
    }

    public void editKeyword(KeywordDTO storeKeyword) {
        adminStoreMapper.editKeyword(storeKeyword);
    }

    public void editStore(AdminStoreInfoDTO storeData) {
        adminStoreMapper.editStore(storeData);
    }

    public int deleteStoreInfo(int storeNo) {

        return adminStoreMapper.deleteStoreInfo(storeNo);
    }

    public void deleteStoreBanner(int storeNo) {
        adminStoreMapper.deleteStoreBanner(storeNo);
    }

    public void deleteStoreOperation(int storeNo) {
        adminStoreMapper.deleteStoreOperation(storeNo);
    }

    public void editBanner(BannerDTO bannerDTO) {
        adminStoreMapper.editBanner(bannerDTO);
    }

    public int lastStoreNo() {
        return adminStoreMapper.lastStoreNo();
    }

    public int insertOperationTime(OperationDTO operationDTO) {
        return adminStoreMapper.insertOperationTime(operationDTO);
    }

    public int insertKeyword(KeywordDTO keywordDTO) {
        return adminStoreMapper.insertKeyword(keywordDTO);
    }

    public int insertBanner(BannerDTO bannerDTO) {
        return adminStoreMapper.insertBanner(bannerDTO);
    }

    public void insertStoreUser(AdminStoreUserDTO userData) {
        adminStoreMapper.insertStoreUser(userData);
    }

    public void insertStore(AdminStoreInfoDTO storeData) {
        adminStoreMapper.insertStore(storeData);
    }

    public int searchCurrentStoreUser(String userId) {
        System.out.println(userId);
        return adminStoreMapper.searchCurrentStoreUser(userId);
    }

    public void deleteStoreKeyword(int storeNo) {
        adminStoreMapper.deleteStoreKeyword(storeNo);
    }

    public String[] getReviewImgStoreNo(int storeNo) {
        return adminStoreMapper.getReviewImgStoreNo(storeNo);
    }

    public void deleteReviewWithStore(int storeNo) {
        adminStoreMapper.deleteReviewWithStore(storeNo);
    }
}
