package com.ohgiraffers.ukki.store.model.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class StoreResPosNumDTO {

    private Long rInfo;  // 예약 정보 번호
    private long storeNo;  // 가게 번호
    private LocalDate reservationDate;  // 예약 날짜
    private String rDay;
    private LocalTime reservationTime;  // 예약 시간
    private int resPosNumber;  // 예약 가능한 인원 수
    private List<DayResPosNumDTO> listDayResPosNumDTO;  // 추가된 필드: 예약 가능한 일별 인원 정보 리스트

    public StoreResPosNumDTO (){};

    public StoreResPosNumDTO(Long rInfo, long storeNo, LocalDate reservationDate, String rDay, LocalTime reservationTime, int resPosNumber, List<DayResPosNumDTO> listDayResPosNumDTO) {
        this.rInfo = rInfo;
        this.storeNo = storeNo;
        this.reservationDate = reservationDate;
        this.rDay = rDay;
        this.reservationTime = reservationTime;
        this.resPosNumber = resPosNumber;
        this.listDayResPosNumDTO = listDayResPosNumDTO;
    }

    public Long getrInfo() {
        return rInfo;
    }

    public void setrInfo(Long rInfo) {
        this.rInfo = rInfo;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public String getrDay() {
        return rDay;
    }

    public void setrDay(String rDay) {
        this.rDay = rDay;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getResPosNumber() {
        return resPosNumber;
    }

    public void setResPosNumber(int resPosNumber) {
        this.resPosNumber = resPosNumber;
    }

    public List<DayResPosNumDTO> getListDayResPosNumDTO() {
        return listDayResPosNumDTO;
    }

    public void setListDayResPosNumDTO(List<DayResPosNumDTO> listDayResPosNumDTO) {
        this.listDayResPosNumDTO = listDayResPosNumDTO;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
        // 예약 날짜가 설정될 때 자동으로 요일 계산
        if (reservationDate != null) {
            this.rDay = reservationDate.getDayOfWeek().toString();
        }
    }

    @Override
    public String toString() {
        return "StoreResPosNumDTO{" +
                "rInfo=" + rInfo +
                ", storeNo=" + storeNo +
                ", reservationDate=" + reservationDate +
                ", rDay='" + rDay + '\'' +
                ", reservationTime=" + reservationTime +
                ", resPosNumber=" + resPosNumber +
                ", listDayResPosNumDTO=" + listDayResPosNumDTO +
                '}';
    }

    //    // Getter, Setter를 추가하여 MyBatis 매핑을 반듯하게
//    public List<DayResPosNumDTO> getResPosNumList() {
//        return resPosNumList;
//    }
//
//    public void setResPosNumList(List<DayResPosNumDTO> resPosNumList) {
//        this.resPosNumList = resPosNumList;
//    }

}

