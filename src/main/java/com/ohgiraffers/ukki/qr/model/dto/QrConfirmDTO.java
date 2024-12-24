package com.ohgiraffers.ukki.qr.model.dto;

public class QrConfirmDTO {

    private int reservationNo;
    private int storeNo;

    public QrConfirmDTO() {}

    public QrConfirmDTO(int reservationNo, int storeNo) {
        this.reservationNo = reservationNo;
        this.storeNo = storeNo;
    }

    public int getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(int reservationNo) {
        this.reservationNo = reservationNo;
    }

    public int getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(int storeNo) {
        this.storeNo = storeNo;
    }

    @Override
    public String toString() {
        return "QrConfilmDTO{" +
                "reservationNo=" + reservationNo +
                ", storeNo=" + storeNo +
                '}';
    }
}

