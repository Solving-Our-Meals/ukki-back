package com.ohgiraffers.ukki.user.model.dto;

import java.sql.Time;
import java.util.Date;

public class MypageReservationDTO {
    private int resNo;
    private String userId;
    private String storeName;
    private Date date;
    private Time time;
    private String qr;
    private String search;
    private int replyNo;

    public MypageReservationDTO() {}

    public MypageReservationDTO(int resNo, String userId, String storeName, Date date, Time time, String qr, String search, int replyNo) {
        this.resNo = resNo;
        this.userId = userId;
        this.storeName = storeName;
        this.date = date;
        this.time = time;
        this.qr = qr;
        this.search = search;
        this.replyNo = replyNo;
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

    @Override
    public String toString() {
        return "MypageReservationDTO{" +
                "resNo=" + resNo +
                ", userId='" + userId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", qr='" + qr + '\'' +
                ", search='" + search + '\'' +
                ", replyNo=" + replyNo +
                '}';
    }
}
