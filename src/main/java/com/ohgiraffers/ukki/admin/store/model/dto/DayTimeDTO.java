package com.ohgiraffers.ukki.admin.store.model.dto;

public class DayTimeDTO {
    private long storeNo;
    private String operTime;
    private int posNumber;

    public DayTimeDTO(long storeNo, String operTime, int posNumber) {
        this.storeNo = storeNo;
        this.operTime = operTime;
        this.posNumber = posNumber;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public String getOperTime() {
        return operTime;
    }

    public int getPosNumber() {
        return posNumber;
    }
} 