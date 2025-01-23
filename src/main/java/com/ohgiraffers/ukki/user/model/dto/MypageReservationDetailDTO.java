package com.ohgiraffers.ukki.user.model.dto;

import java.sql.Time;
import java.util.Date;

public class MypageReservationDetailDTO {
    private int resNo;
    private String userId;
    private String userName;
    private int storeNo;
    private String storeName;
    private String date;
    private String time;
    private String qr;

    public MypageReservationDetailDTO() {}

    public MypageReservationDetailDTO(int resNo, String userId, String userName, int storeNo, String storeName, String date, String time, String qr) {
        this.resNo = resNo;
        this.userId = userId;
        this.userName = userName;
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.date = date;
        this.time = time;
        this.qr = qr;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "MypageReservationDetailDTO{" +
                "resNo=" + resNo +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", storeNo=" + storeNo +
                ", storeName='" + storeName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", qr='" + qr + '\'' +
                '}';
    }
}
