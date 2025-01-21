package com.ohgiraffers.ukki.store.model.dao;

import com.ohgiraffers.ukki.store.controller.ReservationDTO;
import com.ohgiraffers.ukki.store.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
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

    int findAvailableSlotsByStoreNo(@Param("storeNo") long storeNo);

    // 예약 가능 인원 업데이트
    void updateAvailableSlots(@Param("storeNo") long storeNo, @Param("newSlots") int newSlots);

    // 7일간의 예약 정보 조회
    List<ReservationInfoDTO> findReservationsForPeriod(@Param("storeNo") long storeNo,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);



    List<ReservationDTO> getReservationsForDateAndTime(int storeNo, String reservationDate, String reservationTime);
    List<ReservationDTO> getReservationsForDate(int storeNo, String reservationDate);

    List<ReservationDTO> selectReservationList(long storeNo, String reservationDate, String reservationTime);

    void updateReservationSlots(long storeNo, String reservationDate, String reservationTime, int newSlots);

    void updateReportCount(long reviewNo);

    List<InquiryDTO> getInquiryList(long userNo, String searchWord);

    List<InquiryDTO> getReportList(long storeNo, String searchWord);

    List<InquiryDTO> getRecentInquiryList(long userNo);

    List<InquiryDTO> getRecentReportList(long storeNo);

//    InquiryDTO getReviewReportInfo(long inquiryNo);

    InquiryDTO getInquiryInfo(long inquiryNo, String table);

    void deleteReviewReport(long inquiryNo);

    void deleteInquiry(long inquiryNo);

    void decreaseReportCount(long reviewNo);
}
