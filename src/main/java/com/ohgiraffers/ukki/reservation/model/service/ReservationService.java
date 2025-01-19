package com.ohgiraffers.ukki.reservation.model.service;

import com.ohgiraffers.ukki.reservation.model.dao.ReservationMapper;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationInfoDTO;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationStoreDTO;
import com.ohgiraffers.ukki.reservation.model.dto.StoreBannerDTO;
import com.ohgiraffers.ukki.store.model.dto.OperationDTO;
import com.ohgiraffers.ukki.store.model.service.StoreService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.DayOfWeek.*;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final StoreService storeService;

    // 하나의 생성자로 의존성 주입
    public ReservationService(ReservationMapper reservationMapper, StoreService storeService) {
        this.reservationMapper = reservationMapper;
        this.storeService = storeService;
    }

    // 가장 가까운 예약 가능한 시간 조회
    public LocalTime getNextAvailableTime(long storeNo) {
        // 해당 가게의 운영 시간 가져오기
        OperationDTO operationDTO = storeService.getOperation(storeNo);

        // 현재 시간 가져오기
        LocalTime currentTime = LocalTime.now();

        // 8시부터 22시 30분까지의 예약 가능한 시간대 리스트 만들기
        List<LocalTime> availableTimes = getAvailableTimes(operationDTO);

        // 가장 가까운 예약 시간 찾기
        for (LocalTime availableTime : availableTimes) {
            if (availableTime.isAfter(currentTime)) {
                return availableTime; // 현재 시간 이후 가장 가까운 예약 시간
            }
        }

        return null; // 예약 가능한 시간이 없으면 null 반환
    }

    // 운영 시간에 맞는 예약 가능한 시간대 생성
    private List<LocalTime> getAvailableTimes(OperationDTO operationDTO) {
        // 예약 가능한 시간 리스트 만들기
        List<LocalTime> availableTimes = new ArrayList<>();
        LocalTime startTime = LocalTime.of(8, 0);  // 08:00
        LocalTime endTime = LocalTime.of(22, 30); // 22:30

        // 운영 시간에 맞춰서 예약 가능한 시간 추가
        for (LocalTime time = startTime; !time.isAfter(endTime); time = time.plusMinutes(30)) {
            if (isStoreOpenOnDay(operationDTO, time)) {
                availableTimes.add(time);
            }
        }

        return availableTimes;
    }

    // 해당 시간에 가게가 열려있는지 확인
    private boolean isStoreOpenOnDay(OperationDTO operationDTO, LocalTime time) {
        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();

        switch (today.getDayOfWeek()) {
            case MONDAY:
                return operationDTO.getMonday().equals("OPEN");
            case TUESDAY:
                return operationDTO.getTuesday().equals("OPEN");
            case WEDNESDAY:
                return operationDTO.getWednesday().equals("OPEN");
            case THURSDAY:
                return operationDTO.getThursday().equals("OPEN");
            case FRIDAY:
                return operationDTO.getFriday().equals("OPEN");
            case SATURDAY:
                return operationDTO.getSaturday().equals("OPEN");
            case SUNDAY:
                return operationDTO.getSunday().equals("OPEN");
            default:
                return false;
        }
    }

    // 예약 처리 메서드
    public void makeReservation(ReservationInfoDTO reservationInfoDTO) {
        // 예약이 완료된 후, 필요한 추가 처리 (예: DB에 저장 등)
        reservationMapper.insertReservation(reservationInfoDTO);
    }

    // 가게 대표 사진 조회
    public StoreBannerDTO getRepPhotoName(long storeNo) {
        return reservationMapper.getRepPhotoName(storeNo);
    }

    // 예약된 가게 정보 조회
    public ReservationStoreDTO getReservationedStoreInfo(long storeNo) {
        return reservationMapper.getReservationedStoreInfo(storeNo);
    }

    // 예약 삽입
    public void insertReservation(ReservationInfoDTO reservationInfoDTO) {
        reservationMapper.insertReservation(reservationInfoDTO);
    }

    // 이메일 조회
    public String getEmail(ReservationInfoDTO reservationInfoDTO) {
        return reservationMapper.getEmail(reservationInfoDTO);
    }

    // 예약 횟수 증가
    public void increaseReservation(long userNo) {
        reservationMapper.increaseReservation(userNo);
    }

    public List<String> getAvailableSlots(long storeNo, String reservationDate) {
        // 해당 가게의 운영 시간 가져오기
        OperationDTO operationDTO = storeService.getOperation(storeNo);

        // 예약 가능한 시간 리스트 만들기
        List<String> availableSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(8, 0);  // 08:00
        LocalTime endTime = LocalTime.of(22, 30); // 22:30

        // 운영 시간에 맞춰서 예약 가능한 시간 추가
        for (LocalTime time = startTime; !time.isAfter(endTime); time = time.plusMinutes(30)) {
            if (isStoreOpenOnDay(operationDTO, time)) {
                availableSlots.add(time.toString());
            }
        }

        return availableSlots;
    }
}
