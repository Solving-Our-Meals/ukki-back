package com.ohgiraffers.ukki.store.model.service;

import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import com.ohgiraffers.ukki.store.controller.ReservationDTO;
import com.ohgiraffers.ukki.store.model.dao.BossMapper;
import com.ohgiraffers.ukki.store.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BossService {

    @Autowired
    private BossMapper bossMapper;

    // 가게 정보 조회
    public StoreInfoDTO getStoreInfo(long userNo) {
        return bossMapper.getStoreInfo(userNo);
    }

    // 가게 예약 현황 조회
    public List<StoreResPosNumDTO> getReservationStatus(long storeNo, LocalDate reservationDate, LocalTime reservationTime) {
        return bossMapper.selectReservationStatusByStore(storeNo, reservationDate, reservationTime);
    }




    public List<StoreResPosNumDTO> getReservationList(long storeNo, LocalDate reservationDate, LocalTime reservationTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("storeNo", storeNo);
        params.put("reservationDate", reservationDate);
        params.put("reservationTime", reservationTime);

        return bossMapper.selectReservationList(storeNo,reservationDate,reservationTime);
    }




    // 예약 가능 인원 조회
//    public int getAvailableSlots(long storeNo) {
//        try {
//            return bossMapper.findAvailableSlotsByStoreNo(storeNo);
//        } catch (Exception e) {
//            throw new RuntimeException("Error fetching available slots for storeNo: " + storeNo, e);
//        }
//    }



    // 7일간의 예약 정보 조회
//    public List<ReservationInfoDTO> getReservationsForPeriod(long storeNo, LocalDate startDate, LocalDate endDate) {
//        return bossMapper.findReservationsForPeriod(storeNo, startDate, endDate);
//    }

    // 기타 로직은 동일
    public ThisWeekReservationDTO getWeeklyReservationCount(long storeNo) {
        return bossMapper.getWeeklyReservationCount(storeNo);
    }

    public int getTodayReservationCount(long storeNo) {
        return bossMapper.selectTodayReservationCount(storeNo);
    }

//    public String getNextAvailableTime(long storeNo, String resDate, String currentTime) {
//        return bossMapper.getNextAvailableTime(storeNo, resDate, currentTime);
//    }

//    public List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO) {
//        return bossMapper.getResPosNum(storeResPosNumDTO);
//    }

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


    public List<ReservationDTO> getReservationListForTime(long storeNo, LocalDate reservationDate, LocalTime reservationTime) {
        if (reservationTime == null) {
            return bossMapper.getReservationsForDate(storeNo, reservationDate);  // 시간 조건 없는 쿼리 호출
        }
        return bossMapper.getReservationsForDateAndTime(storeNo, reservationDate, reservationTime);  // 기존 쿼리
    }




    public void updateReportCount(long reviewNo) {
        bossMapper.updateReportCount(reviewNo);
    }

    public List<StoreInquiryDTO> getInquiryList(long userNo, String searchWord) {
        return bossMapper.getInquiryList(userNo, searchWord);
    }

    public List<StoreInquiryDTO> getReportList(long storeNo, String searchWord) {
        return bossMapper.getReportList(storeNo, searchWord);
    }

    public List<StoreInquiryDTO> getRecentInquiryList(long userNo) {
        return bossMapper.getRecentInquiryList(userNo);
    }

    public List<StoreInquiryDTO> getRecentReportList(long storeNo) {
        return bossMapper.getRecentReportList(storeNo);
    }


    @Transactional
    public void updateAvailableSlots(long storeNo, LocalDate reservationDate, LocalTime reservationTime, int resPosNumber) {
        try {
            // DTO 생성
            StoreResPosNumDTO storeResPosNum = new StoreResPosNumDTO();
            storeResPosNum.setStoreNo(storeNo);
            storeResPosNum.setReservationDate(reservationDate);
            storeResPosNum.setReservationTime(reservationTime);
            storeResPosNum.setResPosNumber(resPosNumber);

            // 기존 예약 가능한 슬롯을 가져옵니다.
            StoreResPosNumDTO existingSlot = bossMapper.getResPosNumByStoreAndDate(storeNo, reservationDate, reservationTime);

            // 슬롯이 존재하면 업데이트, 존재하지 않으면 추가
            if (existingSlot != null) {
                bossMapper.updateAvailableSlots(storeResPosNum);
            } else {
                bossMapper.insertAvailableSlots(storeResPosNum);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to update or insert available slots", e);
        }
    }


    public StoreInquiryDTO getInquiryInfo(long inquiryNo, String table) {
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


    public void updateReviewReport(StoreInquiryDTO storeInquiryDTO, long inquiryNo) {
        bossMapper.updateReviewReport(storeInquiryDTO, inquiryNo);
    }

    public void updateInquiry(StoreInquiryDTO storeInquiryDTO, long inquiryNo) {
        bossMapper.updateInquiry(storeInquiryDTO, inquiryNo);
    }

    public String getFileName(long inquiryNo) {
        return bossMapper.getFileName(inquiryNo);
    }

    public void insertAvailableSlots(long storeNo, LocalDate reservationDate, LocalTime reservationTime, int resPosNumber) {
        StoreResPosNumDTO storeResPosNumDTO = new StoreResPosNumDTO();
        storeResPosNumDTO.setStoreNo(storeNo);
        storeResPosNumDTO.setReservationDate(reservationDate);
        storeResPosNumDTO.setReservationTime(reservationTime);
        storeResPosNumDTO.setResPosNumber(resPosNumber);

        // rDay가 자동으로 계산되어 설정됩니다.
        bossMapper.insertAvailableSlots(storeResPosNumDTO);
    }



}

