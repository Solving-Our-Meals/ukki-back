package com.ohgiraffers.ukki.store.model.dao;

import com.ohgiraffers.ukki.store.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreMapper {

    StoreInfoDTO getStoreInfo(StoreInfoDTO storeInfoDTO);

    KeywordDTO getKeyword(StoreInfoDTO storeInfoDTO);

    OperationDTO getOperation(StoreInfoDTO storeInfoDTO);

    BannerDTO getBannerList(StoreInfoDTO storeInfoDTO);

    ReviewDTO getReviewList(StoreInfoDTO storeInfoDTO);

    String getReviewCount(String today);

    void createReview(ReviewContentDTO reviewContentDTO);

    List<StoreInfoDTO> getStoresLocation(@Param("category") int category);

}
