package com.ohgiraffers.ukki.store.controller;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDTO {
    private int reservationNo;
    private String reservationDate;
    private String reservationTime;
    private int reservationPeople;
    private String storeName;
    private int storeNo;
        private LocalDate resDate;
        private LocalTime resTime;
        private String userName;


    public ReservationDTO() {
    }

    public ReservationDTO(int reservationNo, String reservationDate, String reservationTime, int reservationPeople, String storeName, int storeNo, LocalDate resDate, LocalTime resTime, String userName) {

        this.reservationNo = reservationNo;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.reservationPeople = reservationPeople;
        this.storeName = storeName;
        this.storeNo = storeNo;
        this.resDate = resDate;
        this.resTime = resTime;
        this.userName = userName;
    }

    public int getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(int reservationNo) {
        this.reservationNo = reservationNo;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getReservationPeople() {
        return reservationPeople;
    }

    public void setReservationPeople(int reservationPeople) {
        this.reservationPeople = reservationPeople;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(int storeNo) {
        this.storeNo = storeNo;
    }

    public LocalDate getResDate() {
        return resDate;
    }

    public void setResDate(LocalDate resDate) {
        this.resDate = resDate;
    }

    public LocalTime getResTime() {
        return resTime;
    }

    public void setResTime(LocalTime resTime) {
        this.resTime = resTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "reservationNo=" + reservationNo +
                ", reservationDate='" + reservationDate + '\'' +
                ", reservationTime='" + reservationTime + '\'' +
                ", reservationPeople=" + reservationPeople +
                ", storeName='" + storeName + '\'' +
                ", storeNo=" + storeNo +
                ", resDate=" + resDate +
                ", resTime=" + resTime +
                ", userName='" + userName + '\'' +
                '}';
    }
}
