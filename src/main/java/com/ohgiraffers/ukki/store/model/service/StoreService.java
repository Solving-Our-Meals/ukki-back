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



}
