package com.ohgiraffers.ukki.user.model.dto;

public class MypageReservationQRDTO {
    private Long resNo;
    private String qr;

    public MypageReservationQRDTO() {}

    public MypageReservationQRDTO(Long resNo, String qr) {
        this.resNo = resNo;
        this.qr = qr;
    }

    public Long getResNo() {
        return resNo;
    }

    public void setResNo(Long resNo) {
        this.resNo = resNo;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    @Override
    public String toString() {
        return "MypageReservationQRDTO{" +
                "resNo=" + resNo +
                ", qr='" + qr + '\'' +
                '}';
    }
}
