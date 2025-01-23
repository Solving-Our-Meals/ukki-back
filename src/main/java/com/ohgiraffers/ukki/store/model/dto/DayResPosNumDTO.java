package com.ohgiraffers.ukki.store.model.dto;

public class DayResPosNumDTO {

    private String operTime;  // 예약 가능한 시간대
    private int resPosNum; // 예약 가능한 인원 수

    public DayResPosNumDTO() {}

    public DayResPosNumDTO(String operTime, int resPosNum) {
        this.operTime = operTime;
        this.resPosNum = resPosNum;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public int getResPosNum() {
        return resPosNum;
    }

    public void setResPosNum(int resPosNum) {
        this.resPosNum = resPosNum;
    }

    @Override
    public String toString() {
        return "DayResPosNumDTO{" +
                "operTime='" + operTime + '\'' +
                ", resPosNum=" + resPosNum +
                '}';
    }
}
