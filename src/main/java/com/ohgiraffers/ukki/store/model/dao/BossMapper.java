package com.ohgiraffers.ukki.store.model.dao;

import com.ohgiraffers.ukki.store.controller.ReservationDTO;
import com.ohgiraffers.ukki.store.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface BossMapper {

    // 가게 정보 조회
    StoreInfoDTO getStoreInfo(long userNo);

    List<StoreResPosNumDTO> selectReservationStatusByStore(long storeNo, LocalDate reservationDate,LocalTime reservationTime);

    List<ReservationDTO> selectReservationPeopleList(long storeNo);

//    int selectAvailableReservationPeople(long storeNo, LocalDate reservationDate);

    WeeklyReservationCountDTO getWeeklyReservationCount(long storeNo);

    int selectTodayReservationCount(long storeNo);



    String getNextAvailableTime(long storeNo, String resDate, String currentTime);

    List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO);

    // 최신 리뷰 조회
    ReviewContentDTO getRecentReview(long storeNo);

    // 리뷰 리스트 가져오기
    ReviewDTO getReviewList(long storeNo);

    // 리뷰 상세 조회
    DetailReviewInfoDTO getReviewInfo(long reviewNo);

    // 리뷰 신고
    void reportReview(ReportReviewDTO reportReviewDTO);

//    int findAvailableSlotsByStoreNo(@Param("storeNo") long storeNo);

    // 예약 가능 인원 업데이트
    void updateAvailableSlots(StoreResPosNumDTO storeResPosNum);



    // 7일간의 예약 정보 조회
    List<StoreReservationInfoDTO> findReservationsForPeriod(@Param("storeNo") long storeNo,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);

    StoreResPosNumDTO getResPosNumByStoreAndDate(@Param("storeNo") long storeNo, @Param("reservationDate") LocalDate reservationDate, @Param("reservationTime") LocalTime reservationTime);


    List<ReservationDTO> getReservationsForDateAndTime(long storeNo, LocalDate reservationDate, LocalTime reservationTime);
    List<ReservationDTO> getReservationsForDate(long storeNo, LocalDate reservationDate);

    List<StoreResPosNumDTO> selectReservationList(long storeNo, LocalDate reservationDate, LocalTime reservationTime);

    void updateReportCount(long reviewNo);

    List<StoreInquiryDTO> getInquiryList(long userNo, String searchWord);

    List<StoreInquiryDTO> getReportList(long storeNo, String searchWord);

    List<StoreInquiryDTO> getRecentInquiryList(long userNo);

    List<StoreInquiryDTO> getRecentReportList(long storeNo);


//    InquiryDTO getReviewReportInfo(long inquiryNo);

    StoreInquiryDTO getInquiryInfo(long inquiryNo, String table);

    void deleteReviewReport(long inquiryNo);

    void deleteInquiry(long inquiryNo);

    void decreaseReportCount(long reviewNo);

    void updateReviewReport(StoreInquiryDTO storeInquiryDTO, long inquiryNo);

    void updateInquiry(StoreInquiryDTO storeInquiryDTO, long inquiryNo);

    String getFileName(long inquiryNo);

    void insertAvailableSlots(StoreResPosNumDTO storeResPosNumDTO);
}
