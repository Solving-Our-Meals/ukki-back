package com.ohgiraffers.ukki.admin.store.model.dto;

public class AdminStoreListDTO {
    private long storeNo;
    private String storeCategory;
    private String storeName;
    private String storeAddress;
    private String storeRegistDate;

    public AdminStoreListDTO() {}

    public AdminStoreListDTO(long storeNo, String storeCategory, String storeName, String storeAddress, String storeRegistDate) {
        this.storeNo = storeNo;
        this.storeCategory = storeCategory;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeRegistDate = storeRegistDate;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public String getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreRegistDate() {
        return storeRegistDate;
    }

    public void setStoreRegistDate(String storeRegistDate) {
        this.storeRegistDate = storeRegistDate;
    }

    @Override
    public String toString() {
        return "AdminStoreListDTO{" +
                "storeNo=" + storeNo +
                ", storeCategory='" + storeCategory + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeRegistDate='" + storeRegistDate + '\'' +
                '}';
    }
}
