package com.ohgiraffers.ukki.reservation.model.dto;

public class StoreBannerDTO {

    private long storeNo;
    private String repPhoto;

    public StoreBannerDTO() {}

    public StoreBannerDTO(long storeNo, String repPhoto) {
        this.storeNo = storeNo;
        this.repPhoto = repPhoto;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public String getRepPhoto() {
        return repPhoto;
    }

    public void setRepPhoto(String repPhoto) {
        this.repPhoto = repPhoto;
    }

    @Override
    public String toString() {
        return "StoreBannerDTO{" +
                "storeNo=" + storeNo +
                ", banner1='" + repPhoto + '\'' +
                '}';
    }
}
