package com.ohgiraffers.ukki.store.controller;

public class ReservationDTO {
    private int reservationNo;
    private String reservationDate;
    private String reservationTime;
    private int reservationPeople;
    private String storeName;
    private int storeNo;

    public ReservationDTO(int reservationNo, String reservationDate, String reservationTime, int reservationPeople, String storeName, int storeNo) {
        this.reservationNo = reservationNo;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.reservationPeople = reservationPeople;
        this.storeName = storeName;
        this.storeNo = storeNo;
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

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "reservationNo=" + reservationNo +
                ", reservationDate='" + reservationDate + '\'' +
                ", reservationTime='" + reservationTime + '\'' +
                ", reservationPeople=" + reservationPeople +
                ", storeName='" + storeName + '\'' +
                ", storeNo=" + storeNo +
                '}';
    }
}
