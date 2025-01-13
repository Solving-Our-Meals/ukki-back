package com.ohgiraffers.ukki.store.model.dao;

import com.ohgiraffers.ukki.store.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreMapper {

    StoreInfoDTO getStoreInfo(long storeNo);

    KeywordDTO getKeyword(long storeNo);

    OperationDTO getOperation(long storeNo);

    BannerDTO getBannerList(long storeNo);

    ReviewDTO getReviewList(long storeNo);

    String getReviewCount(String today);

    void createReview(ReviewContentDTO reviewContentDTO);

    List<StoreInfoDTO> getStoresLocation(@Param("category") int category);

    ReviewDTO getReviewListByScope(long storeNo);

    List<ReservationInfoDTO> getUserReviewList(String userId, long storeNo);

    boolean checkReviewList(long resNo);

    void increaseReview(long userNo);

    List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO);

    List<StoreInfoDTO> searchStores(String storeName);

}
