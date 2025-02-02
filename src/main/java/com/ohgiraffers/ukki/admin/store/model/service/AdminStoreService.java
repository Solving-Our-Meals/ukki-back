package com.ohgiraffers.ukki.admin.store.model.service;

import com.ohgiraffers.ukki.admin.store.model.dao.AdminStoreMapper;
import com.ohgiraffers.ukki.admin.store.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        return adminStoreMapper.monthlyRegistStore() != null ? 
            adminStoreMapper.monthlyRegistStore() : 
            new ArrayList<>();
    }

    public int totalRegistStore() {
        return adminStoreMapper.totalRegistStore() != 0 ? 
            adminStoreMapper.totalRegistStore() : 
            0;
    }

    public List<AdminStoreListDTO> searchStores(String category, String word) {
        Map<String, String> params = new HashMap<>();
        params.put("category", category);
        params.put("word", word);
        
        // null이 아닌 빈 리스트 반환
        return adminStoreMapper.searchBy(params) != null ? 
            adminStoreMapper.searchBy(params) : 
            new ArrayList<>();
    }

    public AdminStoreInfoDTO searchStoreInfo(long storeNo) {
        return adminStoreMapper.searchStoreInfo(storeNo);
    }

    public AdminStoreKeywordDTO getKeyword(long storeNo) {
        return adminStoreMapper.getKeyword(storeNo);
    }

    public AdminStoreOperationDTO getOperation(long storeNo) {
        return adminStoreMapper.getOperation(storeNo);
    }

    public List<AdminStoreCategoryDTO> getCategory() { return adminStoreMapper.getCategory();
    }

    @Transactional(rollbackFor = Exception.class)
    public void editOperationTime(AdminStoreOperationDTO operationTime) {
            adminStoreMapper.editOperationTime(operationTime);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editKeyword(AdminStoreKeywordDTO storeKeyword) {
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
    public void editBanner(AdminStoreBannerDTO adminStoreBannerDTO) {
        adminStoreMapper.editBanner(adminStoreBannerDTO);
    }

    public int lastStoreNo() {
        return adminStoreMapper.lastStoreNo();
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertOperationTime(AdminStoreOperationDTO adminStoreOperationDTO) {
        return adminStoreMapper.insertOperationTime(adminStoreOperationDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertKeyword(AdminStoreKeywordDTO adminStoreKeywordDTO) {
        return adminStoreMapper.insertKeyword(adminStoreKeywordDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertBanner(AdminStoreBannerDTO adminStoreBannerDTO) {
        return adminStoreMapper.insertBanner(adminStoreBannerDTO);
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

    public AdminStoreBannerDTO getBanner(Long storeNo) {
        return adminStoreMapper.getBanner(storeNo);
    }

    public void insertMondayTime(DayTimeDTO dayTimeDTO) {
        adminStoreMapper.insertMondayTime(dayTimeDTO);
    }

    public void insertTuesdayTime(DayTimeDTO dayTimeDTO) {
        adminStoreMapper.insertTuesdayTime(dayTimeDTO);
    }

    public void insertWednesdayTime(DayTimeDTO dayTimeDTO) {
        adminStoreMapper.insertWednesdayTime(dayTimeDTO);
    }

    public void insertThursdayTime(DayTimeDTO dayTimeDTO) {
        adminStoreMapper.insertThursdayTime(dayTimeDTO);
    }

    public void insertFridayTime(DayTimeDTO dayTimeDTO) {
        adminStoreMapper.insertFridayTime(dayTimeDTO);
    }

    public void insertSaturdayTime(DayTimeDTO dayTimeDTO) {
        adminStoreMapper.insertSaturdayTime(dayTimeDTO);
    }

    public void insertSundayTime(DayTimeDTO dayTimeDTO) {
        adminStoreMapper.insertSundayTime(dayTimeDTO);
    }

    public void deleteStoreUser(int storeNo) {
        adminStoreMapper.deleteStoreUser(storeNo);
    }
}
