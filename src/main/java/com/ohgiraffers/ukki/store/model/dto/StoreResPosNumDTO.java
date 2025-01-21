package com.ohgiraffers.ukki.store.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class StoreResPosNumDTO {

    private Long rInfo;  // 예약 정보 번호

    private int storeNo;  // 가게 번호
    private LocalDate rDate;  // 예약 날짜
    private String rDay;
    private LocalTime rOperTime;  // 예약 시간
    private int resPosNumber;  // 예약 가능한 인원 수

    public StoreResPosNumDTO() {
    }

    public StoreResPosNumDTO(Long rInfo, int storeNo, LocalDate rDate, String rDay, LocalTime rOperTime, int resPosNumber) {
        this.rInfo = rInfo;
        this.storeNo = storeNo;
        this.rDate = rDate;
        this.rDay = rDay;
        this.rOperTime = rOperTime;
        this.resPosNumber = resPosNumber;
    }


    public String getrDay() {
        return rDay;
    }

    public void setrDay(String rDay) {
        this.rDay = rDay;
    }

    public Long getrInfo() {
        return rInfo;
    }

    public void setrInfo(Long rInfo) {
        this.rInfo = rInfo;
    }

    public int getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(int storeNo) {
        this.storeNo = storeNo;
    }

    public LocalDate getrDate() {
        return rDate;
    }

    public void setrDate(LocalDate rDate) {
        this.rDate = rDate;
    }

    public LocalTime getrOperTime() {
        return rOperTime;
    }

    public void setrOperTime(LocalTime rOperTime) {
        this.rOperTime = rOperTime;
    }

    public int getResPosNumber() {
        return resPosNumber;
    }

    public void setResPosNumber(int resPosNumber) {
        this.resPosNumber = resPosNumber;
    }

    public void setListDayResPosNumDTO(List<DayResPosNumDTO> listDayResPosNum) {
    }
}