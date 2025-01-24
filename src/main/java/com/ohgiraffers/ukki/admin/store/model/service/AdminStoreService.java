package com.ohgiraffers.ukki.admin.store.model.service;

import com.ohgiraffers.ukki.admin.store.model.dao.AdminStoreMapper;
import com.ohgiraffers.ukki.admin.store.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = Exception.class)
    public void editOperationTime(OperationDTO operationTime) {
            adminStoreMapper.editOperationTime(operationTime);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editKeyword(KeywordDTO storeKeyword) {
        adminStoreMapper.editKeyword(storeKeyword);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editStore(AdminStoreInfoDTO storeData) {
        System.out.println(storeData);
        adminStoreMapper.editStore(storeData);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreInfo(int storeNo) {

        return adminStoreMapper.deleteStoreInfo(storeNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStoreBanner(int storeNo) {
        adminStoreMapper.deleteStoreBanner(storeNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStoreOperation(int storeNo) {
        adminStoreMapper.deleteStoreOperation(storeNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editBanner(BannerDTO bannerDTO) {
        adminStoreMapper.editBanner(bannerDTO);
    }

    public int lastStoreNo() {
        return adminStoreMapper.lastStoreNo();
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertOperationTime(OperationDTO operationDTO) {
        return adminStoreMapper.insertOperationTime(operationDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertKeyword(KeywordDTO keywordDTO) {
        return adminStoreMapper.insertKeyword(keywordDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertBanner(BannerDTO bannerDTO) {
        return adminStoreMapper.insertBanner(bannerDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertStoreUser(AdminStoreUserDTO userData) {
        adminStoreMapper.insertStoreUser(userData);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertStore(AdminStoreInfoDTO storeData) {
        adminStoreMapper.insertStore(storeData);
    }

    public int searchCurrentStoreUser(String userId) {
        System.out.println(userId);
        return adminStoreMapper.searchCurrentStoreUser(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStoreKeyword(int storeNo) {
        adminStoreMapper.deleteStoreKeyword(storeNo);
    }

    public String[] getReviewImgStoreNo(int storeNo) {
        return adminStoreMapper.getReviewImgStoreNo(storeNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteReviewWithStore(int storeNo) {
        adminStoreMapper.deleteReviewWithStore(storeNo);
    }
}
