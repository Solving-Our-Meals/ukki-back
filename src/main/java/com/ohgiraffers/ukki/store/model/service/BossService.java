package com.ohgiraffers.ukki.store.model.service;

import com.ohgiraffers.ukki.store.controller.ReservationDTO;
import com.ohgiraffers.ukki.store.model.dao.BossMapper;
import com.ohgiraffers.ukki.store.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Service
public class BossService {

    @Autowired
    private BossMapper bossMapper;

    // 가게 정보 조회
    public StoreInfoDTO getStoreInfo(long userNo) {
        return bossMapper.getStoreInfo(userNo);
    }

    // 가게 예약 현황 조회
    public List<ReservationDTO> getReservationStatus(int storeNo) {
        return bossMapper.selectReservationStatusByStore(storeNo);
    }

    // 예약 인원 리스트 조회
    public List<ReservationDTO> getReservationPeopleList(int storeNo) {
        return bossMapper.selectReservationPeopleList(storeNo);
    }

    public List<ReservationDTO> getReservationList(long storeNo, String reservationDate, String reservationTime) {
        return bossMapper.selectReservationList(storeNo, reservationDate, reservationTime);
    }

    // 예약 가능 인원 조회
    public int getAvailableSlots(long storeNo) {
        try {
            return bossMapper.findAvailableSlotsByStoreNo(storeNo);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching available slots for storeNo: " + storeNo, e);
        }
    }



    // 7일간의 예약 정보 조회
    public List<ReservationInfoDTO> getReservationsForPeriod(long storeNo, LocalDate startDate, LocalDate endDate) {
        return bossMapper.findReservationsForPeriod(storeNo, startDate, endDate);
    }

    // 기타 로직은 동일
    public WeeklyReservationCountDTO getWeeklyReservationCount(int storeNo) {
        return bossMapper.selectWeeklyReservationCount(storeNo);
    }

    public int getTodayReservationCount(int storeNo) {
        return bossMapper.selectTodayReservationCount(storeNo);
    }

    public String getNextAvailableTime(int storeNo, String resDate, String currentTime) {
        return bossMapper.getNextAvailableTime(storeNo, resDate, currentTime);
    }

    public List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO) {
        return bossMapper.getResPosNum(storeResPosNumDTO);
    }

    // 최신 리뷰 받아오기
    public ReviewContentDTO getRecentReview(long storeNo) {
        return bossMapper.getRecentReview(storeNo);
    }

    // 리뷰 리스트 가져오기
    public ReviewDTO getReviewList(long storeNo) {
        return bossMapper.getReviewList(storeNo);
    }

    // 리뷰 상세 조회
    public DetailReviewInfoDTO getReviewInfo(long reviewNo) {
        return bossMapper.getReviewInfo(reviewNo);
    }

    // 리뷰 신고
    public void reportReview(ReportReviewDTO reportReviewDTO) {
        bossMapper.reportReview(reportReviewDTO);
    }


    public List<ReservationDTO> getReservationListForTime(int storeNo, String reservationDate, String reservationTime) {
        if (reservationTime == null || reservationTime.isEmpty()) {
            return bossMapper.getReservationsForDate(storeNo, reservationDate);  // 시간 조건 없는 쿼리 호출
        }
        return bossMapper.getReservationsForDateAndTime(storeNo, reservationDate, reservationTime);  // 기존 쿼리
    }




    public void updateReportCount(long reviewNo) {
        bossMapper.updateReportCount(reviewNo);
    }

    public List<InquiryDTO> getInquiryList(long userNo, String searchWord) {
        return bossMapper.getInquiryList(userNo, searchWord);
    }

    public List<InquiryDTO> getReportList(long storeNo, String searchWord) {
        return bossMapper.getReportList(storeNo, searchWord);
    }

    public List<InquiryDTO> getRecentInquiryList(long userNo) {
        return bossMapper.getRecentInquiryList(userNo);
    }

    public List<InquiryDTO> getRecentReportList(long storeNo) {
        return bossMapper.getRecentReportList(storeNo);
    }



    public void updateAvailableSlots(int storeNo, LocalDate date, String reservationTime, int resPosNumber) {
        try {
            // StoreResPosNumDTO 객체를 생성하여 필요한 값을 세팅합니다.
            StoreResPosNumDTO storeResPosNum = new StoreResPosNumDTO();
            storeResPosNum.setStoreNo(storeNo);
            storeResPosNum.setrDate(date);  // 예약 날짜 (LocalDate 타입으로 설정)
            storeResPosNum.setResPosNumber(resPosNumber);  // 예약 가능한 인원 수 설정
            storeResPosNum.setrOperTime(LocalTime.parse(reservationTime)); // 예약 시간

            // 1. 예약 정보가 존재하는지 확인
            StoreResPosNumDTO existingSlot = bossMapper.getResPosNumByStoreAndDate(storeNo, date, reservationTime);

            if (existingSlot != null) {
                // 2. 예약 정보가 있으면 업데이트
                bossMapper.updateAvailableSlots(storeResPosNum);
            } else {
                // 3. 예약 정보가 없으면 삽입
                bossMapper.insertAvailableSlots(storeResPosNum);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to update or insert available slots", e);
        }
    }




//    public InquiryDTO getReviewReportInfo(long inquiryNo) {
//        return bossMapper.getReviewReportInfo(inquiryNo);
//    }

    public InquiryDTO getInquiryInfo(long inquiryNo, String table) {
        return bossMapper.getInquiryInfo(inquiryNo, table);
    }

    public void deleteReviewReport(long inquiryNo) {
        bossMapper.deleteReviewReport(inquiryNo);
    }

    public void deleteInquiry(long inquiryNo) {
        bossMapper.deleteInquiry(inquiryNo);
    }

    public void decreaseReportCount(long reviewNo) {
        bossMapper.decreaseReportCount(reviewNo);
    }


    public void updateReviewReport(InquiryDTO inquiryDTO, long inquiryNo) {
        bossMapper.updateReviewReport(inquiryDTO, inquiryNo);
    }

    public void updateInquiry(InquiryDTO inquiryDTO, long inquiryNo) {
        bossMapper.updateInquiry(inquiryDTO, inquiryNo);
    }

    public String getFileName(long inquiryNo) {
        return bossMapper.getFileName(inquiryNo);
    }

}

