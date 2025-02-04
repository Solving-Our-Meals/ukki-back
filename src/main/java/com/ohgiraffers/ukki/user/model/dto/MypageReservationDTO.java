package com.ohgiraffers.ukki.user.model.dto;

import java.sql.Time;
import java.util.Date;

public class MypageReservationDTO {
    private int resNo;
    private String userId;
    private String storeName;
    private String date;
    private String time;
    private String qr;
    private String search;
    private int replyNo;
    private Boolean qrConfirm;
    private int reservationType;

    public MypageReservationDTO() {}

    public MypageReservationDTO(int resNo, String userId, String storeName, String date, String time, String qr, String search, int replyNo, Boolean qrConfirm, int reservationType) {
        this.resNo = resNo;
        this.userId = userId;
        this.storeName = storeName;
        this.date = date;
        this.time = time;
        this.qr = qr;
        this.search = search;
        this.replyNo = replyNo;
        this.qrConfirm = qrConfirm;
        this.reservationType = reservationType;
    }

    public int getResNo() {
        return resNo;
    }

    public void setResNo(int resNo) {
        this.resNo = resNo;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        if (time != null && time.endsWith(":00")) {
            this.time = time.substring(0, time.length() - 3);
        } else {
            this.time = time;
        }
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getReplyNo() {
        return replyNo;
    }

    public void setReplyNo(int replyNo) {
        this.replyNo = replyNo;
    }

    public Boolean getQrConfirm() {
        return qrConfirm;
    }

    public void setQrConfirm(Boolean qrConfirm) {
        this.qrConfirm = qrConfirm;
    }

    public int getReservationType() {
        return reservationType;
    }

    public void setReservationType(int reservationType) {
        this.reservationType = reservationType;
    }

    @Override
    public String toString() {
        return "MypageReservationDTO{" +
                "resNo=" + resNo +
                ", userId='" + userId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", qr='" + qr + '\'' +
                ", search='" + search + '\'' +
                ", replyNo=" + replyNo +
                ", qrConfirm=" + qrConfirm +
                ", reservationType=" + reservationType +
                '}';
    }
}
