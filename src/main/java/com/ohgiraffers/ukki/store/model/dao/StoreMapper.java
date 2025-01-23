package com.ohgiraffers.ukki.store.model.dao;

import com.ohgiraffers.ukki.store.model.dto.*;
import com.ohgiraffers.ukki.user.model.dto.SearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StoreMapper {

    StoreInfoDTO getStoreInfo(long storeNo);

    KeywordDTO getKeyword(long storeNo);

    OperationDTO getOperation(long storeNo);

    BannerDTO getBannerList(long storeNo);

    ReviewDTO getReviewList(long storeNo);

    String getReviewCount(String today);

    void createReview(ReviewContentDTO reviewContentDTO);

    void deleteReview(long reviewNo);

    List<StoreInfoDTO> getStoresLocation(@Param("category") int category);

    ReviewDTO getReviewListByScope(long storeNo);

    ReviewDTO getReviewListByLowScope(long storeNo);

    List<ReservationInfoDTO> getUserReviewList(String userId, long storeNo);

    boolean checkReviewList(long resNo);

    void increaseReview(long userNo);

    void decreaseReview(long userNo);

    List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO);

    // 메인

    List<StoreInfoDTO> searchStores(@Param("storeName") String storeName);

    List<SearchDTO> getPopularSearches();  // 반환 타입을 SearchDTO 리스트로 수정

    void insertOrUpdateSearch(@Param("storeName") String storeName);

    ReviewContentDTO getReviewContent(long reviewNo);

    int selectTotalReservations(int storeNo);

    int selectTodayReservationCount(int storeNo);

    List<Integer> selectWeeklyReservationCount(int storeNo);

    int selectAvailableReservationPeople(int storeNo, String reservationDate);

    List<Map<String, Object>> selectReservationStatusByStore(int storeNo);
}

