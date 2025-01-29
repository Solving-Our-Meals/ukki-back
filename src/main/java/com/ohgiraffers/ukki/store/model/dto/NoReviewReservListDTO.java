package com.ohgiraffers.ukki.store.model.dto;

public class NoReviewReservListDTO {

    private long resNo;
    private boolean isWriteReview;

    public NoReviewReservListDTO(){};

    public NoReviewReservListDTO(long resNo, boolean isWriteReview) {
        this.resNo = resNo;
        this.isWriteReview = isWriteReview;
    }

    public long getResNo() {
        return resNo;
    }

    public void setResNo(long resNo) {
        this.resNo = resNo;
    }

    public boolean isWriteReview() {
        return isWriteReview;
    }

    public void setWriteReview(boolean writeReview) {
        isWriteReview = writeReview;
    }

    @Override
    public String toString() {
        return "NoReviewReservListDTO{" +
                "resNo=" + resNo +
                ", isWriteReview=" + isWriteReview +
                '}';
    }
}
