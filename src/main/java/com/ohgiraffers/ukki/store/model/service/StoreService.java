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


    public StoreInfoDTO getStoreInfo(long storeNo) {
        return storeMapper.getStoreInfo(storeNo);
    }

    public KeywordDTO getKeyword(long storeNo) {
        return storeMapper.getKeyword(storeNo);
    }

    public OperationDTO getOperation(long storeNo) {
        return storeMapper.getOperation(storeNo);
    }

    public BannerDTO getBannerList(long storeNo) {
        return storeMapper.getBannerList(storeNo);
    }

    public ReviewDTO getReviewList(long storeNo) {
        return storeMapper.getReviewList(storeNo);
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


    public ReviewDTO getReviewListByScope(long storeNo) {
        return storeMapper.getReviewListByScope(storeNo);
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

    public List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO) {
        return storeMapper.getResPosNum(storeResPosNumDTO);

    public List<StoreInfoDTO> searchStores(String storeName) {
        return storeMapper.searchStores(storeName);  // StoreMapper의 searchStores 호출
    }
}
