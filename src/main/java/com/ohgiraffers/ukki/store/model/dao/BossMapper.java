package com.ohgiraffers.ukki.store.model.dao;

import com.ohgiraffers.ukki.store.controller.ReservationDTO;
import com.ohgiraffers.ukki.store.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BossMapper {

    // 가게 정보 조회
    StoreInfoDTO getStoreInfo(long userNo);

    List<ReservationDTO> selectReservationStatusByStore(int storeNo);

    List<ReservationDTO> selectReservationPeopleList(int storeNo);

    int selectAvailableReservationPeople(int storeNo, String reservationDate);

    WeeklyReservationCountDTO selectWeeklyReservationCount(int storeNo);

    int selectTodayReservationCount(int storeNo);



    String getNextAvailableTime(int storeNo, String resDate, String currentTime);

    List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO);

    // 최신 리뷰 조회
    ReviewContentDTO getRecentReview(long storeNo);

    // 리뷰 리스트 가져오기
    ReviewDTO getReviewList(long storeNo);

    // 리뷰 상세 조회
    DetailReviewInfoDTO getReviewInfo(long reviewNo);

    // 리뷰 신고
    void reportReview(ReportReviewDTO reportReviewDTO);
}
