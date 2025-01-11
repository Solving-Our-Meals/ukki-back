package com.ohgiraffers.ukki.store.model.dto;

public class ChangedResNumInfoDTO {

    private long rInfo;
    private long storeNo;
    private String rDay;
    private String rDate;
    private String rOperTime;
    private int resPosNumber;

    public ChangedResNumInfoDTO(){};

    public ChangedResNumInfoDTO(long rInfo, long storeNo, String rDay, String rDate, String rOperTime, int resPosNumber) {
        this.rInfo = rInfo;
        this.storeNo = storeNo;
        this.rDay = rDay;
        this.rDate = rDate;
        this.rOperTime = rOperTime;
        this.resPosNumber = resPosNumber;
    }

    public long getrInfo() {
        return rInfo;
    }

    public void setrInfo(long rInfo) {
        this.rInfo = rInfo;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public String getrDay() {
        return rDay;
    }

    public void setrDay(String rDay) {
        this.rDay = rDay;
    }

    public String getrDate() {
        return rDate;
    }

    public void setrDate(String rDate) {
        this.rDate = rDate;
    }

    public String getrOperTime() {
        return rOperTime;
    }

    public void setrOperTime(String rOperTime) {
        this.rOperTime = rOperTime;
    }

    public int getResPosNumber() {
        return resPosNumber;
    }

    public void setResPosNumber(int resPosNumber) {
        this.resPosNumber = resPosNumber;
    }

    @Override
    public String toString() {
        return "ChangedResPosNumDTO{" +
                "rInfo=" + rInfo +
                ", storeNo=" + storeNo +
                ", rDay='" + rDay + '\'' +
                ", rDate='" + rDate + '\'' +
                ", rOperTime='" + rOperTime + '\'' +
                ", resPosNumber=" + resPosNumber +
                '}';
    }
}
