package com.ohgiraffers.ukki.store.model.dto;

public class DayResPosNumDTO {

    private String operTime;  // 예약 가능한 시간대
    private int resPosNumber; // 예약 가능한 인원 수

    public DayResPosNumDTO() {}

    public DayResPosNumDTO(String operTime, int resPosNumber) {
        this.operTime = operTime;
        this.resPosNumber = resPosNumber;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public int getResPosNumber() {
        return resPosNumber;
    }

    public void setResPosNumber(int resPosNumber) {
        this.resPosNumber = resPosNumber;
    }
}
