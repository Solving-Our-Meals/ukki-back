package com.ohgiraffers.ukki.user.model.dto;

import java.sql.Time;
import java.util.Date;

public class MypageReservationDTO {
    private String userId;
    private String storeName;
    private Date date;
    private Time time;
    private String qr;

    public MypageReservationDTO() {}

    public MypageReservationDTO(String userId, String storeName, Date date, Time time, String qr) {
        this.userId = userId;
        this.storeName = storeName;
        this.date = date;
        this.time = time;
        this.qr = qr;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    @Override
    public String toString() {
        return "MypageReservationDTO{" +
                "userId='" + userId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", qr='" + qr + '\'' +
                '}';
    }
}
