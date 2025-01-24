package com.ohgiraffers.ukki.admin.reservation.model.dto;

public class ReservationInfoDTO {
    private int resNo;
    private String resDate;
    private int userNo;
    private String userId;
    private int storeNo;
    private String storeName;
    private String resTime;
    private String qr;
    private boolean qrConfirm;

    public ReservationInfoDTO(){}

    public ReservationInfoDTO(int resNo, String resDate, int userNo, String userId, int storeNo, String storeName, String resTime, String qr, boolean qrConfirm) {
        this.resNo = resNo;
        this.resDate = resDate;
        this.userNo = userNo;
        this.userId = userId;
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.resTime = resTime;
        this.qr = qr;
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

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(int storeNo) {
        this.storeNo = storeNo;
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

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public boolean isQrConfirm() {
        return qrConfirm;
    }

    public void setQrConfirm(boolean qrConfirm) {
        this.qrConfirm = qrConfirm;
    }

    @Override
    public String toString() {
        return "ReservationInfoDTO{" +
                "resNo=" + resNo +
                ", resDate='" + resDate + '\'' +
                ", userNo=" + userNo +
                ", userId='" + userId + '\'' +
                ", storeNo=" + storeNo +
                ", storeName='" + storeName + '\'' +
                ", resTime='" + resTime + '\'' +
                ", qr='" + qr + '\'' +
                ", qrConfirm=" + qrConfirm +
                '}';
    }
}
