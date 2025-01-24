package com.ohgiraffers.ukki.admin.reservation.model.dto;

public class ReservationListDTO {
    private int resNo;
    private String resDate;
    private String userId;
    private String storeName;
    private String resTime;
    private String isToday;
    private boolean qrConfirm;

    public ReservationListDTO() {}

    public ReservationListDTO(int resNo, String resDate, String userId, String storeName, String resTime, String isToday, boolean qrConfirm) {
        this.resNo = resNo;
        this.resDate = resDate;
        this.userId = userId;
        this.storeName = storeName;
        this.resTime = resTime;
        this.isToday = isToday;
        this.qrConfirm = qrConfirm;
    }

    public int getResNo() {
        return resNo;
    }

    public void setResNo(int resNo) {
        this.resNo = resNo;
    }

    public String getResDate() {
        return resDate;
    }

    public void setResDate(String resDate) {
        this.resDate = resDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getResTime() {
        return resTime;
    }

    public void setResTime(String resTime) {
        if (resTime != null && resTime.endsWith(":00")) {
            this.resTime = resTime.substring(0, resTime.length() - 3);
        } else {
            this.resTime = resTime;
        }
    }

    public String getIsToday() {
        return isToday;
    }

    public void setIsToday(String isToday) {
        this.isToday = isToday;
    }

    public boolean isQrConfirm() {
        return qrConfirm;
    }

    public void setQrConfirm(boolean qrConfirm) {
        this.qrConfirm = qrConfirm;
    }

    @Override
    public String toString() {
        return "ReservationListDTO{" +
                "resNo=" + resNo +
                ", resDate='" + resDate + '\'' +
                ", userId='" + userId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", resTime='" + resTime + '\'' +
                ", isToday='" + isToday + '\'' +
                ", qrConfirm=" + qrConfirm +
                '}';
    }
}
