package com.ohgiraffers.ukki.store.model.service;

import com.ohgiraffers.ukki.store.model.dao.StoreMapper;
import com.ohgiraffers.ukki.store.model.dto.*;
import com.ohgiraffers.ukki.user.model.dto.SearchDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void deleteReview(long reviewNo) {
        storeMapper.deleteReview(reviewNo);
    }

    public List<StoreInfoDTO> getStoresLocation(int category) {
        return storeMapper.getStoresLocation(category);
    }


    public ReviewDTO getReviewListByScope(long storeNo) {
        return storeMapper.getReviewListByScope(storeNo);
    }

    public ReviewDTO getReviewListByLowScope(long storeNo) {
        return storeMapper.getReviewListByLowScope(storeNo);
    }

    public List<StoreReservationInfoDTO> getUserReviewList(String userId, long storeNo) {
        return storeMapper.getUserReviewList(userId, storeNo);
    }

    public NoReviewReservListDTO checkReviewList(long resNo) {
        return storeMapper.checkReviewList(resNo);
    }

    public void increaseReview(long userNo) {
        storeMapper.increaseReview(userNo);
    }

    public void decreaseReview(long userNo) {
        storeMapper.decreaseReview(userNo);
    }

    public List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO) {
        return storeMapper.getResPosNum(storeResPosNumDTO);
    }


    // 메인

    public List<StoreInfoDTO> searchStores(String storeName) {
        return storeMapper.searchStores(storeName);  // StoreMapper의 searchStores 호출
    }


    public List<String> getPopularSearches() {
        // DB에서 데이터를 조회하는 메서드 호출
        List<SearchDTO> searchDTOList = storeMapper.getPopularSearches();
        List<String> popularSearches = new ArrayList<>();
        System.out.println("Retrieved SearchDTO List: " + searchDTOList);

        // SearchDTO에서 searchWord만 추출하여 리스트에 추가
        for (SearchDTO searchDTO : searchDTOList) {
            if (searchDTO != null && searchDTO.getSearchWord() != null) {
                popularSearches.add(searchDTO.getSearchWord());
            } else {
                System.out.println("Skipping null or invalid searchWord in SearchDTO");
            }
        }

        System.out.println("Popular Searches: " + popularSearches);
        return popularSearches;
    }

    public void insertOrUpdateSearch(String keyword) {
        storeMapper.insertOrUpdateSearch(keyword);
    }

    public ReviewContentDTO getReviewContent(long reviewNo) {
        return storeMapper.getReviewContent(reviewNo);
    }

    public int getTotalReservations(int storeNo) {
        return storeMapper.selectTotalReservations(storeNo);  // Fetch total reservations from DB
    }

    public int getTodayReservations(int storeNo) {
        return storeMapper.selectTodayReservationCount(storeNo);  // Fetch today's reservation count
    }

    public List<Integer> getWeeklyReservations(int storeNo) {
        return storeMapper.selectWeeklyReservationCount(storeNo);  // Fetch weekly reservation counts
    }

    public int getAvailableSlots(int storeNo, String reservationDate) {
        return storeMapper.selectAvailableReservationPeople(storeNo, reservationDate);  // Fetch available slots
    }

    public List<Map<String, Object>> getReservationList(int storeNo) {
        return storeMapper.selectReservationStatusByStore(storeNo);  // Fetch reservation list
    }

    public List<StoreReservationInfoDTO> getReviewReservationList(List<Long> resNo) {
        return storeMapper.getReviewReservationList(resNo);
    }
}