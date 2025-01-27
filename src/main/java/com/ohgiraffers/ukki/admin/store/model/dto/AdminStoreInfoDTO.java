package com.ohgiraffers.ukki.admin.store.model.dto;

import java.util.List;

public class AdminStoreInfoDTO {

    private long storeNo;
    private String storeName;
    private String storeDes; // 가게소개글
    private String storeAddress;
    private float latitude;  // 가게 위도
    private float longitude; //가게 경도
    private String storeProfile;
    private String storeMenu;
    private int storeCategoryNo;
    private long userNo;
    private int posNumber; // 예약 가능 인원
    private String storeRegistDate;
    private AdminStoreKeywordDTO storeKeyword;
    private List<AdminStoreCategoryDTO> storeCategory;
    private AdminStoreOperationDTO operationTime;

    public AdminStoreInfoDTO() {}

    public AdminStoreInfoDTO(long storeNo, String storeName, String storeDes, String storeAddress, float latitude, float longitude, String storeProfile, String storeMenu, int storeCategoryNo, long userNo, int posNumber, AdminStoreKeywordDTO storeKeyword, List<AdminStoreCategoryDTO> storeCategory, AdminStoreOperationDTO operationTime, String storeRegistDate) {
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.storeDes = storeDes;
        this.storeAddress = storeAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeProfile = storeProfile;
        this.storeMenu = storeMenu;
        this.storeCategoryNo = storeCategoryNo;
        this.userNo = userNo;
        this.posNumber = posNumber;
        this.storeKeyword = storeKeyword;
        this.storeCategory = storeCategory;
        this.operationTime = operationTime;
        this.storeRegistDate = storeRegistDate;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDes() {
        return storeDes;
    }

    public void setStoreDes(String storeDes) {
        this.storeDes = storeDes;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
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

    public String getStoreProfile() {
        return storeProfile;
    }

    public void setStoreProfile(String storeProfile) {
        this.storeProfile = storeProfile;
    }

    public String getStoreMenu() {
        return storeMenu;
    }

    public void setStoreMenu(String storeMenu) {
        this.storeMenu = storeMenu;
    }

    public int getStoreCategoryNo() {
        return storeCategoryNo;
    }

    public void setStoreCategoryNo(int storeCategoryNo) {
        this.storeCategoryNo = storeCategoryNo;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }

    public int getPosNumber() {
        return posNumber;
    }

    public void setPosNumber(int posNumber) {
        this.posNumber = posNumber;
    }

    public AdminStoreKeywordDTO getStoreKeyword() {
        return storeKeyword;
    }

    public void setStoreKeyword(AdminStoreKeywordDTO storeKeyword) {
        this.storeKeyword = storeKeyword;
    }

    public List<AdminStoreCategoryDTO> getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(List<AdminStoreCategoryDTO> storeCategory) {
        this.storeCategory = storeCategory;
    }

    public AdminStoreOperationDTO getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(AdminStoreOperationDTO operationTime) {
        this.operationTime = operationTime;
    }

    public String getStoreRegistDate() {
        return storeRegistDate;
    }

    public void setStoreRegistDate(String storeRegistDate) {
        this.storeRegistDate = storeRegistDate;
    }

    @Override
    public String toString() {
        return "AdminStoreInfoDTO{" +
                "storeNo=" + storeNo +
                ", storeName='" + storeName + '\'' +
                ", storeDes='" + storeDes + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", storeProfile='" + storeProfile + '\'' +
                ", storeMenu='" + storeMenu + '\'' +
                ", storeCategoryNo=" + storeCategoryNo +
                ", userNo=" + userNo +
                ", posNumber=" + posNumber +
                ", storeKeyword=" + storeKeyword +
                ", storeCategory=" + storeCategory +
                ", operationTime=" + operationTime +
                ", storeRegistDate='" + storeRegistDate + '\'' +
                '}';
    }
}


