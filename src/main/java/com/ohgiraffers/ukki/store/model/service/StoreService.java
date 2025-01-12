package com.ohgiraffers.ukki.store.model.service;

import com.ohgiraffers.ukki.store.model.dao.StoreMapper;
import com.ohgiraffers.ukki.store.model.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreMapper storeMapper;

    public StoreService(StoreMapper storeMapper){
        this.storeMapper = storeMapper;
    }


    public StoreInfoDTO getStoreInfo(StoreInfoDTO storeInfoDTO) {
        return storeMapper.getStoreInfo(storeInfoDTO);
    }

    public KeywordDTO getKeyword(StoreInfoDTO storeInfoDTO) {
        return storeMapper.getKeyword(storeInfoDTO);
    }

    public OperationDTO getOperation(StoreInfoDTO storeInfoDTO) {
        return storeMapper.getOperation(storeInfoDTO);
    }

    public BannerDTO getBannerList(StoreInfoDTO storeInfoDTO) {
        return storeMapper.getBannerList(storeInfoDTO);
    }

    public ReviewDTO getReviewList(StoreInfoDTO storeInfoDTO) {
        return storeMapper.getReviewList(storeInfoDTO);
    }

    public String getReviewCount(String today) {
        return storeMapper.getReviewCount(today);
    }

    public void createReview(ReviewContentDTO reviewContentDTO) {
        storeMapper.createReview(reviewContentDTO);
    }

    public List<StoreInfoDTO> getStoresLocation(int category) {
        return storeMapper.getStoresLocation(category);
    }


    public ReviewDTO getReviewListByScope(StoreInfoDTO storeInfoDTO) {
        return storeMapper.getReviewListByScope(storeInfoDTO);
    }

    public List<ReservationInfoDTO> getUserReviewList(String userId, long storeNo) {
        return storeMapper.getUserReviewList(userId, storeNo);
    }

    public boolean checkReviewList(long resNo) {
        return storeMapper.checkReviewList(resNo);
    }

    public void increaseReview(long userNo) {
        storeMapper.increaseReview(userNo);
    }

    public List<StoreInfoDTO> searchStores(String storeName) {
        return storeMapper.searchStores(storeName);  // StoreMapper의 searchStores 호출
    }
}
