package com.ohgiraffers.ukki.store.model.dto;

public class ReservationInfoDTO {

    private long resNo;  // 예약 번호
    private String resDate;  // 예약 날짜
    private String resTime;  // 예약 시간
    private byte isRandom;  // 랜덤 예약 여부
    private long userNo;
    private long storeNo;
    private byte qrConfirm;  // QR코드 확인 여부
    private String qr;  // QR코드

    public ReservationInfoDTO(){}

    public ReservationInfoDTO(long resNo, String resDate, String resTime, byte isRandom, long userNo, long storeNo, byte qrConfirm, String qr) {
        this.resNo = resNo;
        this.resDate = resDate;
        this.resTime = resTime;
        this.isRandom = isRandom;
        this.userNo = userNo;
        this.storeNo = storeNo;
        this.qrConfirm = qrConfirm;
        this.qr = qr;
    }

    public long getResNo() {
        return resNo;
    }

    public void setResNo(long resNo) {
        this.resNo = resNo;
    }

    public String getResDate() {
        return resDate;
    }

    public void setResDate(String resDate) {
        this.resDate = resDate;
    }

    public String getResTime() {
        return resTime;
    }

    public void setResTime(String resTime) {
        this.resTime = resTime;
    }

    public int getIsRandom() {
        return isRandom;
    }

    public void setIsRandom(byte isRandom) {
        this.isRandom = isRandom;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public int getQrConfirm() {
        return qrConfirm;
    }

    public void setQrConfirm(byte qrConfirm) {
        this.qrConfirm = qrConfirm;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    @Override
    public String toString() {
        return "ReservationInfoDTO{" +
                "resNo=" + resNo +
                ", resDate='" + resDate + '\'' +
                ", resTime='" + resTime + '\'' +
                ", isRandom=" + isRandom +
                ", userNo=" + userNo +
                ", storeNo=" + storeNo +
                ", qrConfirm=" + qrConfirm +
                ", qr='" + qr + '\'' +
                '}';
    }
}
