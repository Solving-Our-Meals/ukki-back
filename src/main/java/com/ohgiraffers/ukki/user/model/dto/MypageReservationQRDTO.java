package com.ohgiraffers.ukki.user.model.dto;

public class MypageReservationQRDTO {
    private Long userNo;
    private Long resNo;
    private String qr;

    public MypageReservationQRDTO() {}

    public MypageReservationQRDTO(Long userNo, Long resNo, String qr) {
        this.userNo = userNo;
        this.resNo = resNo;
        this.qr = qr;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
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
                "userNo=" + userNo +
                ", resNo=" + resNo +
                ", qr='" + qr + '\'' +
                '}';
    }
}
