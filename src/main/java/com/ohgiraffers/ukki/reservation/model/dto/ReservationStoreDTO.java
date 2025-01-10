package com.ohgiraffers.ukki.reservation.model.dto;

public class ReservationStoreDTO {

    private long StoreNo;
    private String storeName;
    private String storeProfile;
    private float latitude;  // 가게 위도
    private float longitude;

    public ReservationStoreDTO() {}

    public ReservationStoreDTO(long storeNo, String storeName, String storeProfile, float latitude, float longitude) {
        StoreNo = storeNo;
        this.storeName = storeName;
        this.storeProfile = storeProfile;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getStoreNo() {
        return StoreNo;
    }

    public void setStoreNo(long storeNo) {
        StoreNo = storeNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreProfile() {
        return storeProfile;
    }

    public void setStoreProfile(String storeProfile) {
        this.storeProfile = storeProfile;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "ReservationStoreDTO{" +
                "StoreNo=" + StoreNo +
                ", storeName='" + storeName + '\'' +
                ", storeProfile='" + storeProfile + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
