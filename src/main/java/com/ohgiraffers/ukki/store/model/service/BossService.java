package com.ohgiraffers.ukki.store.model.service;

import com.ohgiraffers.ukki.store.controller.ReservationDTO;
import com.ohgiraffers.ukki.store.model.dao.BossMapper;
import com.ohgiraffers.ukki.store.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
@Service
public class BossService {

    @Autowired
    private BossMapper bossMapper;

    // 가게 예약 현황 조회
    public List<ReservationDTO> getReservationStatus(int storeNo) {
        try {
            return bossMapper.selectReservationStatusByStore(storeNo);
        } catch (Exception e) {
            // Log the error for debugging purposes
            throw new RuntimeException("Error fetching reservation status", e);
        }
    }

    // 예약 인원 리스트 조회
    public List<ReservationDTO> getReservationPeopleList(int storeNo) {
        try {
            return bossMapper.selectReservationPeopleList(storeNo);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching reservation people list", e);
        }
    }

    // 예약 가능 인원 조회
    public AvailableSlotsDTO getAvailableSlots(int storeNo, String reservationDate) {
        try {
            // This should return an integer now
            int availableSlots = bossMapper.selectAvailableReservationPeople(storeNo, reservationDate);
            return new AvailableSlotsDTO(availableSlots);  // Wrap the integer inside DTO
        } catch (Exception e) {
            throw new RuntimeException("Error fetching available slots", e);
        }
    }



    // 이번 주 예약 수 조회
    public WeeklyReservationCountDTO getWeeklyReservationCount(int storeNo) {
        try {
            return bossMapper.selectWeeklyReservationCount(storeNo);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching weekly reservation count", e);
        }
    }

    // 오늘 예약 수 조회
    public int getTodayReservationCount(int storeNo) {
        try {
            return bossMapper.selectTodayReservationCount(storeNo);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching today's reservation count", e);
        }
    }

    // 다음 예약 가능한 시간 조회
    public String getNextAvailableTime(int storeNo, String resDate, String currentTime) {
        try {
            return bossMapper.getNextAvailableTime(storeNo, resDate, currentTime);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching next available time", e);
        }
    }

    // 요일별, 시간별 예약 가능 인원 조회
    public List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO) {
        try {
            return bossMapper.getResPosNum(storeResPosNumDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching reservation position numbers", e);
        }
    }


}
